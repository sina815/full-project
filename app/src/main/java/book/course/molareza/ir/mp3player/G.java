package book.course.molareza.ir.mp3player;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;

import java.io.File;

import book.course.molareza.ir.mp3player.db.DaoMaster;
import book.course.molareza.ir.mp3player.db.DaoSession;
import book.course.molareza.ir.mp3player.db.FavoriteClipDao;
import book.course.molareza.ir.mp3player.db.FavoriteDetailDao;
import book.course.molareza.ir.mp3player.db.FavoriteMusicIraniDao;
import book.course.molareza.ir.mp3player.db.FavoriteMusicKharejiDao;
import book.course.molareza.ir.mp3player.db.LikeClipDao;
import book.course.molareza.ir.mp3player.db.LikeDetailDao;
import book.course.molareza.ir.mp3player.db.LikeMusicIraniDao;
import book.course.molareza.ir.mp3player.db.LikeMusicKharejiDao;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class G extends Application {

    public static Context context;
    public static LayoutInflater inflater;
    public static final Handler HANDLER = new Handler();
    public static Activity currentActivity;
    public static NotificationManager notificationManager;

    public static AudioManager audioManager;

    public static final String URL_IRANI = "http://192.168.1.38/musicbazan/irani.php";
    public static final String URL_KHAREJI = "http://192.168.1.38/musicbazan/khareji.php";
    public static final String URL_Clip = "http://192.168.1.38/musicbazan/clip.php";
    public static final String URL_NEWS = "http://192.168.1.38/musicbazan/news.php";
    public static final String URL_SEARCH = "http://192.168.1.38/musicbazan/search.php";
    public static final String URL_CALL = "http://192.168.1.38/musicbazan/call.php";
    public static final String URL_DIALOG = "http://192.168.1.38/musicbazan/dialog.php";
    public static final String URL_VISIT = "http://192.168.1.38/musicbazan/visit.php";
    public static final String URL_VERSION = "http://192.168.1.38/musicbazan/version.php";

    public static final String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DIR_MUSIC = SD_CARD + "/musicBazan";
    public static final String DIR_CLIP = DIR_MUSIC + "/clip/";
    public static final String DIR_IRANI = DIR_MUSIC + "/irani/";
    public static final String DIR_KHAREJI = DIR_MUSIC + "/khareji/";

    public static FavoriteDetailDao favoriteDetailDao;
    public static FavoriteMusicIraniDao favoriteMusicIraniDao;
    public static FavoriteMusicKharejiDao favoriteMusicKharejiDao;
    public static FavoriteClipDao favoriteClipDao;
    public static LikeDetailDao likeDetailDao;
    public static LikeMusicIraniDao likeMusicIraniDao;
    public static LikeMusicKharejiDao likeMusicKharejiDao;
    public static LikeClipDao likeClipDao;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        File folderMisic = new File(DIR_MUSIC);
        File clipPath = new File(DIR_CLIP);
        File musicIrani = new File(DIR_IRANI);
        File musicKhareji = new File(DIR_KHAREJI);

        if (!folderMisic.exists()) {
            folderMisic.mkdirs();
        }
        if (!clipPath.exists()) {
            clipPath.mkdirs();
        }

        if (!musicIrani.exists()) {
            musicIrani.mkdirs();
        }
        if (!musicKhareji.exists()) {
            musicKhareji.mkdirs();
        }


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "favorite", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        favoriteDetailDao = daoSession.getFavoriteDetailDao();
        favoriteMusicIraniDao = daoSession.getFavoriteMusicIraniDao();
        favoriteMusicKharejiDao = daoSession.getFavoriteMusicKharejiDao();
        favoriteClipDao = daoSession.getFavoriteClipDao();
        likeDetailDao = daoSession.getLikeDetailDao();
        likeMusicIraniDao = daoSession.getLikeMusicIraniDao();
        likeMusicKharejiDao = daoSession.getLikeMusicKharejiDao();
        likeClipDao = daoSession.getLikeClipDao();


    }
}
