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
import book.course.molareza.ir.mp3player.adapter.AdapterMusicIKhareji;
import book.course.molareza.ir.mp3player.db.FavoriteMusicKhareji;
import book.course.molareza.ir.mp3player.struct.StructMusicKhareji;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabFavorite2 extends Fragment {

    private AdapterMusicIKhareji adapterMusicIKhareji;
    private List<StructMusicKhareji> items = new ArrayList<>();
    private ProgressBar progressBar;

    private boolean isReaped = true;
    public int u;

    private TextView txtNotFound;
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
        adapterMusicIKhareji = new AdapterMusicIKhareji(items);
        rcvContent.setAdapter(adapterMusicIKhareji);
        rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));
        if (isReaped) {
            setItem();
        }
//        adapterNews.notifyDataSetChanged();
        return view;
    }

    private void setItem() {

        List<FavoriteMusicKhareji> favoriteMusicKharejis = G.favoriteMusicKharejiDao.loadAll();

        if (!favoriteMusicKharejis.isEmpty()){

            txtNotFound.setVisibility(View.INVISIBLE);
            isCount = false;
        }

        for (int i = 0; i < favoriteMusicKharejis.size(); i++) {

            StructMusicKhareji item = new StructMusicKhareji();

            item.setId(favoriteMusicKharejis.get(i).getId_item());
            item.setName(favoriteMusicKharejis.get(i).getName());
            item.setAlbum(favoriteMusicKharejis.get(i).getAlbum());
            item.setThumbnile(favoriteMusicKharejis.get(i).getThImage());
            item.setBigimage(favoriteMusicKharejis.get(i).getBigImage());
            item.setMp364(favoriteMusicKharejis.get(i).getMp3_64());
            item.setMp3128(favoriteMusicKharejis.get(i).getMp3_128());
            item.setTable(favoriteMusicKharejis.get(i).getTable());

            String urlImage = favoriteMusicKharejis.get(i).getThImage();

            setImage(urlImage, u);

            long num_id = favoriteMusicKharejis.get(i).getId();
            if (num_id == 1) {
                isReaped = false;
            }

            u++;
            items.add(item);

        }

        adapterMusicIKhareji.notifyDataSetChanged();

    }

    private void setImage(String urlImage, final int id) {


        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                AdapterMusicIKhareji.items.get(id).thBitmap = response;
                adapterMusicIKhareji.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);
    }


}