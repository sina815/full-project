package book.course.molareza.ir.mp3player.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.ServicePlayerOffline;

public class ActivityClipOffline extends AppCompatActivity {


    private Toolbar toolbar;

    private VideoView videoView;
    private ProgressDialog pDialog;
    private String urlClip, name, album;
    private TextView txtClipOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_offline);

        G.notificationManager.cancelAll();

        if (ServicePlayerOffline.mediaPlayer != null && ServicePlayerOffline.mediaPlayer.isPlaying()) {
            Intent intent = new Intent(this , ServicePlayerOffline.class);
            stopService(intent);
            G.notificationManager.cancel(0);
            G.notificationManager.cancel(1);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            name = bundle.getString("NAME");
            album = bundle.getString("ALBUM");
            urlClip = bundle.getString("URL_CLIP");

        }

        toolbar = (Toolbar) findViewById(R.id.toolbarClipOffline);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtClipOffline = (TextView) findViewById(R.id.txtClipOffline);
        if (txtClipOffline != null) {
            txtClipOffline.setText(name);
        }


        videoView = (VideoView) findViewById(R.id.videoViewOffline);

        pDialog = new ProgressDialog(ActivityClipOffline.this);
// افزودن عنوان به پروسس دیالوگ
        pDialog.setTitle("پخش ویدیو از آدرس اینترنتی");
// افزودن پیام برای پروسس دیالوگ
        pDialog.setMessage("در حال بارگذاری ویدیو...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.setCancelable(true);
// نمایش پروسس دیالوگ
        pDialog.show();

        try {
// شروع کار مدیاکنترل
            MediaController mediacontroller = new MediaController(
                    ActivityClipOffline.this);
            mediacontroller.setAnchorView(videoView);
// دریافت فایل ویدیوی از آدرس اینترنتی
            Uri video = Uri.parse(urlClip);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // قطع کار پروسس دیالوگ و شروع نمایش ویدیو
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();
            }
        });

    }

}
