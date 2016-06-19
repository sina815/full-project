package book.course.molareza.ir.mp3player.activity;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.Helper;
import book.course.molareza.ir.mp3player.R;

public class ActivityPlayer extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, SeekBar.OnSeekBarChangeListener
        , MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    private Toolbar toolbar;

    private ImageView imgBlur, imgMain, imgRepeat, imgPrev, imgPlay, imgNext, imgDownload;
    private TextView txtCurrentTime, txtTotalTime, txtSingerPlayer;
    private SeekBar seekBarPlayer;

    private String urlImage, name, album, urlMp3_64, urlMp3_128;

    private MediaPlayer mediaPlayer;
    private boolean isRepeat = false;

    long totalTime, currentTime;

    private int po;
    private String id = "";
    private String table = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int result = G.audioManager.requestAudioFocus( ActivityPlayer.this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);



        //   Log.i("TAGURL", "onCreate: " + urlImage);

        imgBlur = (ImageView) findViewById(R.id.imgBlur);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        imgRepeat = (ImageView) findViewById(R.id.imfRepeat);
        imgPrev = (ImageView) findViewById(R.id.imgPrev);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgNext = (ImageView) findViewById(R.id.imgNext);
        imgDownload = (ImageView) findViewById(R.id.imgDownload);
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        txtTotalTime = (TextView) findViewById(R.id.txtTotalTime);
        txtSingerPlayer = (TextView) findViewById(R.id.txtSingerPlayer);
        seekBarPlayer = (SeekBar) findViewById(R.id.seekBarPlayer);

        if (seekBarPlayer != null) {

            seekBarPlayer.setOnSeekBarChangeListener(this);
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        imgRepeat.setEnabled(false);
        imgPrev.setEnabled(false);
        imgPlay.setEnabled(false);
        imgNext.setEnabled(false);
        imgDownload.setEnabled(false);

        toolbar = (Toolbar) findViewById(R.id.toolbarPlay);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            urlImage = bundle.getString("URL_IMAGE");
            name = bundle.getString("NAME");
            album = bundle.getString("ALBUM");
            urlMp3_64 = bundle.getString("URL_MP3_64");
            urlMp3_128 = bundle.getString("URL_MP3_128");
            id = bundle.getString("ID");
            po = bundle.getInt("PO");
            table = bundle.getString("TABLE");
        }

        sendVisit();

        txtSingerPlayer.setText(name + "  " + "آلبوم" + "  " + album);

        Picasso.with(G.context).load(urlImage).into(imgMain);

        Runnable run = new Runnable() {
            @Override
            public void run() {

                imgMain.buildDrawingCache();
                Bitmap mainBlur = imgMain.getDrawingCache();
                imgBlur.setImageBitmap(Helper.blur(G.context, mainBlur));
            }
        };
        G.HANDLER.postDelayed(run, 1000);

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ninja);
//        Bitmap blur = Helper.blur(G.context, bitmap);
//        imgBlur.setImageBitmap(blur);


        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.mipmap.play);
                } else {

                    mediaPlayer.start();
                    imgPlay.setImageResource(R.mipmap.pause);
                    update_seekBar_timer();
                }
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentTime = mediaPlayer.getCurrentPosition();

                if (currentTime + 5000 <= mediaPlayer.getDuration()) {

                    mediaPlayer.seekTo(currentTime + 5000);

                } else {

                    mediaPlayer.seekTo(0);
                }

            }
        });

        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTime = mediaPlayer.getCurrentPosition();

                if (currentTime - 5000 >= 0) {

                    mediaPlayer.seekTo(currentTime - 5000);

                } else {

                    mediaPlayer.seekTo(0);
                }
            }
        });

        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRepeat) {

                    isRepeat = true;
                    imgRepeat.setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
                    Toast.makeText(ActivityPlayer.this, "حالت تکرار فعال شد", Toast.LENGTH_SHORT).show();


                } else {

                    isRepeat = false;
                    imgRepeat.setColorFilter(getResources().getColor(R.color.tab_text_select), PorterDuff.Mode.SRC_IN);
                    Toast.makeText(ActivityPlayer.this, "حالت تکرار غیرفعال شد", Toast.LENGTH_SHORT).show();
                }


            }
        });

//        if (!mediaPlayer.isPlaying()) {

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Play
            new startPlay().execute();
        }
//        }


    }


    //seekBar change listener

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarPlayer.setSecondaryProgress(percent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        int totalTime = mediaPlayer.getDuration();
        int curentProgressSeekBar = Helper.progressToTimer(seekBar.getProgress(), totalTime);
        mediaPlayer.seekTo(curentProgressSeekBar);
        update_seekBar_timer();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (!isRepeat) {

            seekBarPlayer.setProgress(0);
            txtCurrentTime.setText("00:00");
            imgPlay.setImageResource(R.mipmap.play);
            Toast.makeText(ActivityPlayer.this, "repeat off", Toast.LENGTH_SHORT).show();

        } else {

            seekBarPlayer.setProgress(0);
            mediaPlayer.setLooping(true);
            update_seekBar_timer();
            Toast.makeText(ActivityPlayer.this, "repeat on", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            // Stop or pause depending on your need
            mediaPlayer.stop();
        }
    }


    private class startPlay extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                mediaPlayer.setDataSource(urlMp3_64);
                mediaPlayer.prepare();
                //  Log.i("TAGURL", "onCreate: " + urlMp3_64);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            mediaPlayer.start();
            imgRepeat.setEnabled(true);
            imgPrev.setEnabled(true);
            imgPlay.setEnabled(true);
            imgNext.setEnabled(true);
            imgDownload.setEnabled(true);
            imgPlay.setImageResource(R.mipmap.pause);

            update_seekBar_timer();


        }
    }

    private void update_seekBar_timer() {

        if (mediaPlayer.isPlaying()) {
            currentTime = mediaPlayer.getCurrentPosition();
            totalTime = mediaPlayer.getDuration();

            txtTotalTime.setText("" + Helper.milliSecondsToTimer(totalTime));
            txtCurrentTime.setText("" + Helper.milliSecondsToTimer(currentTime));

            int progress = Helper.getProgressPercentage(currentTime, totalTime);
            seekBarPlayer.setProgress(progress);

            Runnable notif = new Runnable() {
                @Override
                public void run() {
                    update_seekBar_timer();
                }
            };
            G.HANDLER.postDelayed(notif, 1000);

            if (currentTime == totalTime) {

                mediaPlayer.stop();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            //  mediaPlayer.stop();
        }

    }

    private void sendVisit() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_VISIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    String state = object.getString("state");
                    int st = Integer.parseInt(state);

                    if (st == 200) {

                        String vs = object.getString("visit");
                        int vPlus = Integer.parseInt(vs);

                        // TODO: 6/19/2016 problem set visit after chenge in database  ( adaptermusic notifiysetchange)

//                        Toast.makeText(ActivityPlayer.this, "" + po, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ActivityPlayer.this, "" + vPlus, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("visit", "1");
                params.put("table", table);
                params.put("id", id);

                return params;

            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }

}
