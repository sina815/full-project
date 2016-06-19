package book.course.molareza.ir.mp3player.adapter;

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
import book.course.molareza.ir.mp3player.activity.ActivityDetailNews;
import book.course.molareza.ir.mp3player.struct.StructNews;


public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    public static int visitPlus = 0;

    public static List<StructNews> items;

    public AdapterNews(List<StructNews> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StructNews item = items.get(position);

        holder.txtDescc.setText(Html.fromHtml(item.getDesc()));
        holder.txtLikeNews.setText("" + item.getLike());

//        int visit = item.getVisit() + item.visitPlus;
        holder.txtVisitNews.setText("" + item.visit);
        holder.txtShareNews.setText("" + item.getShare());

        if (item.thBitmap != null) {
            holder.imgNews.setImageBitmap(item.getThBitmap());
        } else {
            holder.imgNews.setImageResource(R.mipmap.ninja);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgNews;
        private TextView txtDescc, txtLikeNews, txtVisitNews, txtShareNews;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            imgNews = (ImageView) itemView.findViewById(R.id.imgNews);
            txtDescc = (TextView) itemView.findViewById(R.id.txtDescc);
            txtLikeNews = (TextView) itemView.findViewById(R.id.txtLikeNews);
            txtVisitNews = (TextView) itemView.findViewById(R.id.txtVisitNews);
            txtShareNews = (TextView) itemView.findViewById(R.id.txtShareNews);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StructNews item = items.get(getPosition());
                    Intent intent = new Intent(G.currentActivity, ActivityDetailNews.class);
                    intent.putExtra("TITLE", item.getTitle());
                    intent.putExtra("TEXT", item.getText());
                    intent.putExtra("BIGIMAGE", item.bigImage);
                    intent.putExtra("ID", item.id);
                    intent.putExtra("PO", getPosition());
                    G.currentActivity.startActivity(intent);
                }
            });
        }
    }
}
