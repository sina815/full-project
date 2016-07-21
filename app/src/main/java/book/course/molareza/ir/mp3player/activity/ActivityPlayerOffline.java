package book.course.molareza.ir.mp3player.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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
import book.course.molareza.ir.mp3player.ServicePlayerOffline;

public class ActivityPlayerOffline extends AppCompatActivity implements
       AudioManager.OnAudioFocusChangeListener
        , SeekBar.OnSeekBarChangeListener
     {
         public static ImageView imgPlay;
         public static TextView txtCurrentTime, txtTotalTime;
         public static SeekBar seekBarPlayer;
         public static boolean isComplete = false;
         private static long totalTime, currentTime;
         private static boolean isRepeat = false;
         public int po;
         private Toolbar toolbar;
         private ImageView imgBlur, imgMain, imgRepeat, imgPrev, imgNext;
         private TextView txtSingerPlayer;
         private String image, mp3, name, album;
         private String id = "";
         //send Item

         private boolean isResume = false;

         public static void get_percent_seekBar(int cTime, int tTime) {

             currentTime = cTime;
             totalTime = tTime;

             update_seekBar_timer();

         }

         private static void update_seekBar_timer() {

             if (ServicePlayerOffline.mediaPlayer.isPlaying()) {

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
                 G.HANDLER.postDelayed(notif, 1);

             }
         }

         @Override
         protected void onResume() {
             super.onResume();

             G.currentActivity = this;
             G.notificationManager.cancel(0);

         }

         @Override
         protected void onDestroy() {
             super.onDestroy();

             if (ServicePlayerOffline.mediaPlayer !=null){

                 if (ServicePlayerOffline.mediaPlayer.isPlaying()) {

                     notification();
                 }
             }

         }

         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_player_offline);

             int result = G.audioManager.requestAudioFocus(ActivityPlayerOffline.this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

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

             if (seekBarPlayer != null) {

                 seekBarPlayer.setOnSeekBarChangeListener(this);
             }

             imgRepeat.setEnabled(false);
             imgPrev.setEnabled(false);
             imgPlay.setEnabled(false);
             imgNext.setEnabled(false);


             toolbar = (Toolbar) findViewById(R.id.toolbarPlay);
             setSupportActionBar(toolbar);

             if (getSupportActionBar() != null) {

                 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             }

             Bundle bundle = getIntent().getExtras();
             if (bundle != null) {

                 name = bundle.getString("NAME");
                 album = bundle.getString("ALBUM");
                 image = bundle.getString("IMAGE");
                 mp3 = bundle.getString("MP3");
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
                 imgPlay.setImageResource(R.mipmap.pause);
             }

             txtSingerPlayer.setText(name + "  " + "آلبوم" + "  " + album);

             Bitmap bitmap = BitmapFactory.decodeFile(image);
             imgMain.setImageBitmap(bitmap);

             Runnable run = new Runnable() {
                 @Override
                 public void run() {

                     imgMain.buildDrawingCache();
                     Bitmap mainBlur = imgMain.getDrawingCache();
                     imgBlur.setImageBitmap(Helper.blur(G.context, mainBlur));
                 }
             };
             G.HANDLER.postDelayed(run, 500);

             if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                 // Play

                 Intent intent = new Intent(this, ServicePlayerOffline.class);
                 if (ServicePlayerOffline.mediaPlayer != null && ServicePlayerOffline.mediaPlayer.isPlaying()) {
                     stopService(intent);
                     if (isRepeat) {
                         isRepeat = false;
                     }
                 }

                 intent.putExtra("MP3", mp3);
                 startService(intent);
                 Log.i("OFFLINE", "1: " + mp3 );
                 imgRepeat.setEnabled(true);
                 imgPrev.setEnabled(true);
                 imgPlay.setEnabled(true);
                 imgNext.setEnabled(true);
                 imgPlay.setImageResource(R.mipmap.pause);


             }

             imgPlay.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (ServicePlayerOffline.mediaPlayer.isPlaying()) {
                         ServicePlayerOffline.mediaPlayer.pause();
                         imgPlay.setImageResource(R.mipmap.play);
                         imgPrev.setEnabled(false);
                         imgNext.setEnabled(false);

                     } else {

                         if (isComplete) {

                             // txtCurrentTime.setText("00:00");
                             // TODO: 7/20/2016  3 second delay need for player can good work ( seekbar cant work )
                             ServicePlayerOffline.playMusic();

                             imgPlay.setImageResource(R.mipmap.pause);
                             imgPrev.setEnabled(true);
                             imgNext.setEnabled(true);
                             isComplete = false;

                             return;
                         }
                         ServicePlayerOffline.mediaPlayer.start();
                         imgPlay.setImageResource(R.mipmap.pause);
                         imgPrev.setEnabled(true);
                         imgNext.setEnabled(true);

                     }
                 }
             });

             imgNext.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     int totalTime = ServicePlayerOffline.mediaPlayer.getDuration();
                     int currentTime = ServicePlayerOffline.mediaPlayer.getCurrentPosition();

                     if (currentTime + 5000 >= totalTime) {

                         ServicePlayerOffline.mediaPlayer.seekTo(totalTime);

                     } else {
                         ServicePlayerOffline.mediaPlayer.seekTo(currentTime + 5000);
                     }

                 }
             });

             imgPrev.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int currentTime = ServicePlayerOffline.mediaPlayer.getCurrentPosition();

                     if (currentTime - 5000 >= 0) {

                         ServicePlayerOffline.mediaPlayer.seekTo(currentTime - 5000);

                     } else {

                         ServicePlayerOffline.mediaPlayer.seekTo(0);
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



         public void onBufferingUpdate(int percent) {
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

             int totalTime = ServicePlayerOffline.mediaPlayer.getDuration();
             int curentProgressSeekBar = Helper.progressToTimer(seekBar.getProgress(), totalTime);
             ServicePlayerOffline.mediaPlayer.seekTo(curentProgressSeekBar);

         }


         public boolean onCompletion() {


             if (!isRepeat) {
                 seekBarPlayer.setProgress(0);
                 txtCurrentTime.setText("00:00");
                 isComplete = true;

                 return false;

             } else {

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
                 //  ServicePlayerOffline.mediaPlayer.stop();
             }
         }


         //notify if media is play and page is finish
         public void notification() {

             Thread thread = new Thread(new Runnable() {
                 @Override
                 public void run() {

                     final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
                     Intent intent = new Intent(ActivityPlayerOffline.this, ActivityPlayerOffline.class);
                     intent.putExtra("NAME", name);
                     intent.putExtra("ALBUM", album);
                     intent.putExtra("IMAGE", image);
                     intent.putExtra("MP3", mp3);
                     intent.putExtra("RESUME", true);
                     final PendingIntent pIntent = PendingIntent.getActivity(G.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                     Intent buttonPlayIntent = new Intent(ActivityPlayerOffline.this, NotificationPlayButtonHandler.class);
                     PendingIntent buttonPlayPendingIntent = PendingIntent.getBroadcast(ActivityPlayerOffline.this, 0, buttonPlayIntent, 0);
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

                     G.notificationManager.notify(1, builder.build());
                 }
             });

             thread.start();


         }

         @Override
         public boolean onKeyDown(int keyCode, KeyEvent event) {

             if (keyCode == KeyEvent.KEYCODE_BACK) {

                 Intent intent = new Intent(ActivityPlayerOffline.this, ActivityDownload.class);
                 startActivity(intent);
                 finish();

             }

             return super.onKeyDown(keyCode, event);

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
                 ServicePlayerOffline.mediaPlayer.stop();
                 MyToast.makeText(context, "پخش موزیک متوقف شد", Toast.LENGTH_SHORT).show();

             }
         }
}
