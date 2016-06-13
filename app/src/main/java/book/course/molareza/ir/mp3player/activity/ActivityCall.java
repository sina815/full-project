package book.course.molareza.ir.mp3player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterSpinner;
import book.course.molareza.ir.mp3player.struct.StructSpinner;

public class ActivityCall extends AppCompatActivity {

    private EditText edtEmail, edtName, edtText;
    private Button btnSend;

    String email, name, text, sp;

    private Spinner spinner;
    private AdapterSpinner adapterSpinner;
    private ArrayList<StructSpinner> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        edtEmail = (EditText) findViewById(R.id.edtEmailCall);
        edtName = (EditText) findViewById(R.id.edtNameCall);
        edtText = (EditText) findViewById(R.id.editText);
        btnSend = (Button) findViewById(R.id.btnSend);

        spinner = (Spinner) findViewById(R.id.spinnerCall);
        adapterSpinner = new AdapterSpinner(items);
        spinner.setAdapter(adapterSpinner);
        setItem();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edtEmail.getText().toString().trim();
                name = edtName.getText().toString().trim();
                text = edtText.getText().toString().trim();
                int po = spinner.getSelectedItemPosition();
                switch (po) {
                    case 0:
                        sp = "پشتیبانی";
                        break;
                    case 1:
                        sp = "مدیریت";
                        break;
                    case 2:
                        sp = "سایر موارد";
                        break;
                }

                if (email != null) {

                    if (name != null) {

                        if (text != null) {

                            sendData();

                        } else {
                            Toast.makeText(ActivityCall.this, "لطفا متن خود را وارد کنید", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ActivityCall.this, "لطفا نام خود را وارد کنید", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ActivityCall.this, "لطفا ایمیل خود را وارد کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void sendData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, G.URL_CALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("TAGCALL", "onResponse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ActivityCall.this, "متاسفانه ارسال صورت نگرفت", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("name", name);
                params.put("cat", sp);
                params.put("text", text);

                Log.i("TAGCALL", "params: " + params);

                return params;
            }
        };

        Volley.newRequestQueue(G.context).add(stringRequest);

    }

    public void setItem() {

        String[] title = {"پشتیبانی", "مدیریت", "سایرموارد",};
        for (int i = 0; i < title.length; i++) {

            StructSpinner item = new StructSpinner();
            item.title = title[i];
            items.add(item);
        }
        adapterSpinner.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                Intent intent = new Intent(ActivityCall.this, ActivityMain.class);
                startActivity(intent);
                finish();

                break;
        }

        return super.onKeyDown(keyCode, event);

    }
}
