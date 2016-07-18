package book.course.molareza.ir.mp3player.activity;

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.db.ListDownload;


public class ImageDownloader extends AsyncTask<String, Void, String> {

    public String pathFile;
    public String name;
    public String album;
    public String pathImahe;
    public String type;

    public ImageDownloader(String pathFile, String name, String album , String type) {
        this.pathFile = pathFile;
        this.name = name;
        this.album = album;
        this.type = type;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;

        String[] nameUrl = params[0].split("/");
        String nameFile = nameUrl[nameUrl.length - 1];

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            inputStream = connection.getInputStream();

            outputStream = new FileOutputStream(G.DIR_PIC + nameFile);

            pathImahe = G.DIR_PIC + nameFile;

            byte[] buffer = new byte[1024];

            int len;

            while ((len = inputStream.read(buffer)) != -1) {

                outputStream.write(buffer, 0, len);

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {

                connection.disconnect();
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (s == null) {

            ListDownload listDownload = new ListDownload();
            listDownload.setName(name);
            listDownload.setAlbum(album);
            listDownload.setImage(pathImahe);
            listDownload.setMp3(pathFile);
            listDownload.setType(type);
            G.listDownload.insert(listDownload);

        }
    }
}
