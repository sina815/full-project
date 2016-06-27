package book.course.molareza.ir.mp3player.search;


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
import com.android.volley.RequestQueue;
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
import book.course.molareza.ir.mp3player.adapter.AdapterMusicIKhareji;
import book.course.molareza.ir.mp3player.struct.StructMusicKhareji;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabSearch2 extends Fragment {

    private boolean isActive2;

    private RecyclerView rcvContent;
    private AdapterMusicIKhareji adapterMusicIKhareji;
    private List<StructMusicKhareji> items;

    private ProgressBar prgFrag2;

    public int up;
    public String search;
    public String khareji;

    private RequestQueue requestFrag2;
    private SearchView searchViewFrag2;

    private TabLayout tabLayout;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

                searchViewFrag2 = (SearchView) getActivity().findViewById(R.id.searchView);
                searchViewFrag2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {


                        items = new ArrayList<>();

                        up = 0;

                        search = query;

                        prgFrag2.setVisibility(View.VISIBLE);
                        adapterMusicIKhareji = new AdapterMusicIKhareji(items);
                        rcvContent.setAdapter(adapterMusicIKhareji);
                        rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));
                        khareji = "khareji";
                        setItems();

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {


                        return false;
                    }
                });


                Toast.makeText(G.context, "tab2", Toast.LENGTH_SHORT).show();
            } else {
                isActive2 = false;
            }
        }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_tab1, container, false);

        prgFrag2 = (ProgressBar) view.findViewById(R.id.prgFrag1);
        prgFrag2.setVisibility(View.INVISIBLE);
        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag1);

        return view;
    }

    private void setItems() {
        requestFrag2 = Volley.newRequestQueue(G.context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TAG4321", "onQueryTextSubmit2: " + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("search");

                    if (array != null) {

                        for (int i = 0; i < array.length(); i++) {
                            StructMusicKhareji item = new StructMusicKhareji();
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
                adapterMusicIKhareji.notifyDataSetChanged();
                prgFrag2.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(G.context, "متاسفانه چیزی پیدا نشد", Toast.LENGTH_SHORT).show();
                prgFrag2.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> paramss = new HashMap<>();

                paramss.put("search", search);
                paramss.put("table", khareji);
                Log.i("TAG87654321", "params2: " + khareji);
                return paramss;
            }
        };

        requestFrag2.add(stringRequest);


    }

    private void setImage(String th_url, final int position) {

        ImageRequest imageRequest = new ImageRequest(th_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                adapterMusicIKhareji.items.get(position).thBitmap = response;
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