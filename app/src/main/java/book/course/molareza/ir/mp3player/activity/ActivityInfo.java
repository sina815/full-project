package book.course.molareza.ir.mp3player.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.MyToast;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.SharePref;
import book.course.molareza.ir.mp3player.adapter.AdapterInfo;
import book.course.molareza.ir.mp3player.struct.StructOtherApp;

public class ActivityInfo extends AppCompatActivity {

    private ProgressBar prgBar;

    private Toolbar toolbar;

    private RecyclerView rcvContent;
    private AdapterInfo adapterOtherApp;
    private List<StructOtherApp> items = new ArrayList<>();

    private boolean isPage = true;
    private int u = 0;

    private String count;
    private int intCount;

    private ViewGroup vgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        vgInfo = (ViewGroup) findViewById(R.id.vgInfo);
        if (vgInfo != null) {
            vgInfo.setVisibility(View.INVISIBLE);
        }

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            count = bundle.getString("COUNT");

        }

        if (count != null) {
            intCount = Integer.parseInt(count);
        } else {
            intCount = ActivityMain.intCount;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(SharePref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SharePref.COUNT_INFO, intCount);
        editor.apply();

        prgBar = (ProgressBar) findViewById(R.id.prgInfo);

        toolbar = (Toolbar) findViewById(R.id.toolbarOtherApp);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rcvContent = (RecyclerView) findViewById(R.id.rcvContentInfo);
        adapterOtherApp = new AdapterInfo(items);
        rcvContent.setAdapter(adapterOtherApp);
        rcvContent.setLayoutManager(new LinearLayoutManager(G.context));
        setItems();

    }

    private void setItems() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, G.URL_INFO, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("app");

                    if (array != null) {

                        for (int i = 0; i < array.length(); i++) {

                            StructOtherApp item = new StructOtherApp();
                            JSONObject object = array.getJSONObject(i);

                            item.setId(object.getString("id"));
                            item.setDesc(object.getString("desc"));
                            item.setText(object.getString("text"));
                            item.setThImage(object.getString("thumbnail"));
                            item.setBigImage(object.getString("bigImage"));
                            item.setLink(object.getString("link"));
                            item.setVisit(object.getInt("visit"));
                            item.setLike(object.getInt("like"));

                            int id = Integer.parseInt(object.getString("id"));
                            if (id <= 1) {
                                isPage = false;
                            }

                            String urlImage = object.getString("thumbnail");
                            imageDownloader(urlImage, u);
                            u++;

                            items.add(item);

                        }

                    }


                    adapterOtherApp.notifyDataSetChanged();
                    prgBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                prgBar.setVisibility(View.INVISIBLE);
                vgInfo.setVisibility(View.VISIBLE);
                vgInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItems();
                        vgInfo.setVisibility(View.GONE);
                        prgBar.setVisibility(View.VISIBLE);
                    }
                });

            }
        });

        Volley.newRequestQueue(G.context).add(objectRequest);
    }

    private void imageDownloader(String urlImage, final int position) {

        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                adapterOtherApp.items.get(position).setBitmapImage(response);
                adapterOtherApp.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyToast.makeText(G.context, getResources().getString(R.string.error_down_image), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ActivityInfo.this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
