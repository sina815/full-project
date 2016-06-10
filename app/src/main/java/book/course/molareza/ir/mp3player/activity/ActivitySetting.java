package book.course.molareza.ir.mp3player.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.database.DataBase;


public class ActivitySetting extends AppCompatActivity {

    private CheckBox chkScreen;
    private SeekBar seekBar;
    private TextView txtSample;
    private ImageView imgSave;
    private DataBase dataBase;

    private int screen;
    private int size;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_seting);
        dataBase = new DataBase();
        size = dataBase.fetchDatabase();

        screen = dataBase.readScreenSetting();

        chkScreen = (CheckBox) findViewById(R.id.chkScreen);
        if (screen == 1) {
            chkScreen.setChecked(true);
        } else {
            chkScreen.setChecked(false);
        }

        chkScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkScreen.isChecked()) {

                    screen = 1;

                } else {
                    screen = 0;
                }
            }
        });
        txtSample = (TextView) findViewById(R.id.txtSample);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        assert seekBar != null;
        seekBar.setMax(14);
        seekBar.setProgress(size - 10);

        txtSample.setTextSize(size);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                txtSample.setTextSize(progress + 10);
                size = progress + 10;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imgSave = (ImageView) findViewById(R.id.imgSave);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBase.updateFontSize(size);
                dataBase.updateScreen(screen);

                Toast.makeText(ActivitySetting.this, "تغییرات با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
