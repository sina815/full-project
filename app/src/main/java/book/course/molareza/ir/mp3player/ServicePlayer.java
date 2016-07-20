package book.course.molareza.ir.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

import book.course.molareza.ir.mp3player.activity.ActivityPlayerOnline;


public class ServicePlayer extends Service
        implements
        MediaPlayer.OnBufferingUpdateListener
        , MediaPlayer.OnCompletionListener {

    public static MediaPlayer mediaPlayer;
    public static int po;
    private static int currentTime;
    private static int totalTime;
    private static String urlMp3_64;

    private ActivityPlayerOnline activityPlayerOnline;

    public static void update_seekBar_timer() {


        totalTime = mediaPlayer.getDuration();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                currentTime = mediaPlayer.getCurrentPosition();
                int percent = Helper.getProgressPercentage(currentTime, totalTime);
                ActivityPlayerOnline.get_percent_seekBar(currentTime, totalTime);

                if (percent == 100) {
                    return;
                }

                update_seekBar_timer();
            }
        };

        G.HANDLER.postDelayed(run, 1);

    }

    public static void playMusic() {


        mediaPlayer.start();


        if (mediaPlayer.isPlaying()) {
            update_seekBar_timer();
        }


    }

//    private static void sendMessageToActivity(String msg) {
//        Intent intent = new Intent("GPSLocationUpdates");
//        // You can also include some extra data.
//        intent.putExtra("Status", msg);
//        LocalBroadcastManager.getInstance(G.context).sendBroadcast(intent);
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //   sendMessageToActivity("amir");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        activityPlayerOnline = new ActivityPlayerOnline();
        mediaPlayer = new MediaPlayer();

        if (intent.getExtras() != null) {

            Bundle bundle = intent.getExtras();
            urlMp3_64 = bundle.getString("URL_MP3_64");
        }
        try {
            mediaPlayer.setDataSource(urlMp3_64);
            mediaPlayer.prepare();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playMusic();

        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        stopSelf();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        activityPlayerOnline.onBufferingUpdate(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        activityPlayerOnline = new ActivityPlayerOnline();

        boolean isRepeat = activityPlayerOnline.onCompletion();


        if (isRepeat) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            update_seekBar_timer();
        } else {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            ActivityPlayerOnline.seekBarPlayer.setProgress(0);
            ActivityPlayerOnline.imgPlay.setImageResource(R.mipmap.play);
            ActivityPlayerOnline.imgPlay.setEnabled(false);

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    ActivityPlayerOnline.imgPlay.setEnabled(true);
                }
            };

            G.HANDLER.postDelayed(run , 4000);

        }
    }
}

