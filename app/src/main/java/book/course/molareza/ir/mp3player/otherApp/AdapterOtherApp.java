package book.course.molareza.ir.mp3player.otherApp;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.struct.StructOtherApp;

/**
 * Created by Home on 6/28/2016.
 */
public class AdapterOtherApp extends RecyclerView.Adapter<AdapterOtherApp.ViewHolder> {

    public static List<StructOtherApp> items;

    public AdapterOtherApp(List<StructOtherApp> items) {
        AdapterOtherApp.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StructOtherApp item = items.get(position);

        holder.txtDescc.setText(Html.fromHtml(item.getDesc()));
        holder.txtLikeNews.setText("" + item.getLike());
        holder.txtVisitNews.setText("" + item.getVisit());

        if (item.getBitmapImage() != null) {
            holder.imgNews.setImageBitmap(item.getBitmapImage());
        } else {
            holder.imgNews.setImageResource(R.mipmap.splash);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgNews;
        private TextView txtDescc, txtLikeNews, txtVisitNews;
        private CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);

            imgNews = (ImageView) itemView.findViewById(R.id.imgNews);
            txtDescc = (TextView) itemView.findViewById(R.id.txtDescc);
            txtLikeNews = (TextView) itemView.findViewById(R.id.txtLikeNews);
            txtVisitNews = (TextView) itemView.findViewById(R.id.txtVisitNews);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StructOtherApp item = items.get(getPosition());
                    Intent intent = new Intent(G.currentActivity, ActivityOtherAppDetailes.class);
                    intent.putExtra("ID", item.getId());
                    intent.putExtra("DESC", item.getDesc());
                    intent.putExtra("TEXT", item.getText());
                    intent.putExtra("BIGIMAGE", item.getBigImage());
                    intent.putExtra("THIMAGE", item.getThImage());
                    intent.putExtra("LINK", item.getLink());
                    intent.putExtra("LIKE", item.getLike());
                    intent.putExtra("PO", getPosition());
                    G.currentActivity.startActivity(intent);
                }
            });

        }
    }
}
