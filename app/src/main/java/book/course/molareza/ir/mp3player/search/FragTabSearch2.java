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
import book.course.molareza.ir.mp3player.adapter.AdapterMusic;
import book.course.molareza.ir.mp3player.struct.StructMusic;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragTabSearch2 extends Fragment {

    private boolean isActive2;

    private RecyclerView rcvContent;
    private AdapterMusic adapterMusic2;
    private List<StructMusic> items;

    private ProgressBar prgFrag2;

    public int up;
    public String search;
    public String khareji;

    private RequestQueue requestFrag2;
    private SearchView searchViewFrag2;

    private TabLayout tabLayout;

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            isActive2 = true;
        }else {
            isActive2 = false;
        }

        Toast.makeText(G.context, "isActive2: "  + isActive2  , Toast.LENGTH_SHORT).show();

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayoutSearch);
//        int po = tabLayout.getSelectedTabPosition();
//        if (po == 1){
//            isActive = true;
//        }else {
//            isActive = false;
//        }
//        Toast.makeText(G.context, "isActive: "  + isActive + "    po    " + po, Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.frag_tab2, container, false);


        prgFrag2 = (ProgressBar) view.findViewById(R.id.prgFrag2);
        prgFrag2.setVisibility(View.INVISIBLE);

        //    Log.i("TAG4321", "params2: " + khareji);

        rcvContent = (RecyclerView) view.findViewById(R.id.rcvContentFrag2);

        searchViewFrag2 = (SearchView) getActivity().findViewById(R.id.searchView);
        searchViewFrag2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (isActive2) {
                    items = new ArrayList<StructMusic>();
                    up = 0;
                    search = query;

                    prgFrag2.setVisibility(View.VISIBLE);
                    adapterMusic2 = new AdapterMusic(items);
                    rcvContent.setAdapter(adapterMusic2);
                    rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));

                    khareji = "khareji";
                    setItems();
                    Toast.makeText(G.context, "po"  + "  " + khareji, Toast.LENGTH_SHORT).show();
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
                            StructMusic item = new StructMusic();
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
                adapterMusic2.notifyDataSetChanged();
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

                adapterMusic2.items.get(position).thBitmap = response;
                adapterMusic2.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        Volley.newRequestQueue(G.context).add(imageRequest);

    }

}