package book.course.molareza.ir.mp3player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.adapter.AdapterDownload;
import book.course.molareza.ir.mp3player.db.ListDownload;
import book.course.molareza.ir.mp3player.struct.StructDownload;

public class ActivityDownload extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView rcvContent;
    private AdapterDownload adapterDownload;
    private List<StructDownload> items = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();

        G.currentActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        toolbar = (Toolbar) findViewById(R.id.toolbarDownload);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        rcvContent = (RecyclerView) findViewById(R.id.rcvContentDownload);
        adapterDownload = new AdapterDownload(items);
        rcvContent.setAdapter(adapterDownload);
        rcvContent.setLayoutManager(new GridLayoutManager(G.context, 2));
        setItems();


    }

    private void setItems() {

        List<ListDownload> listDownloads = G.listDownload.loadAll();

        for (ListDownload ls : listDownloads) {
            StructDownload item = new StructDownload();

            item.setName(ls.getName());
            item.setAlbum(ls.getAlbum());
            item.setPathImage(ls.getImage());
            item.setPathfile(ls.getMp3());
            item.setType(ls.getType());

            items.add(item);

        }

        adapterDownload.notifyDataSetChanged();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(this, ActivityMain.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
