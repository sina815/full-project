package book.course.molareza.ir.mp3player.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;

public class ActivityDetailNews extends AppCompatActivity {

    private Toolbar toolbar;

    private String txtText, txtUrlImage, txtTitle;

    private ImageView imgDetail;
    private TextView txtTextDetail;

    private CollapsingToolbarLayout collapse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            txtTitle = bundle.getString("TITLE");
            txtText = bundle.getString("TEXT");
            txtUrlImage = bundle.getString("BIGIMAGE");

        }

        toolbar = (Toolbar) findViewById(R.id.toolbarNews);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapse = (CollapsingToolbarLayout) findViewById(R.id.collapse);
        assert collapse != null;
        collapse.setTitle(txtTitle);
        collapse.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapse.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);



        imgDetail = (ImageView) findViewById(R.id.imgDetail);

        Picasso.with(G.context).load(txtUrlImage).into(imgDetail);

        txtTextDetail = (TextView) findViewById(R.id.txtTextDetail);
        txtTextDetail.setText(Html.fromHtml(txtText));


    }
}
