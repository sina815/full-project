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

import com.onesignal.OneSignal;

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
import book.course.molareza.ir.mp3player.db.ListDownloadDao;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@ReportsCrashes(formUri = "http://mbaas.ir/api/acra/18c27e38")
public class G extends Application {

    public static final Handler HANDLER = new Handler();
    public static final String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DIR_MUSIC = SD_CARD + "/musicBazan";
    public static final String DIR_CLIP = DIR_MUSIC + "/clip/";
    public static final String DIR_IRANI = DIR_MUSIC + "/irani/";
    public static final String DIR_KHAREJI = DIR_MUSIC + "/khareji/";
    public static final String DIR_APP = DIR_MUSIC + "/app/";
    public static final String DIR_PIC = DIR_MUSIC + "/pic/";

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
    public static ListDownloadDao listDownload;


    private static String url = "http://musicbaz.molareza.ir";
//    private static String url = "http://192.168.1.38";
    public static final String URL_IRANI = url + "/musicbazan/irani.php";
    public static final String URL_KHAREJI = url + "/musicbazan/khareji.php";
    public static final String URL_Clip = url + "/musicbazan/clip.php";
    public static final String URL_NEWS = url + "/musicbazan/news.php";
    public static final String URL_SEARCH = url + "/musicbazan/search.php";
    public static final String URL_CALL = url + "/musicbazan/call.php";
    public static final String URL_DIALOG = url + "/musicbazan/dialog.php";
    public static final String URL_VISIT = url + "/musicbazan/visit.php";
    public static final String URL_VERSION = url + "/musicbazan/version.php";
    public static final String URL_OTHER_APP = url + "/musicbazan/otherapp.php";
    public static final String URL_INFO = url + "/musicbazan/info.php";

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

        OneSignal.startInit(this).init();

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
        File pic = new File(DIR_PIC);

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

        if (!pic.exists()) {
            pic.mkdirs();
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
        listDownload = daoSession.getListDownloadDao();


    }
}
