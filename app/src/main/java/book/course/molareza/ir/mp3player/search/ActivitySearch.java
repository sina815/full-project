package book.course.molareza.ir.mp3player.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.activity.ActivityMain;
import book.course.molareza.ir.mp3player.adapter.AdapterViewPagerSearch;
import book.course.molareza.ir.mp3player.favorite.ActivityFavorite;

public class ActivitySearch extends AppCompatActivity {

    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterViewPagerSearch adapterViewPager;

    private SearchView searchView;

    public int[] iconTabView = {
            R.mipmap.ic_clip,
            R.mipmap.ic_music,
            R.mipmap.ic_stars,
            R.mipmap.newspaper
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPagerSearch);
        setupViewPager(viewPager);



        tabLayout = (TabLayout) findViewById(R.id.tabLayoutSearch);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        int po =tabLayout.getSelectedTabPosition();
        viewPager.setCurrentItem(po);

    }



    public void setupViewPager(ViewPager viewPager) {

        adapterViewPager = new AdapterViewPagerSearch(getSupportFragmentManager());

        adapterViewPager.addToList(new FragTabSearch1(), "ایرانی");
        adapterViewPager.addToList(new FragTabSearch2(), "خارجی");
        adapterViewPager.addToList(new FragTabSearch3(), "کلیپ");
        adapterViewPager.addToList(new FragTabSearch4(), "اخبار");

        viewPager.setAdapter(adapterViewPager);
        adapterViewPager.notifyDataSetChanged();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(ActivitySearch.this , ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
