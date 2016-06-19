package book.course.molareza.ir.mp3player;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.view.LayoutInflater;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class G extends Application {

    public static Context context;
    public static LayoutInflater inflater;
    public static final Handler HANDLER = new Handler();
    public static Activity currentActivity;

    public static AudioManager audioManager;

    public static final String URL_IRANI = "http://192.168.1.39/musicbazan/irani.php";
    public static final String URL_KHAREJI = "http://192.168.1.39/musicbazan/khareji.php";
    public static final String URL_Clip = "http://192.168.1.39/musicbazan/clip.php";
    public static final String URL_NEWS = "http://192.168.1.39/musicbazan/news.php";
    public static final String URL_SEARCH = "http://192.168.1.39/musicbazan/search.php";
    public static final String URL_CALL = "http://192.168.1.39/musicbazan/call.php";
    public static final String URL_DIALOG = "http://192.168.1.39/musicbazan/dialog.php";
    public static final String URL_VISIT = "http://192.168.1.39/musicbazan/visit.php";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );



    }
}
