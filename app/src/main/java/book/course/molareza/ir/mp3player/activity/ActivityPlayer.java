package book.course.molareza.ir.mp3player.activity;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.Helper;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterMusicIKhareji;
import book.course.molareza.ir.mp3player.adapter.AdapterMusicKhareji;
import book.course.molareza.ir.mp3player.db.FavoriteMusicIrani;
import book.course.molareza.ir.mp3player.db.FavoriteMusicIraniDao;
import book.course.molareza.ir.mp3player.db.FavoriteMusicKhareji;
import book.course.molareza.ir.mp3player.db.FavoriteMusicKharejiDao;
import book.course.molareza.ir.mp3player.db.LikeMusicIrani;
import book.course.molareza.ir.mp3player.db.LikeMusicIraniDao;
import book.course.molareza.ir.mp3player.db.LikeMusicKhareji;
import book.course.molareza.ir.mp3player.db.LikeMusicKharejiDao;

public class ActivityPlayer extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, SeekBar.OnSeekBarChangeListener
        , MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    private Toolbar toolbar;

    private ImageView imgBlur, imgMain, imgRepeat, imgPrev, imgPlay, imgNext, imgDownload, imgLikeMusic;
    private TextView txtCurrentTime, txtTotalTime, txtSingerPlayer, txtLikeMusic;
    private SeekBar seekBarPlayer;

    private ViewGroup layoutLike;
    private ImageView imgFavorite;
    private boolean isFav = false;
    private boolean isLike = false;

    private String urlBigImage, urlThImage, name, album, urlMp3_64, urlMp3_128;

    public  MediaPlayer mediaPlayer;
    private boolean isRepeat = false;

    long totalTime, currentTime;

    public int po;
    private String id = "";
    //send Item

    private String num = "1";
    private String table = "";
    private String set = "visit";
    private int cLike = 0;


    @Override
    protected void onResume() {
        super.onResume();
        G.currentActivity = this;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        int result = G.audioManager.requestAudioFocus(ActivityPlayer.this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

//        G.favoriteMusicIraniDao.deleteAll();
//        G.favoriteMusicKharejiDao.deleteAll();
//        G.likeMusicIraniDao.deleteAll();
//        G.likeMusicKharejiDao.deleteAll();


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
        layoutLike = (ViewGroup) findViewById(R.id.layoutLikeMusic);
        imgLikeMusic = (ImageView) findViewById(R.id.imgLikeMusic);
        txtLikeMusic = (TextView) findViewById(R.id.txtLikeMusic);
        imgFavorite = (ImageView) findViewById(R.id.imgFavoriteMusic);
        imgDownload = (ImageView) findViewById(R.id.imgDownload);

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

            name = bundle.getString("NAME");
            album = bundle.getString("ALBUM");
            urlBigImage = bundle.getString("URL_BIG_IMAGE");
            urlThImage = bundle.getString("URL_TH_IMAGE");
            urlMp3_64 = bundle.getString("URL_MP3_64");
            urlMp3_128 = bundle.getString("URL_MP3_128");
            id = bundle.getString("ID");
            po = bundle.getInt("PO");
            table = bundle.getString("TABLE");
            cLike = bundle.getInt("LIKE");
        }


        clickLike();

        sendVisit();
        checkFavoriteAndLike();
        isState();

        txtSingerPlayer.setText(name + "  " + "آلبوم" + "  " + album);

        Picasso.with(G.context).load(urlBigImage).into(imgMain);

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


                    ////////////////////////////////////////
//
//                    String strtitle = "i dont now";
//                    // Set Notification Text
//                    String strtext = "its new for me";
//
//                    // Open NotificationView Class on Notification Click
//                    Intent intent = new Intent(ActivityPlayer.this, ActivityMain.class);
//                    // Send data to NotificationView Class
//                    intent.putExtra("title", strtitle);
//                    intent.putExtra("text", strtext);
//
//                    PendingIntent pIntent = PendingIntent.getActivity(ActivityPlayer.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(G.context)
//                            // Set Icon
//                            .setSmallIcon(R.mipmap.play)
//                            // Set Ticker Message
//                            .setTicker("Ticker")
//                            // Set Title
//                            .setContentTitle(name)
//                            // Set Text
//                            .setContentText(album)
//                            // Add an Action Button below Notification
//                            .addAction(R.mipmap.pause, "Action Button", pIntent)
//                            // Set PendingIntent into Notification
//                            .setContentIntent(pIntent)
//                            // Dismiss Notification
//                            .setAutoCancel(true);
//
//                    // Create Notification Manager
//                    NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    // Build Notification with Notification Manager
//                    notificationmanager.notify(0, builder.build());







//                 	// Using RemoteViews to bind custom layouts into Notification
//                    RemoteViews remoteViews = new RemoteViews(getPackageName(),
//                            R.layout.custom_notification);
////
////                    // Set Notification Title
//
//                    String strtitle = "i dont now";
//                    // Set Notification Text
//                    String strtext = "its new for me";
//
//                    // Open NotificationView Class on Notification Click
//                    Intent intent = new Intent(ActivityPlayer.this, ActivityMain.class);
//                    // Send data to NotificationView Class
//                    intent.putExtra("title", strtitle);
//                    intent.putExtra("text", strtext);
//                    // Open NotificationView.java Activity
//                    PendingIntent pIntent = PendingIntent.getActivity(G.context, 0, intent,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
////
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(G.context)
//                            // Set Icon
//                            .setSmallIcon(R.mipmap.down)
//                            // Set Ticker Message
//                            .setTicker("ticker")
//                            // Dismiss Notification
//                            .setAutoCancel(true)
//                            // Set PendingIntent into Notification
//                            .setContentIntent(pIntent)
//                            // Set RemoteViews into Notification
//                            .setContent(remoteViews);
//
//                    // Locate and set the Image into customnotificationtext.xml ImageViews
//               //     remoteViews.setImageViewResource(R.id.notification_button_play,R.mipmap.play);
//               //     remoteViews.setImageViewResource(R.id.notification_button_close,R.mipmap.close);
//
//                    // Locate and set the Text into customnotificationtext.xml TextViews
//                    remoteViews.setTextViewText(R.id.title,"setText1");
//                    remoteViews.setTextViewText(R.id.text,"setText2");
//
//                    // Create Notification Manager
//                    NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                    // Build Notification with Notification Manager
//                    notificationmanager.notify(0, builder.build());



                    //////////////////////////////////
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

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Play
            new startPlay().execute();
        }

        // favorite button

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (table.equals("irani")) {
                    if (isFav) {

                        G.favoriteMusicIraniDao.queryBuilder()
                                .where(FavoriteMusicIraniDao
                                        .Properties
                                        .Id_item.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();

                        imgFavorite.setImageResource(R.mipmap.ic_favorite_border_black_48dp);
                        imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_select));
                        isFav = false;
                        Toast.makeText(ActivityPlayer.this, "این مطلب از لیست علاقه مندی ها پاک شد", Toast.LENGTH_SHORT).show();

                    } else {


                        FavoriteMusicIrani favoriteMusicIrani = new FavoriteMusicIrani();
                        favoriteMusicIrani.setId_item(id);
                        favoriteMusicIrani.setName(name);
                        favoriteMusicIrani.setAlbum(album);
                        favoriteMusicIrani.setThImage(urlThImage);
                        favoriteMusicIrani.setBigImage(urlBigImage);
                        favoriteMusicIrani.setMp3_64(urlMp3_64);
                        favoriteMusicIrani.setMp3_128(urlMp3_128);
                        favoriteMusicIrani.setTable(table);
                        G.favoriteMusicIraniDao.insert(favoriteMusicIrani);

                        imgFavorite.setImageResource(R.mipmap.ic_favorite_black_48dp);
                        imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_title));
                        isFav = true;

                        Toast.makeText(ActivityPlayer.this, "این مطلب به علاقه مندی ها اضافه شد", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (isFav) {

                        G.favoriteMusicKharejiDao.queryBuilder()
                                .where(FavoriteMusicKharejiDao
                                        .Properties
                                        .Id_item.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();

                        imgFavorite.setImageResource(R.mipmap.ic_favorite_border_black_48dp);
                        imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_select));
                        isFav = false;
                    } else {

                        FavoriteMusicKhareji favoriteMusicKhareji = new FavoriteMusicKhareji();
                        favoriteMusicKhareji.setId_item(id);
                        favoriteMusicKhareji.setName(name);
                        favoriteMusicKhareji.setAlbum(album);
                        favoriteMusicKhareji.setThImage(urlThImage);
                        favoriteMusicKhareji.setBigImage(urlBigImage);
                        favoriteMusicKhareji.setMp3_64(urlMp3_64);
                        favoriteMusicKhareji.setMp3_128(urlMp3_128);
                        favoriteMusicKhareji.setTable(table);
                        G.favoriteMusicKharejiDao.insert(favoriteMusicKhareji);

                        imgFavorite.setImageResource(R.mipmap.ic_favorite_black_48dp);
                        imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_title));
                        isFav = true;

                    }
                }


            }
        });

        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (table.equals("irani")) {
                    if (isLike) {
                        G.likeMusicIraniDao.queryBuilder().
                                where(LikeMusicIraniDao.Properties.Item_id.eq(id))
                                .buildDelete()
                                .executeDeleteWithoutDetachingEntities();

                        num = "2";
                        set = "like1";
                        sendVisit();

                        cLike = cLike - 1;
                        clickLike();
                        imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_select));
                        txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_select));
                        isLike = false;

                    } else {

                        LikeMusicIrani like = new LikeMusicIrani();
                        like.setItem_id(id);
                        G.likeMusicIraniDao.insert(like);

                        num = "1";
                        set = "like1";

                        sendVisit();

                        cLike = cLike + 1;
                        clickLike();
                        imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_title));
                        txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_title));
                        isLike = true;
                    }
                } else {
                    if (isLike) {
                        G.likeMusicKharejiDao.queryBuilder().
                                where(LikeMusicKharejiDao.Properties.Item_id.eq(id))
                                .buildDelete()
                                .executeDeleteWithoutDetachingEntities();

                        num = "2";
                        set = "like1";
                        sendVisit();

                        cLike = cLike - 1;
                        clickLike();
                        imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_select));
                        txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_select));
                        isLike = false;

                    } else {

                        LikeMusicKhareji like = new LikeMusicKhareji();
                        like.setItem_id(id);
                        G.likeMusicKharejiDao.insert(like);

                        num = "1";
                        set = "like1";

                        sendVisit();

                        cLike = cLike + 1;
                        clickLike();
                        imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_title));
                        txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_title));
                        isLike = true;
                    }
                }

            }
        });

        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (table.equals("irani")) {

                    FileDownloader fileDownloader = new FileDownloader(ActivityPlayer.this);
                    fileDownloader.execute(urlMp3_64, G.DIR_IRANI);
                } else {
                    FileDownloader fileDownloader = new FileDownloader(ActivityPlayer.this);
                    fileDownloader.execute(urlMp3_64, G.DIR_KHAREJI);
                }

            }
        });
    }

    private void clickLike() {

        txtLikeMusic.setText("" + cLike);

    }


    private void isState() {

        if (table.equals("irani")) {

            if (isFav) {
                imgFavorite.setImageResource(R.mipmap.ic_favorite_black_48dp);
                imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_title));
            } else {
                imgFavorite.setImageResource(R.mipmap.ic_favorite_border_black_48dp);
                imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_select));
            }

            if (isLike) {

                imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_title));
                txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_title));

            } else {
                imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_select));
                txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_select));
            }

        } else {

            if (isFav) {
                imgFavorite.setImageResource(R.mipmap.ic_favorite_black_48dp);
                imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_title));
            } else {
                imgFavorite.setImageResource(R.mipmap.ic_favorite_border_black_48dp);
                imgFavorite.setColorFilter(getResources().getColor(R.color.tab_text_select));
            }

            if (isLike) {
                imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_title));
                txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_title));

            } else {
                imgLikeMusic.setColorFilter(getResources().getColor(R.color.tab_text_select));
                txtLikeMusic.setTextColor(getResources().getColor(R.color.tab_text_select));
            }
        }


    }

    private void checkFavoriteAndLike() {

        if (table.equals("irani")) {
            List<FavoriteMusicIrani> favoriteIranisList = G.favoriteMusicIraniDao.queryBuilder()
                    .where(FavoriteMusicIraniDao.Properties.Id_item.eq(id))
                    .list();

            for (FavoriteMusicIrani fav : favoriteIranisList) {

                if (fav.getId_item() != null) {

                    isFav = true;
                } else {
                    isFav = false;
                }
            }
            List<LikeMusicIrani> likeMusicIranis = G.likeMusicIraniDao.queryBuilder().where(LikeMusicIraniDao.Properties
                    .Item_id.eq(id))
                    .list();
            for (LikeMusicIrani lk : likeMusicIranis) {

                if (lk.getItem_id() != null) {
                    Log.i("LOGLOG", "out: " + lk.getItem_id());
                    isLike = true;

                } else {
                    isLike = false;
                }

            }

        } else {
            List<FavoriteMusicKhareji> favoriteMusicKharejis = G.favoriteMusicKharejiDao.queryBuilder()
                    .where(FavoriteMusicKharejiDao.Properties.Id_item.eq(id))
                    .list();

            for (FavoriteMusicKhareji fav : favoriteMusicKharejis) {

                if (fav.getId_item() != null) {

                    Log.i("LOGLOG", "out: " + fav.getId_item());
                    isFav = true;
                } else {
                    isFav = false;
                }

            }

            List<LikeMusicKhareji> likeMusicKharejis = G.likeMusicKharejiDao.queryBuilder().where(LikeMusicKharejiDao.Properties
                    .Item_id.eq(id))
                    .list();
            for (LikeMusicKhareji lk : likeMusicKharejis) {

                if (lk.getItem_id() != null) {

                    isLike = true;

                } else {
                    isLike = false;
                }

            }
        }
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


    private void sendVisit() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_VISIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LOGTAG", "onResponse: " + response);

                try {
                    JSONObject object = new JSONObject(response);

                    String state = object.getString("state");
                    int st = Integer.parseInt(state);

                    if (st == 200) {

                        String vs = object.getString("plus");
                        int vPlus = Integer.parseInt(vs);

                        if (set.equals("visit")) {

                            if (table.equals("irani")) {

                                AdapterMusicKhareji.items.get(po).setVisit(vPlus);

                            } else {

                                AdapterMusicIKhareji.items.get(po).setVisit(vPlus);

                            }

                            //   AdapterMusicKhareji ad = new AdapterMusicKhareji(null);
                            //  ad.items.get(po).setVisit(vPlus);

                        } else {

                            if (table.equals("irani")) {

                                AdapterMusicKhareji.items.get(po).setLike(vPlus);

                            } else {

                                AdapterMusicIKhareji.items.get(po).setVisit(vPlus);

                            }

                        }


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

                params.put("num", num);
                params.put("table", table);
                params.put("id", id);
                params.put("set", set);

                return params;

            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }


}
