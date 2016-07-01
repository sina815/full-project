package book.course.molareza.ir.mp3player.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.MyToast;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.SharePref;
import book.course.molareza.ir.mp3player.adapter.AdapterViewPager;
import book.course.molareza.ir.mp3player.database.DataBase;
import book.course.molareza.ir.mp3player.favorite.ActivityFavorite;
import book.course.molareza.ir.mp3player.fragment.FragNavEnd;
import book.course.molareza.ir.mp3player.fragment.FragTab1;
import book.course.molareza.ir.mp3player.fragment.FragTab2;
import book.course.molareza.ir.mp3player.fragment.FragTab3;
import book.course.molareza.ir.mp3player.fragment.FragTab4;
import book.course.molareza.ir.mp3player.otherApp.ActivityOtherApp;
import book.course.molareza.ir.mp3player.search.ActivitySearch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityMain extends AppCompatActivity {

    private static final int TIME_INTERVAL = 3000; // # milliseconds, زمان مورد نیاز  برای دو پرس دکمه بازگشت.
    private long mBackPressed;

    public int[] iconTabView = {
            R.mipmap.ic_music,
            R.mipmap.ic_stars,
            R.mipmap.ic_clip,
            R.mipmap.newspaper


    };
    private SharedPreferences sharedPreferences;
    private String id, newVersion, desc, link;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FragNavEnd fragNavEnd;
    private ImageView menu_right, menu_left, imgSearch, imgFavorite;
    private CoordinatorLayout layoutRoot;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterViewPager adapterViewPager;
    private String dialogId;
    private String titleDialog;
    private String messageDialog;
    private String linkDialog;


    /////////////// check newVersion

    @Override
    protected void onResume() {
        super.onResume();

        G.currentActivity = this;
    }


    ///////////////////////// check newVersion my app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkNewVersion();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        menu_left = (ImageView) findViewById(R.id.toolbar_menu_start);
        menu_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ActivityMain.this, v);
                popup.getMenuInflater().inflate(R.menu.menu_right, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.setting:

                                Intent intent = new Intent(G.currentActivity, ActivitySetting.class);
                                startActivity(intent);

                                break;

                            case R.id.aboutMe: {

                                dialogId = "1";
                                getDataDialog();
                            }

                            break;

                            case R.id.another_app: {

                                Intent intent1 = new Intent(ActivityMain.this, ActivityOtherApp.class);
                                startActivity(intent1);
                            }

                            break;
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });


        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivitySearch.class);
                startActivity(intent);
            }
        });

        imgFavorite = (ImageView) findViewById(R.id.imgFavorite);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityFavorite.class);
                startActivity(intent);
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.draw_nav);
        fragNavEnd = (FragNavEnd) getSupportFragmentManager().findFragmentById(R.id.frag_nav_end);

        layoutRoot = (CoordinatorLayout) findViewById(R.id.layoutRoot);

        menu_right = (ImageView) findViewById(R.id.toolbar_menu);
        fragNavEnd.setup(drawerLayout, toolbar, menu_right, layoutRoot);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupIconTabView();

        DataBase dataBase = new DataBase();

    }

    private void checkNewVersion() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, G.URL_VERSION, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("version");

                    if (array != null) {

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);

                            id = object.getString("id");
                            newVersion = object.getString("version");
                            desc = object.getString("desc");
                            link = object.getString("link");

                            sharedPreferences = getSharedPreferences(SharePref.FILE_NAME, MODE_PRIVATE);
                            float oldVersion = sharedPreferences.getFloat(SharePref.VERSION, 1.0f);

                            if (oldVersion < Float.parseFloat(newVersion)) {
                                checkVersion();
                            }

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
        });

        Volley.newRequestQueue(G.context).add(objectRequest);
    }

    private void checkVersion() {
        ///// check newVersion my app

        PackageManager manager = G.context.getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(G.context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        assert info != null;
        String versionApp = info.versionName;

        float myAppVersion = Float.parseFloat(versionApp);
        float myNewVersion = Float.parseFloat(newVersion);

        sharedPreferences = getSharedPreferences(SharePref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(SharePref.VERSION, myNewVersion);
        editor.apply();


        if (myAppVersion < myNewVersion) {

            shoDialogUpdate();

        }

        ///////////////////////////

    }

    private void shoDialogUpdate() {

        final Dialog dialog = new Dialog(ActivityMain.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.alert_dialog);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitleDialog);
        txtTitle.setText("بروز رسانی");

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessageDialog);
        txtMessage.setText(desc);

        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgCloseDialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.show();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setupIconTabView() {


        for (int i = 0; i < 4; i++) {
            tabLayout.getTabAt(i).setIcon(iconTabView[i]);

            tabLayout.getTabAt(i).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
        }

        int position = tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_select), PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                tab.getIcon().setColorFilter(getResources().getColor(R.color.tab_text_select), PorterDuff.Mode.SRC_IN);
                int position = tab.getPosition();
                viewPager.setCurrentItem(position);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                tab.getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {

        adapterViewPager = new AdapterViewPager(getSupportFragmentManager());

        adapterViewPager.addToList(new FragTab1(), getResources().getString(R.string.iran_music));
        adapterViewPager.addToList(new FragTab2(), getResources().getString(R.string.abroad_music));
        adapterViewPager.addToList(new FragTab3(), getResources().getString(R.string.clip_music));
        adapterViewPager.addToList(new FragTab4(), getResources().getString(R.string.news_music));
        viewPager.setAdapter(adapterViewPager);
        adapterViewPager.notifyDataSetChanged();

    }

    private void showDialog() {

        final Dialog dialog = new Dialog(ActivityMain.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.alert_dialog);

        ImageView imgCloseDialog = (ImageView) dialog.findViewById(R.id.imgCloseDialog);
        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        TextView txtTitleDialog = (TextView) dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText(titleDialog);

        TextView txtMessageDialog = (TextView) dialog.findViewById(R.id.txtMessageDialog);
        txtMessageDialog.setText(messageDialog);

        Button btnSite = (Button) dialog.findViewById(R.id.btnDialog);

        btnSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(linkDialog));
                startActivity(i);

            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //animation for dialog

        dialog.show();


    }

    private void getDataDialog() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_DIALOG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject JObject = new JSONObject(response);
                    JSONArray array = JObject.getJSONArray("dialog");

                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject object = array.getJSONObject(i);
                            titleDialog = object.getString("title");
                            messageDialog = object.getString("message");
                            linkDialog = object.getString("link");
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", dialogId);

                return params;
            }
        };
        Volley.newRequestQueue(G.context).add(stringRequest);
    }

    //متد دکمه بازگشت
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }
//نمایش پیغام هنگام پرس دکمه بازگشت
        else {
            MyToast.makeText(getBaseContext(), "برای خروج از برنامه بروی دکمه خروج  دوباره کنید!", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}

