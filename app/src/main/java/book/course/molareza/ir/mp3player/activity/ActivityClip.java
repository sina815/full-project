package book.course.molareza.ir.mp3player.activity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.MyToast;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterClip;
import book.course.molareza.ir.mp3player.db.FavoriteClip;
import book.course.molareza.ir.mp3player.db.FavoriteClipDao;
import book.course.molareza.ir.mp3player.db.LikeClip;
import book.course.molareza.ir.mp3player.db.LikeClipDao;

public class ActivityClip extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView imgLikeClip, imgFavClip, imgDownloadClip;
    private TextView txtLikeClip;
    private ViewGroup layoutLikeClip;

    private VideoView videoView;
    private ProgressDialog pDialog;
    private String urlClip, like, name, album, th_image, big_image;
    private String id;
    private int cLike, po;

    private boolean isFav = false;
    private boolean isLike = false;

    //send to server

    private String num = "1";
    private String set = "visit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip);

        if ( ActivityPlayer.mediaPlayer != null){

            if (ActivityPlayer.mediaPlayer.isPlaying()) {
                ActivityPlayer.mediaPlayer.stop();
            }
        }


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            name = bundle.getString("NAME");
            album = bundle.getString("ALBUM");
            th_image = bundle.getString("TH_IMAGE");
            big_image = bundle.getString("BIG_IMAGE");
            urlClip = bundle.getString("URL_CLIP");
            like = bundle.getString("LIKE");
            id = bundle.getString("ID");
            po = bundle.getInt("PO");

        }

        if (like != null) {

            cLike = Integer.parseInt(like);
        }

        imgDownloadClip = (ImageView) findViewById(R.id.imgDownloadClip);
        layoutLikeClip = (ViewGroup) findViewById(R.id.layoutLikeClip);
        imgFavClip = (ImageView) findViewById(R.id.imgFavClip);
        txtLikeClip = (TextView) findViewById(R.id.txtLikeClip);
        imgLikeClip = (ImageView) findViewById(R.id.imgLikeClip);

        sendData();
        countLike();
        checkLikeAndFav();
        isState();


        toolbar = (Toolbar) findViewById(R.id.toolbarClip);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        imgFavClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFav) {

                    G.favoriteClipDao.queryBuilder()
                            .where(FavoriteClipDao.Properties.Id_item.eq(id))
                            .buildDelete()
                            .executeDeleteWithoutDetachingEntities();

                    imgFavClip.setImageResource(R.mipmap.ic_favorite_border_black_48dp);
                    imgFavClip.setColorFilter(getResources().getColor(R.color.tab_text_select));

                    isFav = false;

                    MyToast.makeText(ActivityClip.this, "این مطلب از لیست علاقه مندی ها پاک شد", Toast.LENGTH_SHORT).show();

                } else {

                    FavoriteClip favoriteClip = new FavoriteClip();
                    favoriteClip.setId_item(id);
                    favoriteClip.setName(name);
                    favoriteClip.setAlbum(album);
                    favoriteClip.setThImage(th_image);
                    favoriteClip.setBigImage(big_image);
                    favoriteClip.setClip(urlClip);
                    G.favoriteClipDao.insert(favoriteClip);

                    imgFavClip.setImageResource(R.mipmap.ic_favorite_black_48dp);
                    imgFavClip.setColorFilter(getResources().getColor(R.color.tab_text_title));

                    isFav = true;

                    MyToast.makeText(ActivityClip.this, "این مطلب به علاقه مندی ها اضافه شد", Toast.LENGTH_SHORT).show();
                }

            }
        });

        layoutLikeClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isLike) {

                    G.likeClipDao.queryBuilder()
                            .where(LikeClipDao.Properties.Item_id.eq(id))
                            .buildDelete()
                            .executeDeleteWithoutDetachingEntities();


                    num = "2";
                    set = "like1";

                    cLike = cLike - 1;
                    countLike();

                    sendData();
                    txtLikeClip.setTextColor(getResources().getColor(R.color.tab_text_select));
                    imgLikeClip.setColorFilter(getResources().getColor(R.color.tab_text_select));

                    isLike = false;

                } else {

                    LikeClip likeClip = new LikeClip();
                    likeClip.setItem_id(id);
                    G.likeClipDao.insert(likeClip);

                    num = "1";
                    set = "like1";

                    cLike = cLike + 1;
                    countLike();

                    sendData();

                    txtLikeClip.setTextColor(getResources().getColor(R.color.tab_text_title));
                    imgLikeClip.setColorFilter(getResources().getColor(R.color.tab_text_title));

                    isLike = true;
                }

            }
        });


        videoView = (VideoView) findViewById(R.id.videoView);

        pDialog = new ProgressDialog(ActivityClip.this);
// افزودن عنوان به پروسس دیالوگ
        pDialog.setTitle("پخش ویدیو از آدرس اینترنتی");
// افزودن پیام برای پروسس دیالوگ
        pDialog.setMessage("در حال بارگذاری ویدیو...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
// نمایش پروسس دیالوگ
        pDialog.show();

        try {
// شروع کار مدیاکنترل
            MediaController mediacontroller = new MediaController(
                    ActivityClip.this);
            mediacontroller.setAnchorView(videoView);
// دریافت فایل ویدیوی از آدرس اینترنتی
            Uri video = Uri.parse(urlClip);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // قطع کار پروسس دیالوگ و شروع نمایش ویدیو
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();
            }
        });


        imgDownloadClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileDownloader fileDownloader = new FileDownloader(ActivityClip.this);
                fileDownloader.execute(urlClip, G.DIR_CLIP);
            }
        });

    }


    private void countLike() {

        txtLikeClip.setText("" + cLike);

    }

    private void checkLikeAndFav() {

        List<FavoriteClip> favoriteClips = G.favoriteClipDao
                .queryBuilder()
                .where(FavoriteClipDao.Properties.Id_item.eq(id))
                .list();

        for (FavoriteClip fav : favoriteClips) {

            if (fav.getId_item() != null) {

                isFav = true;

            } else {
                isFav = false;
            }

        }

        List<LikeClip> likeClips = G.likeClipDao
                .queryBuilder()
                .where(LikeClipDao.Properties.Item_id.eq(id))
                .list();

        for (LikeClip lk : likeClips) {

            if (lk.getItem_id() != null) {

                Log.i("TAGIDQAZ", "checkLikeAndFav: " + lk.getItem_id());

                isLike = true;
            } else {
                isLike = false;
            }

        }

    }

    private void isState() {
        if (isFav) {

            imgFavClip.setImageResource(R.mipmap.ic_favorite_black_48dp);
            imgFavClip.setColorFilter(getResources().getColor(R.color.tab_text_title));

        } else {

            imgFavClip.setImageResource(R.mipmap.ic_favorite_border_black_48dp);
            imgFavClip.setColorFilter(getResources().getColor(R.color.tab_text_select));
        }

        if (isLike) {

            txtLikeClip.setTextColor(getResources().getColor(R.color.tab_text_title));
            imgLikeClip.setColorFilter(getResources().getColor(R.color.tab_text_title));

        } else {

            txtLikeClip.setTextColor(getResources().getColor(R.color.tab_text_select));
            imgLikeClip.setColorFilter(getResources().getColor(R.color.tab_text_select));
        }
    }

    private void sendData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_VISIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    String state = object.getString("state");
                    int st = Integer.parseInt(state);

                    Log.i("LOGTAG", "onResponse: " + response);
                    if (st == 200) {

                        String vs = object.getString("plus");
                        int vPlus = Integer.parseInt(vs);

                        if (set.equals("visit")) {

                            AdapterClip.items.get(po).setVisit(vs);
                        } else {

                            AdapterClip.items.get(po).setLike(vs);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("num", num);
                params.put("table", "clip");
                params.put("id", id);
                params.put("set", set);

                return params;
            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }


}
