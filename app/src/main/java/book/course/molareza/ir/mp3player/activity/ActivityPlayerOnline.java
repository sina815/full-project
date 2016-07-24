package book.course.molareza.ir.mp3player.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.Helper;
import book.course.molareza.ir.mp3player.MyToast;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.ServicePlayerOnline;
import book.course.molareza.ir.mp3player.adapter.AdapterMusicIKhareji;
import book.course.molareza.ir.mp3player.adapter.AdapterMusicIrani;
import book.course.molareza.ir.mp3player.db.FavoriteMusicIrani;
import book.course.molareza.ir.mp3player.db.FavoriteMusicIraniDao;
import book.course.molareza.ir.mp3player.db.FavoriteMusicKhareji;
import book.course.molareza.ir.mp3player.db.FavoriteMusicKharejiDao;
import book.course.molareza.ir.mp3player.db.LikeMusicIrani;
import book.course.molareza.ir.mp3player.db.LikeMusicIraniDao;
import book.course.molareza.ir.mp3player.db.LikeMusicKhareji;
import book.course.molareza.ir.mp3player.db.LikeMusicKharejiDao;

public class ActivityPlayerOnline extends AppCompatActivity
        implements
        SeekBar.OnSeekBarChangeListener
        , AudioManager.OnAudioFocusChangeListener {

    public static ImageView imgPlay;
    public static TextView txtCurrentTime, txtTotalTime;
    public static SeekBar seekBarPlayer;
    public static boolean isComplete = false;
    public static boolean isStopTime = false;
    private static long totalTime, currentTime;
    private static boolean isRepeat = false;
    public int po;
    private Toolbar toolbar;
    private ImageView imgBlur, imgMain, imgRepeat, imgPrev, imgNext, imgDownload, imgLikeMusic;
    private TextView txtSingerPlayer, txtLikeMusic;
    private ViewGroup layoutLike;
    private ImageView imgFavorite;
    private boolean isFav = false;
    private boolean isLike = false;
    private String urlBigImage, urlThImage, name, album, urlMp3_64, urlMp3_128;
    //send Item
    private String id = "";
    private String num = "1";
    private String table = "";
    private String set = "visit";
    private int cLike = 0;
    private boolean isResume = false;

    public static void get_percent_seekBar(int cTime, int tTime) {

        currentTime = cTime;
        totalTime = tTime;

        if (!isStopTime ) {
            update_seekBar_timer();
            //     Log.i("URL1234", "currentTime222: " + isStopTime);
        }

    }

    private static void update_seekBar_timer() {

        if (ServicePlayerOnline.mediaPlayer !=null) {
            if (ServicePlayerOnline.mediaPlayer.isPlaying()) {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        if (txtTotalTime != null && txtCurrentTime != null) {

                            txtTotalTime.setText("" + Helper.milliSecondsToTimer(totalTime));
                            txtCurrentTime.setText("" + Helper.milliSecondsToTimer(currentTime));
                        }
                        int progress = Helper.getProgressPercentage(currentTime, totalTime);
                        if (seekBarPlayer != null && currentTime > 0) {

                            seekBarPlayer.setProgress(progress);
                        }
                        update_seekBar_timer();
                    }
                };

                G.HANDLER.postDelayed(run, 1);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        G.currentActivity = this;
        G.notificationManager.cancel(0);
        G.notificationManager.cancelAll();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ServicePlayerOnline.mediaPlayer.isPlaying()) {

            notification();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        G.notificationManager.cancelAll();
        int result = G.audioManager.requestAudioFocus(ActivityPlayerOnline.this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

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
            isResume = bundle.getBoolean("RESUME");
        }

        if (!isResume) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            result = 0;
            imgRepeat.setEnabled(true);
            imgPrev.setEnabled(true);
            imgPlay.setEnabled(true);
            imgNext.setEnabled(true);
            imgDownload.setEnabled(true);
            imgPlay.setImageResource(R.mipmap.pause);
        }

        clickLike();

        sendVisit();
        checkFavoriteAndLike();
        isState();

        txtSingerPlayer.setText(name + "  " + "آلبوم" + "  " + album);


        Picasso.with(G.context).load(urlBigImage).into(imgMain);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Play

            final Intent intent = new Intent(this, ServicePlayerOnline.class);
            if (ServicePlayerOnline.mediaPlayer != null && ServicePlayerOnline.mediaPlayer.isPlaying()) {
                stopService(intent);
                if (isRepeat) {
                    isRepeat = false;
                }
            }

            intent.putExtra("URL_MP3_64", urlMp3_64);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    startService(intent);
                }
            });

            thread.start();

            imgRepeat.setEnabled(true);
            imgPrev.setEnabled(true);
            imgPlay.setEnabled(true);
            imgNext.setEnabled(true);
            imgDownload.setEnabled(true);
            imgPlay.setImageResource(R.mipmap.pause);

        }

        Runnable runBlur = new Runnable() {
            @Override
            public void run() {
                try {
                    imgMain.buildDrawingCache();
                    Bitmap mainBlur = imgMain.getDrawingCache();

                    imgBlur.setImageBitmap(Helper.blur(G.context, mainBlur));

                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        };
        G.HANDLER.postDelayed(runBlur, 4000);

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServicePlayerOnline.isStopTime = false;
                isStopTime = false;

                if (ServicePlayerOnline.mediaPlayer.isPlaying()) {
                    ServicePlayerOnline.mediaPlayer.pause();
                    imgPlay.setImageResource(R.mipmap.play);
                    imgPrev.setEnabled(false);
                    imgNext.setEnabled(false);

                } else {

                    if (isComplete) {

                        // txtCurrentTime.setText("00:00");
                        // TODO: 7/20/2016  3 second delay need for player can good work ( seekbar cant work )
                        //       ServicePlayerOnline.playMusic();

                        imgPlay.setImageResource(R.mipmap.pause);
                        imgPrev.setEnabled(true);
                        imgNext.setEnabled(true);
                        isComplete = false;

                        return;
                    }
                    ServicePlayerOnline.mediaPlayer.start();
                    imgPlay.setImageResource(R.mipmap.pause);
                    imgPrev.setEnabled(true);
                    imgNext.setEnabled(true);
                    ServicePlayerOnline.update_seekBar_timer();

                }
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int totalTime = ServicePlayerOnline.mediaPlayer.getDuration();
                int currentTime = ServicePlayerOnline.mediaPlayer.getCurrentPosition();

                if (currentTime + 5000 >= totalTime) {

                    ServicePlayerOnline.mediaPlayer.seekTo(totalTime);

                } else {
                    ServicePlayerOnline.mediaPlayer.seekTo(currentTime + 5000);
                }

            }
        });

        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTime = ServicePlayerOnline.mediaPlayer.getCurrentPosition();

                if (currentTime - 5000 >= 0) {

                    ServicePlayerOnline.mediaPlayer.seekTo(currentTime - 5000);

                } else {

                    ServicePlayerOnline.mediaPlayer.seekTo(0);
                }
            }
        });

        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRepeat) {

                    isRepeat = true;
                    imgRepeat.setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
                    MyToast.makeText(ActivityPlayerOnline.this, "حالت تکرار فعال شد", Toast.LENGTH_SHORT).show();

                } else {

                    isRepeat = false;
                    imgRepeat.setColorFilter(getResources().getColor(R.color.tab_text_select), PorterDuff.Mode.SRC_IN);
                    MyToast.makeText(ActivityPlayerOnline.this, "حالت تکرار غیرفعال شد", Toast.LENGTH_SHORT).show();
                }

            }
        });


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
                        MyToast.makeText(ActivityPlayerOnline.this, "این مطلب از لیست علاقه مندی ها پاک شد", Toast.LENGTH_SHORT).show();

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

                        MyToast.makeText(ActivityPlayerOnline.this, "این مطلب به علاقه مندی ها اضافه شد", Toast.LENGTH_SHORT).show();
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

                    FileDownloader fileDownloader = new FileDownloader(ActivityPlayerOnline.this, name, album, urlBigImage, table);
                    fileDownloader.execute(urlMp3_64, G.DIR_IRANI);
                } else {
                    FileDownloader fileDownloader = new FileDownloader(ActivityPlayerOnline.this, name, album, urlBigImage, table);
                    fileDownloader.execute(urlMp3_64, G.DIR_KHAREJI);
                }

            }
        });

        if (seekBarPlayer != null) {

            seekBarPlayer.setOnSeekBarChangeListener(this);
        }

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


    //seekBar change listener

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


    public void onBufferingUpdate(int percent) {
        if (seekBarPlayer != null) {

            seekBarPlayer.setSecondaryProgress(percent);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        int totalTime = ServicePlayerOnline.mediaPlayer.getDuration();
        int curentProgressSeekBar = Helper.progressToTimer(seekBar.getProgress(), totalTime);
        ServicePlayerOnline.mediaPlayer.seekTo(curentProgressSeekBar);

    }


    public boolean onCompletion() {


        if (!isRepeat) {
            seekBarPlayer.setProgress(0);
            txtCurrentTime.setText("00:00");


            return false;

        } else {
            isComplete = true;
            seekBarPlayer.setProgress(0);
            txtCurrentTime.setText("00:00");
            return true;
        }

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause
            //  mediaPlayer.pause();
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            // Stop or pause depending on your need
            //  ServicePlayerOnline.mediaPlayer.stop();
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

                        String vs = object.getString("plus");
                        int vPlus = Integer.parseInt(vs);

                        if (set.equals("visit")) {

                            if (table.equals("irani")) {

                                AdapterMusicIrani.items.get(po).setVisit(vPlus);

                            } else {

                                AdapterMusicIKhareji.items.get(po).setVisit(vPlus);

                            }

                        } else {

                            if (table.equals("irani")) {

                                AdapterMusicIrani.items.get(po).setLike(vPlus);

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

    //notify if media is play and page is finish
    public void notification() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
                Intent intent = new Intent(ActivityPlayerOnline.this, ActivityPlayerOnline.class);
                intent.putExtra("NAME", name);
                intent.putExtra("ALBUM", album);
                intent.putExtra("URL_BIG_IMAGE", urlBigImage);
                intent.putExtra("URL_TH_IMAGE", urlThImage);
                intent.putExtra("URL_MP3_64", urlMp3_64);
                intent.putExtra("URL_MP3_128", urlMp3_128);
                intent.putExtra("ID", id);
                intent.putExtra("po", po);
                intent.putExtra("TABLE", table);
                intent.putExtra("LICK", cLike);
                intent.putExtra("RESUME", true);
                final PendingIntent pIntent = PendingIntent.getActivity(ActivityPlayerOnline.this, 0, intent, 0);

                Intent buttonPlayIntent = new Intent(ActivityPlayerOnline.this, NotificationPlayButtonHandler.class);
                PendingIntent buttonPlayPendingIntent = PendingIntent.getBroadcast(ActivityPlayerOnline.this, 0, buttonPlayIntent, 0);
                remoteViews.setOnClickPendingIntent(R.id.imgPauseNotify, buttonPlayPendingIntent);

                final NotificationCompat.Builder builder = new NotificationCompat.Builder(G.context)

                        .setSmallIcon(R.mipmap.play)
                        .setTicker(name)
                        .setAutoCancel(true)
                        .setContent(remoteViews)
                        .setContentIntent(pIntent);


                imgMain.buildDrawingCache();
                Bitmap bt = imgMain.getDrawingCache();

                remoteViews.setTextViewText(R.id.txtSingerNotify, name);
                remoteViews.setTextViewText(R.id.txtAlbumNotify, album);
                remoteViews.setImageViewBitmap(R.id.imgNotify, bt);

                G.notificationManager.notify(0, builder.build());
            }
        });

        thread.start();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(ActivityPlayerOnline.this, ActivityMain.class);
            startActivity(intent);
            finish();

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when user clicks the "play/pause" button on the on-going system Notification.
     */
    public static class NotificationPlayButtonHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//
            G.currentActivity.finish();
            Intent intent1 = new Intent(G.currentActivity, ActivityMain.class);
            G.currentActivity.startActivity(intent1);
            G.notificationManager.cancel(0);
            ServicePlayerOnline.mediaPlayer.stop();
            MyToast.makeText(context, "پخش موزیک متوقف شد", Toast.LENGTH_SHORT).show();

        }
    }
}



