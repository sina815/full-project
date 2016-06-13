package book.course.molareza.ir.mp3player.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterClip;
import book.course.molareza.ir.mp3player.struct.StructClip;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTab3 extends Fragment {

    private boolean isPage = true;

    private RecyclerView rcvContent;
    private AdapterClip adapterClip;
    private List<StructClip> items = new ArrayList<>();

    private ProgressBar prgFrag3;

    private int u;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_tab3, container, false);

        prgFrag3 = (ProgressBar) view.findViewById(R.id.prgFrag3);

        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag3);
        adapterClip = new AdapterClip(items);
        rcvContent.setAdapter(adapterClip);
        rcvContent.setLayoutManager(new LinearLayoutManager(G.context));

        if (isPage){

            prgFrag3.setVisibility(View.VISIBLE);
            setItem();
        }else {
            prgFrag3.setVisibility(View.INVISIBLE);
        }


        return view;
    }

    private void setItem() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, G.URL_Clip, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("clip");

                    if (array != null) {

                        for (int i = 0; i < array.length(); i++) {
                            StructClip item = new StructClip();
                            JSONObject object = array.getJSONObject(i);

                            item.setId(object.getString("id"));
                            item.setName(object.getString("name"));
                            item.setAlbum(object.getString("album"));
                            item.setThumbnile(object.getString("thumbnile"));
                            item.setBigImage(object.getString("bigimage"));
                            item.setClip(object.getString("clip"));
                            item.setCat(object.getString("cat"));
                            item.setIdCat(object.getString("idcat"));
                            item.setLike(object.getString("like"));
                            item.setVisit(object.getString("visit"));


                            int id = Integer.parseInt(object.getString("id"));
                            Log.i("TAGID", "id: " + id);
                            if (id <=1){
                                isPage = false;
                            }
//                            Log.i("TAGQWE", "onResponse: " + item.getAlbum());

                            String image = object.getString("thumbnile");
                            imageDownloader(image, u);
                            u++;

                            items.add(item);

                        }

                    }

                    prgFrag3.setVisibility(View.GONE);
                    adapterClip.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(G.context, "failure connect to server", Toast.LENGTH_SHORT).show();
                Log.i("ERROR_CLIP", "onErrorResponse: " + error.getMessage());
            }
        });

        Volley.newRequestQueue(G.context).add(objectRequest);
    }

    private void imageDownloader(String image, final int position) {

        ImageRequest imageRequest = new ImageRequest(image, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                adapterClip.items.get(position).thBitmap = response;
                adapterClip.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(G.context, "failure download image", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);

    }
}
