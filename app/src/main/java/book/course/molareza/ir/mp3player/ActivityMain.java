package book.course.molareza.ir.mp3player;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityMain extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private FragNavEnd fragNavEnd;

    private ImageView menu_right, menu_left;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actyvity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

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
        adapterViewPager.addToList(new FragTab3(), getResources().getString(R.string.news_music));

        viewPager.setAdapter(adapterViewPager);
        adapterViewPager.notifyDataSetChanged();


    }
}
