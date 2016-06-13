package book.course.molareza.ir.mp3player.search;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import book.course.molareza.ir.mp3player.adapter.AdapterClip;
import book.course.molareza.ir.mp3player.struct.StructClip;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabSearch3 extends Fragment {

    private boolean isActive3;

    private RecyclerView rcvContent;
    private AdapterClip adapterClip;
    private List<StructClip> items;

    private ProgressBar prgFrag3;

    public int up;
    public String search;
    public String clip;

    private SearchView searchViewFrag3;

    private TabLayout tabLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            isActive3 = true;
        }else {
            isActive3 = false;
        }

        Toast.makeText(G.context, "isActive3: "  + isActive3  , Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayoutSearch);
//        int po = tabLayout.getSelectedTabPosition();
//        if (po == 2){
//            isActive = true;
//        }else {
//            isActive = false;
//        }
//        Toast.makeText(G.context, "isActive: "  + isActive + "    po    " + po, Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_tab3, container, false);

        prgFrag3 = (ProgressBar) view.findViewById(R.id.prgFrag3);
        prgFrag3.setVisibility(View.INVISIBLE);


        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag3);

        searchViewFrag3 = (SearchView) getActivity().findViewById(R.id.searchView);
        searchViewFrag3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (isActive3) {
                    items = new ArrayList<StructClip>();

                    up = 0;

                    search = query;

                    prgFrag3.setVisibility(View.VISIBLE);
                    adapterClip = new AdapterClip(items);
                    rcvContent.setAdapter(adapterClip);
                    rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));


                    clip = "clip";
                    setItems();
                    Toast.makeText(G.context, "po"  + "  " + clip, Toast.LENGTH_SHORT).show();

                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });

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

                            String th_url = object.getString("thumbnile");
                            setImage(th_url, up);
                            up++;
                            Log.i("TAG4321", "onQueryTextSubmit: " + th_url);
                            items.add(item);
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterClip.notifyDataSetChanged();
                prgFrag3.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(G.context, "متاسفانه چیزی پیدا نشد", Toast.LENGTH_SHORT).show();
                prgFrag3.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramss = new HashMap<>();

                paramss.put("search", search);
                paramss.put("table", clip);
                Log.i("TAG87654321", "params3: " + clip);
                return paramss;
            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }

    private void setImage(String th_url, final int position) {

        ImageRequest imageRequest = new ImageRequest(th_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                adapterClip.items.get(position).thBitmap = response;
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
