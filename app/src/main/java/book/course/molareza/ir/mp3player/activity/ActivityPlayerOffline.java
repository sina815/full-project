package book.course.molareza.ir.mp3player.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.Helper;
import book.course.molareza.ir.mp3player.MyToast;
import book.course.molareza.ir.mp3player.R;

public class ActivityPlayerOffline extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, SeekBar.OnSeekBarChangeListener
        , MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    public static MediaPlayer mediaPlayer;

    public int po;
    private long totalTime, currentTime;
    private Toolbar toolbar;
    private ImageView imgBlur, imgMain, imgRepeat, imgPrev, imgPlay, imgNext;
    private TextView txtCurrentTime, txtTotalTime, txtSingerPlayer;
    private SeekBar seekBarPlayer;
    private boolean isRepeat = false;
    private String image, name, album, mp3;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_offline);

        toolbar = (Toolbar) findViewById(R.id.toolbarPlayOffline);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int result = G.audioManager.requestAudioFocus(ActivityPlayerOffline.this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//            G.notificationManager.cancel(0);
//        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        imgBlur = (ImageView) findViewById(R.id.imgBlurOffline);
        imgMain = (ImageView) findViewById(R.id.imgMainOffline);
        imgRepeat = (ImageView) findViewById(R.id.imfRepeatOffline);
        imgPrev = (ImageView) findViewById(R.id.imgPrevOffline);
        imgPlay = (ImageView) findViewById(R.id.imgPlayOffline);
        imgNext = (ImageView) findViewById(R.id.imgNextOffline);
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTimeOffline);
        txtTotalTime = (TextView) findViewById(R.id.txtTotalTimeOffline);
        txtSingerPlayer = (TextView) findViewById(R.id.txtSingerPlayerOffline);
        seekBarPlayer = (SeekBar) findViewById(R.id.seekBarPlayerOffline);

        imgRepeat.setEnabled(false);
        imgPrev.setEnabled(false);
        imgPlay.setEnabled(false);
        imgNext.setEnabled(false);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            name = bundle.getString("NAME");
            album = bundle.getString("ALBUM");
            image = bundle.getString("IMAGE");
            mp3 = bundle.getString("MP3");
        }

        if (seekBarPlayer != null) {

            seekBarPlayer.setOnSeekBarChangeListener(this);
        }

//        mediaPlayer = new MediaPlayer();////////////////////////
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

        txtSingerPlayer.setText(name + "  " + "آلبوم" + "  " + album);

        Bitmap imgBitmap = BitmapFactory.decodeFile(image);
        imgMain.setImageBitmap(imgBitmap);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Play
//            mediaPlayer.reset();
//            StartPlayer startPlayer = new StartPlayer();
//            startPlayer.execute();
//            Log.i("TATT", "1: " );

        }

        Runnable run = new Runnable() {
            @Override
            public void run() {

                imgMain.buildDrawingCache();
                Bitmap mainBlur = imgMain.getDrawingCache();
                imgBlur.setImageBitmap(Helper.blur(G.context, mainBlur));
            }
        };
        G.HANDLER.postDelayed(run, 100);


        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.mipmap.play);
                    imgPrev.setEnabled(false);
                    imgNext.setEnabled(false);

                } else {

                    mediaPlayer.start();
                    imgPlay.setImageResource(R.mipmap.pause);
                    update_seekBar_timer();
                    imgPrev.setEnabled(true);
                    imgNext.setEnabled(true);

                }
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int totalTime = mediaPlayer.getDuration();

                int currentTime = mediaPlayer.getCurrentPosition();

                if (currentTime + 5000 >= totalTime) {

                    mediaPlayer.seekTo(totalTime);

                } else {
                    mediaPlayer.seekTo(currentTime + 5000);
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
                    MyToast.makeText(ActivityPlayerOffline.this, "حالت تکرار فعال شد", Toast.LENGTH_SHORT).show();

                } else {

                    isRepeat = false;
                    imgRepeat.setColorFilter(getResources().getColor(R.color.tab_text_select), PorterDuff.Mode.SRC_IN);
                    MyToast.makeText(ActivityPlayerOffline.this, "حالت تکرار غیرفعال شد", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause
            mediaPlayer.pause();
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            // Stop or pause depending on your need
            mediaPlayer.stop();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBarPlayer.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

//        if (!isRepeat) {
//            seekBarPlayer.setProgress(0);
//            txtCurrentTime.setText("00:00");
//            imgPlay.setImageResource(R.mipmap.play);
//            imgPrev.setEnabled(false);
//            imgNext.setEnabled(false);
//            MyToast.makeText(ActivityPlayerOffline.this, "برای اجرای متوالی حالت تکرار را فعال کنید", Toast.LENGTH_SHORT).show();
//
//        } else {
//
//            seekBarPlayer.setProgress(0);
//            txtCurrentTime.setText("00:00");
//            mediaPlayer.start();
//            update_seekBar_timer();
//            MyToast.makeText(ActivityPlayerOffline.this, "تکرار آهنگ", Toast.LENGTH_SHORT).show();
//        }

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


            if (!isRepeat && currentTime >= totalTime) {
                imgPlay.setImageResource(R.mipmap.pause);
                mediaPlayer.stop();
            }
        }
    }

    private void notification() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        Intent intent = new Intent(ActivityPlayerOffline.this, ActivityPlayerOffline.class);
        intent.putExtra("NAME", name);
        intent.putExtra("ALBUM", album);
        intent.putExtra("IMAGE", image);
        intent.putExtra("MP3", mp3);

        PendingIntent pIntent = PendingIntent.getActivity(G.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent buttonPlayIntent = new Intent(this, NotificationPlayButtonHandler.class);
        PendingIntent buttonPlayPendingIntent = PendingIntent.getBroadcast(this, 0, buttonPlayIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imgPauseNotify, buttonPlayPendingIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(G.context)

                .setSmallIcon(R.mipmap.play)
                .setTicker(name)
                .setAutoCancel(true)
                .setContent(remoteViews)
                .setContentIntent(pIntent);


        //  imgMain.buildDrawingCache();
        Bitmap bt = imgMain.getDrawingCache();

        remoteViews.setTextViewText(R.id.txtSingerNotify, name);
        remoteViews.setTextViewText(R.id.txtAlbumNotify, album);
        remoteViews.setImageViewBitmap(R.id.imgNotify, bt);

        G.notificationManager.notify(0, builder.build());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer.isPlaying()) {

            notification();
        }
    }

    /**
     * Called when user clicks the "play/pause" button on the on-going system Notification.
     */

    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
    public static final String SERVICECMD = "com.android.music.musicservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDSTOP = "stop";
    public class NotificationPlayButtonHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//
//            G.currentActivity.finish();
//            Intent intent1 = new Intent(G.currentActivity, ActivityMain.class);
//            G.currentActivity.startActivity(intent1);
//            G.notificationManager.cancel(0);
//            mediaPlayer.stop();
//            MyToast.makeText(context, "پخش موزیک متوقف شد", Toast.LENGTH_SHORT).show();

            AudioManager mAudioManager = (AudioManager) G.context.getSystemService(Context.AUDIO_SERVICE);

            if(mAudioManager.isMusicActive()) {
                Intent i = new Intent(SERVICECMD);
                i.putExtra(CMDNAME , CMDSTOP );
                ActivityPlayerOffline.this.sendBroadcast(i);
            }

        }
    }

    public class StartPlayer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Uri uri = Uri.parse(mp3);
//                mediaPlayer.setDataSource(G.context, uri);
//                mediaPlayer.setDataSource(mp3);
                mediaPlayer = MediaPlayer.create(G.context , uri);
              //  mediaPlayer.prepare();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mediaPlayer.start();
            imgRepeat.setEnabled(true);
            imgPrev.setEnabled(true);
            imgPlay.setEnabled(true);
            imgNext.setEnabled(true);
            imgPlay.setImageResource(R.mipmap.pause);

            update_seekBar_timer();

            Log.i("TATT", "test1111111: ");
        }
    }

}
