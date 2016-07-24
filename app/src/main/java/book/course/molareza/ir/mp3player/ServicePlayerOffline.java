package book.course.molareza.ir.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import book.course.molareza.ir.mp3player.activity.ActivityPlayerOffline;


public class ServicePlayerOffline extends Service
        implements
        MediaPlayer.OnBufferingUpdateListener
        , MediaPlayer.OnCompletionListener {

    public static MediaPlayer mediaPlayer;
    public static int po;
    private static int currentTime;
    private static int totalTime;
    private static String urlMp3_64;

    private ActivityPlayerOffline activityPlayerOffline;

    public static void update_seekBar_timer() {


        totalTime = mediaPlayer.getDuration();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                currentTime = mediaPlayer.getCurrentPosition();
                int percent = Helper.getProgressPercentage(currentTime, totalTime);
                ActivityPlayerOffline.get_percent_seekBar(currentTime, totalTime);

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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("OFFLINE", "2: " );
        activityPlayerOffline = new ActivityPlayerOffline();
        mediaPlayer = new MediaPlayer();

        if (intent.getExtras() != null) {

            Bundle bundle = intent.getExtras();
            urlMp3_64 = bundle.getString("MP3");
        }

        Log.i("OFFLINE", "3: " + urlMp3_64);
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(urlMp3_64);
            mediaPlayer.prepare();
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

        activityPlayerOffline.onBufferingUpdate(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        activityPlayerOffline = new ActivityPlayerOffline();

        boolean isRepeat = activityPlayerOffline.onCompletion();


        if (isRepeat) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            update_seekBar_timer();
        } else {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            ActivityPlayerOffline.seekBarPlayer.setProgress(0);
            ActivityPlayerOffline.imgPlay.setImageResource(R.mipmap.play);
            ActivityPlayerOffline.imgPlay.setEnabled(false);

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    ActivityPlayerOffline.imgPlay.setEnabled(true);
                }
            };

            G.HANDLER.postDelayed(run , 4000);

        }
    }
}

