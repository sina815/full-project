package book.course.molareza.ir.mp3player.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterViewPager;
import book.course.molareza.ir.mp3player.fragment.FragNavEnd;
import book.course.molareza.ir.mp3player.fragment.FragTab1;
import book.course.molareza.ir.mp3player.fragment.FragTab2;
import book.course.molareza.ir.mp3player.fragment.FragTab3;
import book.course.molareza.ir.mp3player.fragment.FragTab4;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityMain extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private FragNavEnd fragNavEnd;

    private ImageView menu_right, menu_left, imgSearch;

    private LinearLayout layoutRoot;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private AdapterViewPager adapterViewPager;

    public int[] iconTabView = {
            R.mipmap.ic_clip,
            R.mipmap.ic_music,
            R.mipmap.ic_stars,
            R.mipmap.newspaper


    };


    @Override
    protected void onResume() {
        super.onResume();

        G.currentActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actyvity_main);


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

                                String title = getResources().getString(R.string.dialog_title_aboutMe);
                                String message = getResources().getString(R.string.dialog_message_aboutMe);
                                dialogAboutMe(title, message);
                            }

                            break;

                            case R.id.another_app: {

                                String title = getResources().getString(R.string.dialog_title_another_app);
                                String message = getResources().getString(R.string.dialog_message_another_app);
                                dialogAboutMe(title, message);
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


        drawerLayout = (DrawerLayout) findViewById(R.id.draw_nav);
        fragNavEnd = (FragNavEnd) getSupportFragmentManager().findFragmentById(R.id.frag_nav_end);

        menu_right = (ImageView) findViewById(R.id.toolbar_menu);
        fragNavEnd.setup(drawerLayout, toolbar, menu_right, layoutRoot);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupIconTabView();


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setupIconTabView() {


        tabLayout.getTabAt(0).setIcon(iconTabView[1]);
        tabLayout.getTabAt(1).setIcon(iconTabView[2]);
        tabLayout.getTabAt(2).setIcon(iconTabView[0]);
        tabLayout.getTabAt(3).setIcon(iconTabView[3]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_right, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.setting:

                Toast.makeText(ActivityMain.this, "setting", Toast.LENGTH_SHORT).show();


                break;
            case R.id.aboutMe:

                Toast.makeText(ActivityMain.this, "aboutMe", Toast.LENGTH_SHORT).show();

                break;
            case R.id.another_app:

                Toast.makeText(ActivityMain.this, "another_app", Toast.LENGTH_SHORT).show();

                break;
        }

        return true;
    }

    private void dialogAboutMe(String title, String message) {

        final Dialog dialog = new Dialog(ActivityMain.this);

        dialog.setContentView(R.layout.alert_dialog);

        TextView txtTitleDialog = (TextView) dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText(title);

        TextView txtMessageDialog = (TextView) dialog.findViewById(R.id.txtMessageDialog);
        txtMessageDialog.setText(message);

        Button btnCloseDialog = (Button) dialog.findViewById(R.id.btnCloseDialog);
        btnCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.show();
    }

}

