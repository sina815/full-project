package book.course.molareza.ir.mp3player.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.activity.ActivityCall;
import book.course.molareza.ir.mp3player.activity.ActivityInfo;
import book.course.molareza.ir.mp3player.activity.ActivitySetting;
import book.course.molareza.ir.mp3player.favorite.ActivityFavorite;
import book.course.molareza.ir.mp3player.otherApp.ActivityOtherApp;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragNavEnd extends Fragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private ViewGroup ltCall, ltSetting, ltDialogAbout, ltDialogAnother, ltFavorite , ltInfo;


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

                drawerLayout.closeDrawers();
                getActivity().finish();
            }
        });

        ltInfo = (ViewGroup) view.findViewById(R.id.ltInfo);
        ltInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(G.currentActivity , ActivityInfo.class);
                G.currentActivity.startActivity(intent);
                drawerLayout.closeDrawers();
                getActivity().finish();

            }
        });

        ltCall = (ViewGroup) view.findViewById(R.id.ltCall);
        ltCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(G.currentActivity, ActivityCall.class);
                G.currentActivity.startActivity(intent);
                drawerLayout.closeDrawers();
                getActivity().finish();
            }
        });

        ltSetting = (ViewGroup) view.findViewById(R.id.ltSetting);
        ltSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(G.currentActivity, ActivitySetting.class);
                G.currentActivity.startActivity(intent);
                drawerLayout.closeDrawers();
                getActivity().finish();

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


                Intent intent = new Intent(G.currentActivity, ActivityOtherApp.class);
                G.currentActivity.startActivity(intent);
                drawerLayout.closeDrawers();
                getActivity().finish();

            }
        });

        return view;
    }

    private void dialog(String title, String message) {

        final Dialog dialog = new Dialog(G.currentActivity);
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
        txtTitleDialog.setText(title);

        TextView txtMessageDialog = (TextView) dialog.findViewById(R.id.txtMessageDialog);
        txtMessageDialog.setText(message);

        Button btnSite = (Button) dialog.findViewById(R.id.btnDialog);

        btnSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(link));
//                startActivity(i);

            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //animation for dialog

        dialog.show();

    }

    public void setup(final DrawerLayout draw, final Toolbar toolbar, ImageView menu_right, final CoordinatorLayout layoutRoot) {

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

                int widthDrawer = drawerView.getWidth();
                float pad = (slideOffset * widthDrawer) * (-1);
                layoutRoot.setTranslationX(pad);
                drawerLayout.requestLayout();

            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        drawerLayout.setScrimColor(getResources().getColor(R.color.transition_full));

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
