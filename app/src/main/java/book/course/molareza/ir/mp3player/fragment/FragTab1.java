package book.course.molareza.ir.mp3player.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import book.course.molareza.ir.mp3player.adapter.AdapterMusicIrani;
import book.course.molareza.ir.mp3player.struct.StructMusicIrani;

public class FragTab1 extends Fragment {

    private RecyclerView rcvContent;
    private AdapterMusicIrani adapterMusicIrani;
    private List<StructMusicIrani> items = new ArrayList<>();

    private ProgressBar prgFrag1;

    private boolean isPage = true;

    public int page = 0;
    public int up;

    @Override
    public void onResume() {
        super.onResume();
        adapterMusicIrani.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        prgFrag1 = (ProgressBar) view.findViewById(R.id.prgFrag1);

        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);
        adapterMusicIrani = new AdapterMusicIrani(items);
        rcvContent.setAdapter(adapterMusicIrani);
        rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));
        adapterMusicIrani.notifyDataSetChanged();
        if (isPage){

            prgFrag1.setVisibility(View.VISIBLE);
            setItem();
        }else {
            prgFrag1.setVisibility(View.INVISIBLE);
        }

        return view;
    }


    private void setItem() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, G.URL_IRANI, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray array = response.getJSONArray("music");

                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            StructMusicIrani item = new StructMusicIrani();
                            JSONObject object = array.getJSONObject(i);

                            item.setId(object.getString("id"));
                            item.setName(object.getString("name"));
                            item.setAlbum(object.getString("album"));
                            item.setThumbnile(object.getString("thumbnile"));
                            item.setBigimage(object.getString("bigimage"));
                            item.setMp364(object.getString("mp364"));
                            item.setMp3128(object.getString("mp3128"));
                            item.setCat(object.getString("cat"));
                            item.setIdcat(object.getString("idcat"));
                            item.setLike(object.getInt("like"));
                            item.setVisit(object.getInt("visit"));
                            item.setShare(object.getInt("share"));
                            item.setTable(object.getString("tbName"));

                            int id = Integer.parseInt(object.getString("id"));
                            Log.i("TAGID", "id: " + id);
                            if (id <=1){
                                isPage = false;
                            }

                            String th_url = object.getString("thumbnile");
                            setImage(th_url, up);
                            up++;

                            Log.i("TAG", "onResponse: " + th_url);

                            items.add(item);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapterMusicIrani.notifyDataSetChanged();
                prgFrag1.setVisibility(View.INVISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(G.context, "failure to connect server", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onResponse: " + error.getMessage());

            }
        });

//        requestQueue.add(objectRequest);
        Volley.newRequestQueue(G.context).add(objectRequest);

    }

    private void setImage(String th_url, final int position) {

        ImageRequest imageRequest = new ImageRequest(th_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {


                adapterMusicIrani.items.get(position).thBitmap = response;
                adapterMusicIrani.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(G.context, "failure to get image", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);
    }

}
