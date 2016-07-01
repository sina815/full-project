package book.course.molareza.ir.mp3player.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;

public class Splash extends AppCompatActivity {

    private boolean connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalesh);

        G.HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isConnect()) {

                    Intent intent = new Intent(Splash.this, ActivityMain.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Splash.this, ActivityInternet.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 1000);

    }

    private boolean isConnect() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;

        } else {
            return true;
        }

    }


}
