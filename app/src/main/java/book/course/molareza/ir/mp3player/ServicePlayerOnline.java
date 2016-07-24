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

import book.course.molareza.ir.mp3player.activity.ActivityPlayerOnline;


public class ServicePlayerOnline extends Service
        implements
        MediaPlayer.OnBufferingUpdateListener
        , MediaPlayer.OnCompletionListener
        , MediaPlayer.OnPreparedListener
        , MediaPlayer.OnErrorListener {

    public static MediaPlayer mediaPlayer;
    public static int po;
    public static boolean isStopTime = false;
    private static int currentTime;
    private static int totalTime;
    private static String urlMp3_64;
    private ActivityPlayerOnline activityPlayerOnline;

    public static void update_seekBar_timer() {


        if (mediaPlayer.isPlaying()) {

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    totalTime = mediaPlayer.getDuration();
                    if (!isStopTime) {
                        currentTime = mediaPlayer.getCurrentPosition();
                        int percent = Helper.getProgressPercentage(currentTime, totalTime);
                        if (percent > 0) {

                            ActivityPlayerOnline.get_percent_seekBar(currentTime, totalTime);
                        } else if (percent >= 99) {
                            Log.i("PERCENT", "run: " + percent);
                            ActivityPlayerOnline.seekBarPlayer.setProgress(0);

                        }
                        update_seekBar_timer();
                    }
                }
            };

            G.HANDLER.postDelayed(run, 1);
        }

    }

    public void playMusic() {


        if (mediaPlayer != null) {

            isStopTime = false;
            ActivityPlayerOnline.isStopTime = false;
            ActivityPlayerOnline.seekBarPlayer.setProgress(0);
            mediaPlayer.start();

            if (mediaPlayer.isPlaying()) {

                update_seekBar_timer();

            } else {
                mediaPlayer.reset();
                startMusic();

            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        activityPlayerOnline = new ActivityPlayerOnline();

        try {
            if (intent.getExtras() != null) {

                Bundle bundle = intent.getExtras();
                urlMp3_64 = bundle.getString("URL_MP3_64");
            }


        } catch (NullPointerException e) {

            e.getStackTrace();

        }


        if (mediaPlayer != null) {
            mediaPlayer.release();
            //  mediaPlayer.stop();
        }

        mediaPlayer = new MediaPlayer();


        startMusic();

        ////////////////////////////////play media

        return super.onStartCommand(intent, flags, startId);
    }

    public void startMusic() {

        //    new DoPlayMusic().execute();

        if (urlMp3_64 != null) {

            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(urlMp3_64);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(this);

            } catch (IOException e) {
                e.printStackTrace();
                startMusic();
            }


        } else {
            Log.i("ERROR", "errrrrroooorrrr: ");
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        activityPlayerOnline.onBufferingUpdate(percent);
        Log.i("BUFFER", "onBufferingUpdate: " + percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        activityPlayerOnline = new ActivityPlayerOnline();
        boolean isRepeat = activityPlayerOnline.onCompletion();


        if (isRepeat) {
            isStopTime = false;
            ActivityPlayerOnline.isStopTime = false;
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            update_seekBar_timer();
        } else {
            mediaPlayer.pause();
            ActivityPlayerOnline.imgPlay.setImageResource(R.mipmap.play);
            ActivityPlayerOnline.imgPlay.setEnabled(false);
            isStopTime = true;
            ActivityPlayerOnline.isStopTime = true;

            Runnable run = new Runnable() {
                @Override
                public void run() {
                    ActivityPlayerOnline.imgPlay.setEnabled(true);
                }
            };

            G.HANDLER.postDelayed(run, 1);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {


            playMusic();
            mediaPlayer.setOnBufferingUpdateListener(ServicePlayerOnline.this);
            mediaPlayer.setOnCompletionListener(ServicePlayerOnline.this);


        } catch (Exception e) {
            startMusic();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        Log.i("ERROR1234", "onErrorrrrrrrrrrrrrrr: " + what +  extra);

        return false;


    }
}

