package book.course.molareza.ir.mp3player.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import book.course.molareza.ir.mp3player.MyToast;
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

    private ViewGroup layoutRefreshAgain;

    @Override
    public void onResume() {
        super.onResume();

        adapterClip.notifyDataSetChanged();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        prgFrag3 = (ProgressBar) view.findViewById(R.id.prgFrag1);

        layoutRefreshAgain = (ViewGroup) view.findViewById(R.id.layoutRefreshAgain);
        layoutRefreshAgain.setVisibility(View.GONE);

        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);
        adapterClip = new AdapterClip(items);
        rcvContent.setAdapter(adapterClip);
        rcvContent.setLayoutManager(new GridLayoutManager(G.context,2));
        adapterClip.notifyDataSetChanged();

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
                            if (id <=1){
                                isPage = false;

                            }
                            String image = object.getString("thumbnile");
                            imageDownloader(image, u);
                            u++;

                            items.add(item);
                        }

                    }

                    prgFrag3.setVisibility(View.GONE);
                    adapterClip.notifyDataSetChanged();
                    layoutRefreshAgain.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                MyToast.makeText(G.context, "متاسفانه ارتباط با سرور برقرار نشد", Toast.LENGTH_SHORT).show();
                prgFrag3.setVisibility(View.INVISIBLE);
                layoutRefreshAgain.setVisibility(View.VISIBLE);
                layoutRefreshAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItem();
                        layoutRefreshAgain.setVisibility(View.GONE);
                        prgFrag3.setVisibility(View.VISIBLE);
                    }
                });
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

                MyToast.makeText(G.context, "متاسفانه مشکلی در دریافت عکس ها ازسمت سرور به وجود آمده !", Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);

    }
}
