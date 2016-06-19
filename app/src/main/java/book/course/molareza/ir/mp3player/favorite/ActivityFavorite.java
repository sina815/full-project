package book.course.molareza.ir.mp3player.favorite;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterViewPager;

public class ActivityFavorite extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPager upViewPager;
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
        setContentView(R.layout.activity_favorite);

        toolbar = (Toolbar) findViewById(R.id.toolbarFavorite);
        assert toolbar != null;
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPagerFavorite);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutFavorite);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
        setupTab();


    }

    private void setupTab() {

        for (int i = 0; i < 4; i++) {

            tabLayout.getTabAt(i).setIcon(iconTabView[i]);
            tabLayout.getTabAt(i).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
        }

        int po = tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(po).getIcon().setColorFilter(getResources().getColor(R.color.tab_text_select), PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(getResources().getColor(R.color.tab_text_select), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                tab.getIcon().setColorFilter(getResources().getColor(R.color.tab_text_title), PorterDuff.Mode.SRC_IN);
//
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void setupViewPager(ViewPager viewPager) {

        adapterViewPager = new AdapterViewPager(getSupportFragmentManager());
        adapterViewPager.addToList(new FragTabFavorite1(), getResources().getString(R.string.iran_music));
        adapterViewPager.addToList(new FragTabFavorite2(), getResources().getString(R.string.abroad_music));
        adapterViewPager.addToList(new FragTabFavorite3(), getResources().getString(R.string.clip_music));
        adapterViewPager.addToList(new FragTabFavorite4(), getResources().getString(R.string.news_music));

        viewPager.setAdapter(adapterViewPager);
        adapterViewPager.notifyDataSetChanged();

    }
}
