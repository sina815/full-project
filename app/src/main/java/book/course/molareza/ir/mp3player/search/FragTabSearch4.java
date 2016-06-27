package book.course.molareza.ir.mp3player.search;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterNews;
import book.course.molareza.ir.mp3player.struct.StructNews;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabSearch4 extends Fragment {

    private boolean isActive4;

    private RecyclerView rcvContent;
    private AdapterNews adapterNews;
    private List<StructNews> items;

    private ProgressBar prgFrag4;

    private SearchView searchViewFrag4;

    public int up;
    public String search;
    public String news;

    private TabLayout tabLayout;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            searchViewFrag4 = (SearchView) getActivity().findViewById(R.id.searchView);
            searchViewFrag4.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    up = 0;
                    items = new ArrayList<StructNews>();
                    search = query;
                    prgFrag4.setVisibility(View.VISIBLE);
                    adapterNews = new AdapterNews(items);
                    rcvContent.setAdapter(adapterNews);
                    rcvContent.setLayoutManager(new LinearLayoutManager(G.context));
                    news = "news";
                    setItems();

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {


                    return false;
                }
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        prgFrag4 = (ProgressBar) view.findViewById(R.id.prgFrag1);
        prgFrag4.setVisibility(View.INVISIBLE);
        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);


        return view;
    }

    private void setItems() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG4321", "onQueryTextSubmit2: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("search");
                    if (array != null) {

                        for (int i = 0; i < array.length(); i++) {
                            StructNews item = new StructNews();
                            JSONObject object = array.getJSONObject(i);
                            Log.i("TAGGQWER", "array: " + object);

                            item.setId(object.getString("id"));
                            item.setTitle(object.getString("title"));
                            item.setDesc(object.getString("desc"));
                            item.setText(object.getString("text"));
                            item.setThumbnil(object.getString("thumbnil"));
                            item.setBigImage(object.getString("bigimage"));
                            item.setLike(object.getInt("like"));
                            item.setVisit(object.getInt("visit"));
                            item.setShare(object.getInt("share"));

                            String urlImage = object.getString("thumbnil");

                            setImage(urlImage, up);
                            up++;
                            items.add(item);
                        }

                    } else {
                        Log.i("TAGGQWER", "error: ");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("", "error: " + e.getMessage());

                }
                adapterNews.notifyDataSetChanged();
                prgFrag4.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(G.context, "متاسفانه چیزی پیدا نشد", Toast.LENGTH_SHORT).show();
                prgFrag4.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramss = new HashMap<>();

                paramss.put("search", search);
                paramss.put("table", news);
                Log.i("TAG4321", "params4: " + news + "    search: " + search);
                return paramss;
            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }

    private void setImage(String th_url, final int position) {

        ImageRequest imageRequest = new ImageRequest(th_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                adapterNews.items.get(position).thBitmap = response;
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
