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

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

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
import book.course.molareza.ir.mp3player.db.LikeOtherAppDao;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
@ReportsCrashes(formUri = "http://mbaas.ir/api/acra/18c27e38")
public class G extends Application {

    public static final Handler HANDLER = new Handler();
    public static final String URL_IRANI = "http://192.168.1.39/musicbazan/irani.php";
    public static final String URL_KHAREJI = "http://192.168.1.39/musicbazan/khareji.php";
    public static final String URL_Clip = "http://192.168.1.39/musicbazan/clip.php";
    public static final String URL_NEWS = "http://192.168.1.39/musicbazan/news.php";
    public static final String URL_SEARCH = "http://192.168.1.39/musicbazan/search.php";
    public static final String URL_CALL = "http://192.168.1.39/musicbazan/call.php";
    public static final String URL_DIALOG = "http://192.168.1.39/musicbazan/dialog.php";
    public static final String URL_VISIT = "http://192.168.1.39/musicbazan/visit.php";
    public static final String URL_VERSION = "http://192.168.1.39/musicbazan/version.php";
    public static final String URL_OTHER_APP = "http://192.168.1.39/musicbazan/otherapp.php";

    public static final String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DIR_MUSIC = SD_CARD + "/musicBazan";
    public static final String DIR_CLIP = DIR_MUSIC + "/clip/";
    public static final String DIR_IRANI = DIR_MUSIC + "/irani/";
    public static final String DIR_KHAREJI = DIR_MUSIC + "/khareji/";
    public static final String DIR_APP = DIR_MUSIC + "/app/";

    public static Context context;
    public static LayoutInflater inflater;
    public static Activity currentActivity;
    public static NotificationManager notificationManager;
    public static AudioManager audioManager;
    public static FavoriteDetailDao favoriteDetailDao;
    public static FavoriteMusicIraniDao favoriteMusicIraniDao;
    public static FavoriteMusicKharejiDao favoriteMusicKharejiDao;
    public static FavoriteClipDao favoriteClipDao;
    public static LikeDetailDao likeDetailDao;
    public static LikeMusicIraniDao likeMusicIraniDao;
    public static LikeMusicKharejiDao likeMusicKharejiDao;
    public static LikeClipDao likeClipDao;
    public static LikeOtherAppDao likeOtherAppDao;

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

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
        File app = new File(DIR_APP);

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
        if (!app.exists()) {
            app.mkdirs();
        }


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "favorite.sqlite", null);
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
        likeOtherAppDao = daoSession.getLikeOtherAppDao();


    }
}
