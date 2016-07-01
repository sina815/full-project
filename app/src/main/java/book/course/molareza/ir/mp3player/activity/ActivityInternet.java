package book.course.molareza.ir.mp3player.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;

import book.course.molareza.ir.mp3player.R;

public class ActivityInternet extends ActivityMain {

    private ViewGroup layoutRefresh, layoutWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        layoutWifi = (ViewGroup) findViewById(R.id.layoutWify);
        layoutRefresh = (ViewGroup) findViewById(R.id.layoutRefresh);

        layoutWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

            }
        });

        layoutRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = cm.getActiveNetworkInfo();

                if (networkInfo != null) {

                    startActivity(new Intent(ActivityInternet.this, ActivityMain.class));

                }

            }
        });

    }
}
