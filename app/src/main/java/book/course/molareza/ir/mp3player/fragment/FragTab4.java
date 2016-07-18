package book.course.molareza.ir.mp3player.fragment;


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

    private ViewGroup layoutRefreshAgain;

    @Override
    public void onResume() {
        super.onResume();
        adapterNews.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        prgFrag4 = (ProgressBar) view.findViewById(R.id.prgFrag1);

        layoutRefreshAgain = (ViewGroup) view.findViewById(R.id.layoutRefreshAgain);
        layoutRefreshAgain.setVisibility(View.GONE);

        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);
        adapterNews = new AdapterNews(items);
        rcvContent.setAdapter(adapterNews);
        rcvContent.setLayoutManager(new LinearLayoutManager(G.context));

        if (isPage) {

            prgFrag4.setVisibility(View.VISIBLE);
            setItem();
        } else {
            prgFrag4.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void setItem() {

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
                            if (id <= 1) {
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
                    layoutRefreshAgain.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyToast.makeText(G.context, "متاسفانه ارتباط با سرور برقرار نشد", Toast.LENGTH_SHORT).show();
                prgFrag4.setVisibility(View.INVISIBLE);
                layoutRefreshAgain.setVisibility(View.VISIBLE);
                layoutRefreshAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setItem();
                        layoutRefreshAgain.setVisibility(View.GONE);
                        prgFrag4.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        Volley.newRequestQueue(G.context).add(objectRequest);
    }

    private void imageDownloader(String urlImage, final int position) {

        ImageRequest imageRequest = new ImageRequest(urlImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                AdapterNews.items.get(position).setThBitmap(response);
                adapterNews.notifyDataSetChanged();

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
