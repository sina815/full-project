package book.course.molareza.ir.mp3player.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.activity.ActivityCall;
import book.course.molareza.ir.mp3player.favorite.ActivityFavorite;
import book.course.molareza.ir.mp3player.activity.ActivitySetting;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragNavEnd extends Fragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ViewGroup ltCall, ltSetting, ltDialogAbout, ltDialogAnother, ltFavorite;

    public FragNavEnd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_nav, container, false);

        ltFavorite = (ViewGroup) view.findViewById(R.id.ltFavorite);
        ltFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G.currentActivity, ActivityFavorite.class);
                startActivity(intent);
            }
        });

        ltCall = (ViewGroup) view.findViewById(R.id.ltCall);
        ltCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(G.currentActivity, ActivityCall.class);
                G.currentActivity.startActivity(intent);
            }
        });

        ltSetting = (ViewGroup) view.findViewById(R.id.ltSetting);
        ltSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G.currentActivity, ActivitySetting.class);
                G.currentActivity.startActivity(intent);

            }
        });

        ltDialogAbout = (ViewGroup) view.findViewById(R.id.ltDialogAbout);
        ltDialogAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = getResources().getString(R.string.dialog_title_aboutMe);
                String message = getResources().getString(R.string.dialog_message_aboutMe);
                dialog(title, message);

            }
        });

        ltDialogAnother = (ViewGroup) view.findViewById(R.id.ltDialogAnother);
        ltDialogAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = getResources().getString(R.string.dialog_title_another_app);
                String message = getResources().getString(R.string.dialog_message_another_app);
                dialog(title, message);
            }
        });

        return view;
    }

    private void dialog(String title, String message) {

        final Dialog dialog = new Dialog(G.currentActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.alert_dialog);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitleDialog);
        txtTitle.setText(title);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessageDialog);
        txtMessage.setText(message);

        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgCloseDialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.create();
        dialog.show();

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
