package book.course.molareza.ir.mp3player.otherApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import book.course.molareza.ir.mp3player.activity.ActivityMain;
import book.course.molareza.ir.mp3player.struct.StructOtherApp;

public class ActivityOtherApp extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rcvContent;
    private ProgressBar prgBar;
    private AdapterOtherApp adapterOtherApp;
    private List<StructOtherApp> items = new ArrayList<>();

    private boolean isPage = true;
    private int u;

    private ViewGroup vgRefreshAgainOtherApp;

    @Override
    protected void onResume() {
        super.onResume();
        adapterOtherApp.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_app);

        vgRefreshAgainOtherApp = (ViewGroup) findViewById(R.id.vgRefreshAgainOtherApp);
        if (vgRefreshAgainOtherApp != null) {
            vgRefreshAgainOtherApp.setVisibility(View.INVISIBLE);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbarOtherApp);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prgBar = (ProgressBar) findViewById(R.id.prgOtherApp);
        assert prgBar != null;
        prgBar.setVisibility(View.VISIBLE);

        rcvContent = (RecyclerView) findViewById(R.id.rcvContentOtherApp);
        adapterOtherApp = new AdapterOtherApp(items);
        rcvContent.setAdapter(adapterOtherApp);
        rcvContent.setLayoutManager(new LinearLayoutManager(G.context));
        setItems();
        adapterOtherApp.notifyDataSetChanged();

    }

    private void setItems() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, G.URL_OTHER_APP, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray array = response.getJSONArray("app");

                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {

                            StructOtherApp item = new StructOtherApp();
                            JSONObject object = array.getJSONObject(i);

                            Log.i("TAGAPP", "onResponse: " + array);
                            item.setId(object.getString("id"));
                            item.setDesc(object.getString("desc"));
                            item.setText(object.getString("text"));
                            item.setThImage(object.getString("thumbnail"));
                            item.setBigImage(object.getString("bigImage"));
                            item.setLink(object.getString("link"));
                            item.setVisit(object.getInt("visit"));
                            item.setLike(object.getInt("like"));
                            item.setTable(object.getString("tbName"));

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapterOtherApp.notifyDataSetChanged();
                prgBar.setVisibility(View.INVISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyToast.makeText(G.context, "متاسفانه ارتباط با سرور برقرار نشد", Toast.LENGTH_SHORT).show();
                prgBar.setVisibility(View.INVISIBLE);
                vgRefreshAgainOtherApp.setVisibility(View.VISIBLE);
                vgRefreshAgainOtherApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItems();
                        vgRefreshAgainOtherApp.setVisibility(View.INVISIBLE);
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
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
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
            Intent intent = new Intent(ActivityOtherApp.this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
