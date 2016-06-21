package book.course.molareza.ir.mp3player.favorite;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterNews;
import book.course.molareza.ir.mp3player.db.FavoriteDetail;
import book.course.molareza.ir.mp3player.struct.StructNews;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabFavorite4 extends Fragment {

    private AdapterNews adapterNews;
    private List<StructNews> items = new ArrayList<>();
    private ProgressBar progressBar;

    private boolean isReaped = true;

    public int u;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        G.favoriteDetailDao.deleteAll();
        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.prgFrag1);
        progressBar.setVisibility(View.INVISIBLE);

        //     Toast.makeText(G.context, "ا ها پاک شد", Toast.LENGTH_SHORT).show();

        RecyclerView rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);
        adapterNews = new AdapterNews(items);
        rcvContent.setAdapter(adapterNews);
        rcvContent.setLayoutManager(new LinearLayoutManager(G.context));

        if (isReaped) {
            setItem();
        }
//        adapterNews.notifyDataSetChanged();
        return view;
    }

    private void setItem() {

        List<FavoriteDetail> favoritesList = G.favoriteDetailDao.loadAll();

        for (int i = 0; i < favoritesList.size(); i++) {

            // Toast.makeText(G.context, "ا ها پاک شد", Toast.LENGTH_SHORT).show();
            StructNews item = new StructNews();

            item.setId(favoritesList.get(i).getId_item());
            item.setTitle(favoritesList.get(i).getName());
            item.setDesc(favoritesList.get(i).getAlbum());
            item.setText(favoritesList.get(i).getText());
            item.setThumbnil(favoritesList.get(i).getThImage());
            item.setBigImage(favoritesList.get(i).getBigImage());
            String urlImage = favoritesList.get(i).getThImage();

            long num_id = favoritesList.get(i).getId();
            if (num_id == 1) {
                isReaped = false;
            }
            setImage(urlImage, u);

            u++;
            items.add(item);

        }

        adapterNews.notifyDataSetChanged();

    }

    private void setImage(String urlImage, final int id) {

        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                AdapterNews.items.get(id).setThBitmap(response);
                adapterNews.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);
    }

}


//            Log.i("LOG123", "big: " + favoritesList.get(i).getBigImage());