package book.course.molareza.ir.mp3player;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class ServicePlayerOnline extends Service
//        implements
//        MediaPlayer.OnBufferingUpdateListener
//        , MediaPlayer.OnCompletionListener {
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public static MediaPlayer mediaPlayer;
//    public static int po;
//    private static int currentTime;
//    private static int totalTime;
//    private static String urlMp3_64;
//
//    private ActivityPlayerOnline activityPlayerOnline;
//
//    public static void update_seekBar_timer() {
//
//
//        totalTime = mediaPlayer.getDuration();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                currentTime = mediaPlayer.getCurrentPosition();
//                int percent = Helper.getProgressPercentage(currentTime, totalTime);
//                ActivityPlayerOnline.get_percent_seekBar(currentTime, totalTime);
//
//                if (percent == 100) {
//                    return;
//                }
//
//                update_seekBar_timer();
//            }
//        };
//
//        G.HANDLER.postDelayed(run, 1000);
//
//    }
//
//    public static void playMusic() {
//
//        if (mediaPlayer != null) {
//
//            try {
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            mediaPlayer.start();
//
//
//            if (mediaPlayer.isPlaying()) {
//                update_seekBar_timer();
//            }
//
//        }
//    }
//
////    private static void sendMessageToActivity(String msg) {
////        Intent intent = new Intent("GPSLocationUpdates");
////        // You can also include some extra data.
////        intent.putExtra("Status", msg);
////        LocalBroadcastManager.getInstance(G.context).sendBroadcast(intent);
////    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        //   sendMessageToActivity("amir");
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        activityPlayerOnline = new ActivityPlayerOnline();
//
//
//        try {
//
//            if (intent.getExtras() != null) {
//
//                Bundle bundle = intent.getExtras();
//                urlMp3_64 = bundle.getString("URL_MP3_64");
//            }
//
//        } catch (NullPointerException e) {
//
//            e.getStackTrace();
//
//        }
//
//
//        new DoPlayMusic().execute();
//
//        //  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        mediaPlayer.stop();
//        stopSelf();
//    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//
//        activityPlayerOnline.onBufferingUpdate(percent);
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//
//        activityPlayerOnline = new ActivityPlayerOnline();
//
//        boolean isRepeat = activityPlayerOnline.onCompletion();
//
//
//        if (isRepeat) {
//            mediaPlayer.setLooping(true);
//            mediaPlayer.start();
//            update_seekBar_timer();
//        } else {
//            mediaPlayer.pause();
//            mediaPlayer.seekTo(0);
//            ActivityPlayerOnline.seekBarPlayer.setProgress(0);
//            ActivityPlayerOnline.imgPlay.setImageResource(R.mipmap.play);
//            ActivityPlayerOnline.imgPlay.setEnabled(false);
//
//            Runnable run = new Runnable() {
//                @Override
//                public void run() {
//                    ActivityPlayerOnline.imgPlay.setEnabled(true);
//                }
//            };
//
//            G.HANDLER.postDelayed(run, 4000);
//
//        }
//    }
//
//    public class DoPlayMusic extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            mediaPlayer = new MediaPlayer();
//            if (urlMp3_64 != null) {
//                try {
//
//                    mediaPlayer.setDataSource(urlMp3_64);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            playMusic();
//            mediaPlayer.setOnBufferingUpdateListener(ServicePlayerOnline.this);
//            mediaPlayer.setOnCompletionListener(ServicePlayerOnline.this);
//        }
//    }

}

