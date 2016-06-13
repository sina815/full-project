package book.course.molareza.ir.mp3player.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
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
import book.course.molareza.ir.mp3player.adapter.AdapterNews;
import book.course.molareza.ir.mp3player.struct.StructNews;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTab4 extends Fragment {

    private boolean isPage = true;

    private RecyclerView rcvContent;
    private AdapterNews adapterNews;
    private List<StructNews> items = new ArrayList<>();

    private ProgressBar prgFrag4;

    private int u;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_tab4, container, false);

        prgFrag4 = (ProgressBar) view.findViewById(R.id.prgFrag4);

        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag4);
        adapterNews = new AdapterNews(items);
        rcvContent.setAdapter(adapterNews);
        rcvContent.setLayoutManager(new LinearLayoutManager(G.context));

        if (isPage){

            prgFrag4.setVisibility(View.VISIBLE);
            setItems();
        }else {
            prgFrag4.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void setItems() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, G.URL_NEWS, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("news");

                    if (array != null) {

                        for (int i = 0; i < array.length(); i++) {

                            StructNews item = new StructNews();
                            JSONObject object = array.getJSONObject(i);

                            item.setId(object.getString("id"));
                            item.setTitle(object.getString("title"));
                            item.setDesc(object.getString("desc"));
                            item.setText(object.getString("text"));
                            item.setThumbnil(object.getString("thumbnil"));
                            item.setBigImage(object.getString("bigimage"));
                            item.setLike(object.getInt("like"));
                            item.setVisit(object.getInt("visit"));
                            item.setShare(object.getInt("share"));

                            int id = Integer.parseInt(object.getString("id"));
                            Log.i("TAGID", "id: " + id);
                            if (id <=1){
                                isPage = false;
                            }

                            String urlImage = object.getString("thumbnil");
                            imageDownloader(urlImage, u);
                            u++;

                            items.add(item);

                        }

                    }

                    prgFrag4.setVisibility(View.INVISIBLE);
                    adapterNews.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(G.context, "failure to connect to server", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(G.context).add(objectRequest);
    }

    private void imageDownloader(String urlImage, final int position) {

        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                adapterNews.items.get(position).setThBitmap(response);
                adapterNews.notifyDataSetChanged();

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
