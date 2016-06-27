package book.course.molareza.ir.mp3player.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.activity.ActivityPlayer;
import book.course.molareza.ir.mp3player.struct.StructMusicIrani;


public class AdapterMusicKhareji extends RecyclerView.Adapter<AdapterMusicKhareji.ViewHolder> {


    public static List<StructMusicIrani> items;

    public AdapterMusicKhareji(List<StructMusicIrani> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_music, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StructMusicIrani item = items.get(position);

        holder.txtSinger.setText(item.getName());
        holder.txtAlbum.setText(item.getAlbum());
        holder.txtLike.setText("" + item.getLike());
        holder.txtVisit.setText("" + item.getVisit());

        if (item.thBitmap != null) {

            holder.imgSinger.setImageBitmap(item.thBitmap);

        } else {
            holder.imgSinger.setImageResource(R.mipmap.splash);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSinger;

        private TextView txtSinger, txtAlbum, txtLike, txtVisit, txtShare;

        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSinger = (ImageView) itemView.findViewById(R.id.imgSinger);
            txtLike = (TextView) itemView.findViewById(R.id.txtLike);
            txtVisit = (TextView) itemView.findViewById(R.id.txtVisit);
            txtSinger = (TextView) itemView.findViewById(R.id.txtSinger);
            txtAlbum = (TextView) itemView.findViewById(R.id.txtAlbum);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StructMusicIrani item = items.get(getPosition());

                    Intent intent = new Intent(G.currentActivity, ActivityPlayer.class);
                    intent.putExtra("URL_BIG_IMAGE", item.getBigimage());
                    intent.putExtra("URL_TH_IMAGE", item.getThumbnile());
                    intent.putExtra("URL_MP3_64", item.getMp364());
                    intent.putExtra("URL_MP3_128", item.getMp3128());
                    intent.putExtra("NAME", item.getName());
                    intent.putExtra("ALBUM", item.getAlbum());
                    intent.putExtra("ID", item.getId());
                    intent.putExtra("PO", getPosition());
                    intent.putExtra("TABLE", item.getTable());
                    intent.putExtra("LIKE", item.getLike());
                    G.currentActivity.startActivity(intent);
                }
            });

        }
    }
}
