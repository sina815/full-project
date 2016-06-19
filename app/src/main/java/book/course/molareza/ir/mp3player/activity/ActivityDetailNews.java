package book.course.molareza.ir.mp3player.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
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
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterNews;
import book.course.molareza.ir.mp3player.database.DataBase;
import book.course.molareza.ir.mp3player.struct.StructNews;

public class ActivityDetailNews extends AppCompatActivity {

    private Toolbar toolbar;

    private String txtText, txtUrlImage, txtTitle;

    private FloatingActionButton fab;
    private boolean fav = false;

    private ImageView imgDetail;
    private TextView txtTextDetail;
    private String id = "";

    private CollapsingToolbarLayout collapse;
    private int po;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        sendVisit();

        DataBase dataBase = new DataBase();
        int screen = dataBase.readScreenSetting();
        if (screen == 1) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            txtTitle = bundle.getString("TITLE");
            txtText = bundle.getString("TEXT");
            txtUrlImage = bundle.getString("BIGIMAGE");
            id = bundle.getString("ID");
            po = bundle.getInt("PO");

        }


        toolbar = (Toolbar) findViewById(R.id.toolbarNews);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        assert collapse != null;
        collapse.setTitle(txtTitle);
        collapse.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapse.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fav){
                    fab.setColorFilter(getResources().getColor(R.color.tab_text_select));
                    fav = false;

                }else {

                    fab.setColorFilter(getResources().getColor(R.color.tab_text_title));
                    fav = true;
                }


            }
        });

        imgDetail = (ImageView) findViewById(R.id.imgDetail);

        Picasso.with(G.context).load(txtUrlImage).into(imgDetail);

        txtTextDetail = (TextView) findViewById(R.id.txtTextDetail);
        txtTextDetail.setText(Html.fromHtml(txtText));


        int size = dataBase.fetchDatabase();
        txtTextDetail.setTextSize(size);

    }

    private void sendVisit() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_VISIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    String state = object.getString("state");
                    int st = Integer.parseInt(state);

                    //  Log.i("LOGTAG", "onResponse: " + state);
                    if (st == 200) {

                        StructNews item = new StructNews();

                        String vs = object.getString("visit");
                        int vPlus = Integer.parseInt(vs);

                        AdapterNews.items.get(po).setVisit(vPlus);

                        Toast.makeText(ActivityDetailNews.this, "" + po, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ActivityDetailNews.this, "" + vPlus, Toast.LENGTH_SHORT).show();

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

                params.put("visit", "1");
                params.put("table", "news");
                params.put("id", id);

                return params;

            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }
}
