package book.course.molareza.ir.mp3player.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import book.course.molareza.ir.mp3player.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragNavEnd extends Fragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    public FragNavEnd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_nav_end, container, false);




        return view;
    }

    public void setup(DrawerLayout draw, Toolbar toolbar, ImageView menu_right, final LinearLayout layoutRoot) {

        drawerLayout = draw;

        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);


            }
        };


        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        menu_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.post(new Runnable() {
                    @Override
                    public void run() {

                        drawerLayout.openDrawer(GravityCompat.END);
                    }
                });
            }
        });

    }
}
