package book.course.molareza.ir.mp3player.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.MyToast;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterNews;
import book.course.molareza.ir.mp3player.database.DataBase;
import book.course.molareza.ir.mp3player.db.FavoriteDetail;
import book.course.molareza.ir.mp3player.db.FavoriteDetailDao;
import book.course.molareza.ir.mp3player.db.LikeDetail;
import book.course.molareza.ir.mp3player.db.LikeDetailDao;

public class ActivityDetailNews extends AppCompatActivity {

    private Toolbar toolbar;

    private String txtText, txtId, txtDesc, txtUrlBigImage, txtUrlThImage, txtTitle;

    private FloatingActionButton fab;
    private boolean isFav = false;
    private boolean isLike = false;
    private ViewGroup layoutLike;

    private ImageView imgDetail, imgLikeDetail;
    private TextView txtTextDetail, txtLikeDetail;
    private String id = "";
    private int cLike = 0;

    private CollapsingToolbarLayout collapse;
    private int po;

    //send Item

    private String num = "1";
    private String table = "news";
    private String set = "visit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        Log.i("LOGLOG", "f1: " + isFav);
//        G.favoriteDetailDao.deleteAll();

        DataBase dataBase = new DataBase();
        int screen = dataBase.readScreenSetting();
        if (screen == 1) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            txtId = bundle.getString("ID");
            txtTitle = bundle.getString("TITLE");
            txtDesc = bundle.getString("DESC");
            txtText = bundle.getString("TEXT");
            txtUrlBigImage = bundle.getString("BIGIMAGE");
            txtUrlThImage = bundle.getString("THIMAGE");
            id = bundle.getString("ID");
            po = bundle.getInt("PO");
            cLike = bundle.getInt("LIKE");
        }

        sendVisit();
        checkFavoriteAndLike();

        toolbar = (Toolbar) findViewById(R.id.toolbarNews);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() !=null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        assert collapse != null;
        collapse.setTitle(txtTitle);
        collapse.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapse.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        layoutLike = (ViewGroup) findViewById(R.id.layoutLike);
        imgLikeDetail = (ImageView) findViewById(R.id.imgLikeDetail);
        txtLikeDetail = (TextView) findViewById(R.id.txtLikeDetail);
        clickLike();


        isState();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFav) {

                    G.favoriteDetailDao.queryBuilder()
                            .where(FavoriteDetailDao.Properties.Id_item.eq(txtId))
                            .buildDelete()
                            .executeDeleteWithoutDetachingEntities();

                    fab.setColorFilter(getResources().getColor(R.color.tab_text_select));
                    fab.setImageResource(R.mipmap.ic_favorite_border_black_48dp);

                    isFav = false;

                    MyToast.makeText(ActivityDetailNews.this, "این مطلب از لیست علاقه مندی ها پاک شد", Toast.LENGTH_SHORT).show();
                } else {

                    FavoriteDetail favorite = new FavoriteDetail();
                    favorite.setId_item(txtId);
                    favorite.setName(txtTitle);
                    favorite.setAlbum(txtDesc);
                    favorite.setText(txtText);
                    favorite.setThImage(txtUrlThImage);
                    favorite.setBigImage(txtUrlBigImage);
                    G.favoriteDetailDao.insert(favorite);

                    fab.setImageResource(R.mipmap.ic_favorite_black_48dp);
                    fab.setColorFilter(getResources().getColor(R.color.tab_text_title));
                    isFav = true;

                    Log.i("LOGLOG", "f2: " + isFav);

                    MyToast.makeText(ActivityDetailNews.this, "این مطلب به علاقه مندی ها اضافه شد", Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLike) {

                    G.likeDetailDao.queryBuilder().
                            where(LikeDetailDao.Properties.Item_id.eq(txtId))
                            .buildDelete()
                            .executeDeleteWithoutDetachingEntities();

                    num = "2";
                    set = "like1";
                    sendVisit();

                    cLike = cLike - 1;
                    clickLike();
                    imgLikeDetail.setColorFilter(getResources().getColor(R.color.tab_text_select));
                    txtLikeDetail.setTextColor(getResources().getColor(R.color.tab_text_select));
                    isLike = false;

                } else {

                    LikeDetail like = new LikeDetail();
                    like.setItem_id(txtId);
                    G.likeDetailDao.insert(like);

                    num = "1";
                    set = "like1";

                    sendVisit();

                    cLike = cLike + 1;
                    clickLike();
                    imgLikeDetail.setColorFilter(getResources().getColor(R.color.tab_text_title));
                    txtLikeDetail.setTextColor(getResources().getColor(R.color.tab_text_title));
                    isLike = true;
                }
            }
        });

        imgDetail = (ImageView) findViewById(R.id.imgDetail);

        Picasso.with(G.context).load(txtUrlBigImage).into(imgDetail);

        txtTextDetail = (TextView) findViewById(R.id.txtTextDetail);
        if (txtText != null) {

            txtTextDetail.setText(Html.fromHtml(txtText));
        }


        int size = dataBase.fetchDatabase();
        txtTextDetail.setTextSize(size);

    }

    private void clickLike() {


        assert txtLikeDetail != null;
        txtLikeDetail.setText("" + cLike);

    }

    private void isState() {
        if (isFav) {
            fab.setColorFilter(getResources().getColor(R.color.tab_text_title));
            fab.setImageResource(R.mipmap.ic_favorite_black_48dp);
        } else {
            fab.setColorFilter(getResources().getColor(R.color.tab_text_select));
            fab.setImageResource(R.mipmap.ic_favorite_border_black_48dp);

        }

        if (isLike) {
            imgLikeDetail.setColorFilter(getResources().getColor(R.color.tab_text_title));
            txtLikeDetail.setTextColor(getResources().getColor(R.color.tab_text_title));

        } else {
            imgLikeDetail.setColorFilter(getResources().getColor(R.color.tab_text_select));
            txtLikeDetail.setTextColor(getResources().getColor(R.color.tab_text_select));

        }


    }

    private void checkFavoriteAndLike() {
        List<FavoriteDetail> favoritesList = G.favoriteDetailDao.queryBuilder()
                .where(FavoriteDetailDao.Properties.Id_item.eq(txtId))
                .list();

        for (FavoriteDetail fc : favoritesList) {

            if (fc.getId_item() != null) {
                Log.i("LOGLOG", "in: " + fc.getId_item());

                isFav = true;
            }
        }

        List<LikeDetail> likeList = G.likeDetailDao.queryBuilder().where(LikeDetailDao.Properties
                .Item_id.eq(txtId))
                .list();
        for (LikeDetail lk : likeList) {

            if (lk.getItem_id() != null) {

                isLike = true;

            }

        }
    }

    private void sendVisit() {

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
                            AdapterNews.items.get(po).setVisit(vPlus);
                        } else {

                            AdapterNews.items.get(po).setLike(vPlus);
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
                params.put("table", table);
                params.put("id", id);
                params.put("set", set);

                return params;

            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }
}
