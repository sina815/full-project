package book.course.molareza.ir.mp3player.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.MyToast;

public class FileDownloader extends AsyncTask<String, Integer, String> {

    final FileDownloader me = this;
    public boolean downloadStatus = true;

    public ProgressDialog mProgress;
    private Context context;
    private boolean isCheck;

    private String urlDownload;

    public FileDownloader(Context cont) {

        this.context = cont;

        mProgress = new ProgressDialog(context);
        mProgress.setMessage("فایل در حال دانلود می باشد");
        mProgress.setIndeterminate(true);
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setCancelable(true);
        mProgress.setButton(DialogInterface.BUTTON_NEGATIVE,
                "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        me.cancel(true);  //<<<<<
                        downloadStatus = false;
                        dialog.cancel();
                    }
                });
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgress.show();

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        mProgress.setIndeterminate(false);
        mProgress.setMax(100);
        mProgress.setProgress(values[0]);
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpURLConnection connection = null;

        String[] nameUrl = params[0].split("/");
        String nameFile = nameUrl[nameUrl.length - 1];

        File check = new File(params[1] + nameFile);

        if (!check.exists()) {

            isCheck = true;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                int fileSize = connection.getContentLength();

                inputStream = connection.getInputStream();

                outputStream = new FileOutputStream(params[1] + nameFile);

                urlDownload = params[1] + nameFile;

                byte[] buffer = new byte[4096];

                long percent = 0;
                int len;

                while ((len = inputStream.read(buffer)) != -1) {

                    if (!me.isCancelled() && downloadStatus == true && mProgress.isShowing()) {

                        percent += len;

                        if (fileSize > 0) {

                            publishProgress((int) (percent * 100 / fileSize));
                        }
                        outputStream.write(buffer, 0, len);

                    } else {
                        if (!mProgress.isShowing()) {
                            me.cancel(true);
                        }
                    }
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (outputStream != null) {
                        outputStream.flush();
                        outputStream.close();
                    }
                    if (inputStream != null) {

                        inputStream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (connection != null) {

                    connection.disconnect();
                }
            }

        } else {
            isCheck = false;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (isCheck) {
            mProgress.dismiss();

            if (s != null) {

                MyToast.makeText(G.context, "خطا در حین دانلود", Toast.LENGTH_SHORT).show();
            } else {
                MyToast.makeText(G.context, "دانلود با موفقیت صورت گرفت", Toast.LENGTH_SHORT).show();

            }
        } else {
            mProgress.dismiss();
            MyToast.makeText(G.context, "این فایل قبلا دانلود شده", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (urlDownload != null) {


            File dl = new File(urlDownload);
            if (dl.exists()) {
                dl.delete();
                MyToast.makeText(G.context, "دانلود لغو شد", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


