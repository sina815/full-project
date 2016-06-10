package book.course.molareza.ir.mp3player.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import book.course.molareza.ir.mp3player.G;


public class DataBase extends SQLiteOpenHelper {

    public DataBase() {
        super(G.context, DbHelper.data_file_name, null, DbHelper.database_version);

        isDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void isDatabase() {

        File check = new File(DbHelper.data_package);

        if (!check.exists()) {

            boolean mkdirs = check.mkdirs();

        }

        check = G.context.getDatabasePath(DbHelper.data_file_name);

        if (!check.exists()) {

            try {
                copy();
                Toast.makeText(G.context, "" + check, Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void copy() throws IOException {

        InputStream inputStream = G.context.getAssets().open("database/tb_setting.sqlite");
        String filePath = DbHelper.data_package + DbHelper.data_file_name;
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

        byte[] buffer = new byte[1024];
        int lengh;

        while ((lengh = inputStream.read(buffer)) > 0) {

            fileOutputStream.write(buffer, 0, lengh);

        }
        inputStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();

    }


    public int fetchDatabase() {

        int value = 0;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT " + DbHelper.data_fontsize + " FROM " + DbHelper.database_table_name + "";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            do {

                value = cursor.getInt(cursor.getColumnIndex(DbHelper.data_fontsize));

            } while (cursor.moveToNext());

        }
        cursor.close();
        sqLiteDatabase.close();

        return value;
    }

    public void updateFontSize(int size) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "UPDATE " + DbHelper.database_table_name + " SET " + DbHelper.data_fontsize + "=" + size + "";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();

    }

    public int readScreenSetting() {

        int value = 0;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT " + DbHelper.data_screen + " FROM " + DbHelper.database_table_name + "";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            do {

                value = cursor.getInt(cursor.getColumnIndex(DbHelper.data_screen));

            } while (cursor.moveToNext());
        }

        sqLiteDatabase.close();
        cursor.close();

        return value;
    }

    public void updateScreen(int screen) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "UPDATE " + DbHelper.database_table_name + " SET " + DbHelper.data_screen + "=" + screen + "";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();

    }

}
