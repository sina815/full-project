package book.course.molareza.ir.mp3player.favorite;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterMusicKhareji;
import book.course.molareza.ir.mp3player.db.FavoriteMusicIrani;
import book.course.molareza.ir.mp3player.struct.StructMusicIrani;


public class FragTabFavorite1 extends Fragment {

    private AdapterMusicKhareji adapterMusic;
    private List<StructMusicIrani> items = new ArrayList<>();
    private ProgressBar progressBar;

    private boolean isReaped = true;

    private TextView txtNotFound;

    public int u;

    private boolean isCount = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        txtNotFound = (TextView) view.findViewById(R.id.txtNotFound);
        txtNotFound.setText(R.string.favorite_nothing);
        if (isCount){

            txtNotFound.setVisibility(View.VISIBLE);
        }

        progressBar = (ProgressBar) view.findViewById(R.id.prgFrag1);
        progressBar.setVisibility(View.INVISIBLE);

        RecyclerView rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);
        adapterMusic = new AdapterMusicKhareji(items);
        rcvContent.setAdapter(adapterMusic);
        rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));


        if (isReaped) {
            setItem();
        }


        return view;
    }

    private void setItem() {

        List<FavoriteMusicIrani> favoriteMusicIranis = G.favoriteMusicIraniDao.loadAll();

        if (!favoriteMusicIranis.isEmpty()){

            txtNotFound.setVisibility(View.INVISIBLE);
            isCount = false;
        }

        for (int i = 0; i < favoriteMusicIranis.size(); i++) {

            StructMusicIrani item = new StructMusicIrani();

            item.setId(favoriteMusicIranis.get(i).getId_item());
            item.setName(favoriteMusicIranis.get(i).getName());
            item.setAlbum(favoriteMusicIranis.get(i).getAlbum());
            item.setThumbnile(favoriteMusicIranis.get(i).getThImage());
            item.setBigimage(favoriteMusicIranis.get(i).getBigImage());
            item.setMp364(favoriteMusicIranis.get(i).getMp3_64());
            item.setMp3128(favoriteMusicIranis.get(i).getMp3_128());
            item.setTable(favoriteMusicIranis.get(i).getTable());

            String urlImage = favoriteMusicIranis.get(i).getThImage();

            setImage(urlImage, u);

            long num_id = favoriteMusicIranis.get(i).getId();
            if (num_id == 1) {
                isReaped = false;
            }
            u++;
            items.add(item);
        }

        adapterMusic.notifyDataSetChanged();

    }

    private void setImage(String urlImage, final int id) {


        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                AdapterMusicKhareji.items.get(id).thBitmap = response;
                adapterMusic.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);


    }


}