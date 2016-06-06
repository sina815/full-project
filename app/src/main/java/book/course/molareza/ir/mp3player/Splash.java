package book.course.molareza.ir.mp3player;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalesh);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        G.HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        }, 1500);

    }
}
