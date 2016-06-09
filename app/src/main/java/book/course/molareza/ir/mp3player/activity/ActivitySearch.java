package book.course.molareza.ir.mp3player.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import book.course.molareza.ir.mp3player.adapter.AdapterSpinner;
import book.course.molareza.ir.mp3player.adapter.AdapterSpinnerTable;
import book.course.molareza.ir.mp3player.struct.StructMusic;
import book.course.molareza.ir.mp3player.struct.StructSpinner;

public class ActivitySearch extends AppCompatActivity {

    private Spinner spinnerType, spinnerTable;
    private AdapterSpinner adapterSpinnerType;
    private ArrayList<StructSpinner> spinnerTypeList = new ArrayList<>();

    private AdapterSpinnerTable adapterSpinnerTable;
    private ArrayList<StructSpinner> spinnerTableList = new ArrayList<>();


    private EditText edtSearch;
    private RecyclerView rcvContent;
    private AdapterMusic adapterMusic;
    private List<StructMusic> items;

    private Button btnSearch;
    private String search;

    private ProgressBar prgSearch;
    public int up;

    public String table;

    public String type;

    public RequestQueue requestQueue = Volley.newRequestQueue(G.context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        adapterSpinnerType = new AdapterSpinner(spinnerTypeList);
        spinnerType.setAdapter(adapterSpinnerType);
        setItemsSpinnerType();


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {

                    case 0:
                        type = "name";
                        break;
                    case 1:
                        type = "album";
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTable = (Spinner) findViewById(R.id.spinnerTable);
        adapterSpinnerTable = new AdapterSpinnerTable(spinnerTableList);
        spinnerTable.setAdapter(adapterSpinnerTable);
        setItemsSpinnerTabe();
        spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        table = "irani";
                        break;
                    case 1:
                        table = "khareji";
                        break;
                    case 2:

                        table = "clip";
                        break;
                    case 3:
                        table = "news";
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        prgSearch = (ProgressBar) findViewById(R.id.prgSearch);
        assert prgSearch != null;
        prgSearch.setVisibility(View.INVISIBLE);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        rcvContent = (RecyclerView) findViewById(R.id.rcvContentSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                items = new ArrayList<StructMusic>();
                adapterMusic = new AdapterMusic(items);
                rcvContent.setAdapter(adapterMusic);
                rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));

                up = 0;
                assert prgSearch != null;
                prgSearch.setVisibility(View.VISIBLE);
                search = edtSearch.getText().toString().trim();
                setItems();
            }
        });


    }

    private void setItemsSpinnerTabe() {
        String[] table = new String[]{"موزیک ایرانی", "موزیک خارجی", "کلیپ", "اخبار"};

        for (int i = 0; i < table.length; i++) {

            StructSpinner item = new StructSpinner();

            item.title = table[i];
            spinnerTableList.add(item);

        }
        adapterSpinnerTable.notifyDataSetChanged();
    }

    private void setItemsSpinnerType() {
        String[] type = {"نام خواننده", "نام آلبوم"};

        for (int i = 0; i < type.length; i++) {

            StructSpinner item = new StructSpinner();

            item.title = type[i];
            spinnerTypeList.add(item);

        }
        adapterSpinnerType.notifyDataSetChanged();
    }

    private void setItems() {

        Toast.makeText(ActivitySearch.this, "" + type + "---" + table, Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_SEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("TAGASDF", "response: " + response);
                //   Toast.makeText(ActivitySearch.this, "" + response, Toast.LENGTH_LONG).show();

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

                            Log.i("SEARCH", "onResponse11: " + th_url);

                            items.add(item);

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapterMusic.notifyDataSetChanged();
                prgSearch.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("TAGASDF", "error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("search", search);
                params.put("type", type);
                params.put("table", table);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void setImage(String th_url, final int position) {

        ImageRequest imageRequest = new ImageRequest(th_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {


                adapterMusic.items.get(position).thBitmap = response;
                adapterMusic.notifyDataSetChanged();

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
