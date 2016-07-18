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
import book.course.molareza.ir.mp3player.adapter.AdapterClip;
import book.course.molareza.ir.mp3player.db.FavoriteClip;
import book.course.molareza.ir.mp3player.struct.StructClip;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabFavorite3 extends Fragment {


    private AdapterClip adapterClip;
    private List<StructClip> items = new ArrayList<>();
    private ProgressBar progressBar;

    private boolean isReaped = true;
    public int u;

    private TextView txtNotFound;

    private boolean isCount = true;

    private ViewGroup layoutRefreshAgain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        layoutRefreshAgain = (ViewGroup) view.findViewById(R.id.layoutRefreshAgain);
        layoutRefreshAgain.setVisibility(View.GONE);

        txtNotFound = (TextView) view.findViewById(R.id.txtNotFound);
        txtNotFound.setText(R.string.favorite_nothing);

        if (isCount){

            txtNotFound.setVisibility(View.VISIBLE);
        }

        progressBar = (ProgressBar) view.findViewById(R.id.prgFrag1);
        progressBar.setVisibility(View.INVISIBLE);

        RecyclerView rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);
        adapterClip = new AdapterClip(items);
        rcvContent.setAdapter(adapterClip);
        rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));
        if (isReaped) {
            setItem();
        }
//        adapterNews.notifyDataSetChanged();
        return view;
    }

    private void setItem() {

        List<FavoriteClip> favoriteClips = G.favoriteClipDao.loadAll();

        if (!favoriteClips.isEmpty()){

            txtNotFound.setVisibility(View.INVISIBLE);
            isCount = false;
        }



        if (!favoriteClips.isEmpty()){

            txtNotFound.setVisibility(View.INVISIBLE);
        }


        for (int i = 0; i < favoriteClips.size(); i++) {


            StructClip item = new StructClip();

            item.setId(favoriteClips.get(i).getId_item());
            item.setName(favoriteClips.get(i).getName());
            item.setAlbum(favoriteClips.get(i).getAlbum());
            item.setThumbnile(favoriteClips.get(i).getThImage());
            item.setBigImage(favoriteClips.get(i).getBigImage());
            item.setClip(favoriteClips.get(i).getClip());

            String urlImage = favoriteClips.get(i).getThImage();

            setImage(urlImage, u);

            long num_id = favoriteClips.get(i).getId();
            if (num_id == 1) {
                    isReaped = false;
            }

            u++;
            items.add(item);

        }

        adapterClip.notifyDataSetChanged();

    }

    private void setImage(String urlImage, final int id) {

        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                adapterClip.items.get(id).thBitmap = response;
                adapterClip.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);
    }


}