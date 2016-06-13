package book.course.molareza.ir.mp3player.search;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterViewPagerSearch;

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

//        searchView = (SearchView) findViewById(R.id.searchView);
//
//
//        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
//            public boolean onQueryTextChange(String newText) {
//                // this is your adapter that will be filtered
//                return false;
//            }
//
//            public boolean onQueryTextSubmit(String query) {
//                //Here u can get the value "query" which is entered in the search box.
//
//
//                return false;
//            }
//        };
//        searchView.setOnQueryTextListener(queryTextListener);

        viewPager = (ViewPager) findViewById(R.id.viewPagerSearch);
        setupViewPager(viewPager);



        tabLayout = (TabLayout) findViewById(R.id.tabLayoutSearch);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        setupTab();

        int po =tabLayout.getSelectedTabPosition();
        viewPager.setCurrentItem(po);

    }

    private void setupTab() {

//        tabLayout.getTabAt(0).setIcon(iconTabView[0]);
//        tabLayout.getTabAt(1).setIcon(iconTabView[1]);
//        tabLayout.getTabAt(2).setIcon(iconTabView[2]);
//        tabLayout.getTabAt(3).setIcon(iconTabView[3]);
//
//        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
//        tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
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
}
