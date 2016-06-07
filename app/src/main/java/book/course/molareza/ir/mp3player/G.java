package book.course.molareza.ir.mp3player;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class G extends Application {

    public static Context context;
    public static LayoutInflater inflater;
    public static final Handler HANDLER = new Handler();
    public static Activity currentActivity;

    public static final String URL_IRANI = "http://192.168.1.38/musicbazan/irani.php";
    public static final String URL_KHAREJI = "http://192.168.1.38/musicbazan/khareji.php";
    public static final String URL_Clip = "http://192.168.1.38/musicbazan/clip.php";
    public static final String URL_NEWS = "http://192.168.1.38/musicbazan/news.php";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }
}
