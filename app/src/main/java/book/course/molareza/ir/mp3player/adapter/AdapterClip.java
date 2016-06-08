package book.course.molareza.ir.mp3player.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.struct.StructClip;

/**
 * Created by Home on 6/7/2016.
 */
public class AdapterClip extends RecyclerView.Adapter<AdapterClip.ViewHolder> {


    public List<StructClip> items;

    public AdapterClip(List<StructClip> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_clip, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StructClip item = items.get(position);

        holder.txtSingerClip.setText(item.getName());
        holder.txtAlbumClip.setText(item.getAlbum());
        holder.txtLikeClip.setText(item.getLike());
        holder.txtVisitClip.setText(item.getVisit());

        if (item.thBitmap != null) {
            holder.imgSinger.setImageBitmap(item.thBitmap);
        } else {
            holder.imgSinger.setImageResource(R.mipmap.ninja);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSinger;
        private TextView txtAlbumClip, txtSingerClip, txtLikeClip, txtVisitClip;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSinger = (ImageView) itemView.findViewById(R.id.imgSingerClip);
            txtAlbumClip = (TextView) itemView.findViewById(R.id.txtAlbumClip);
            txtSingerClip = (TextView) itemView.findViewById(R.id.txtSingerClip);
            txtLikeClip = (TextView) itemView.findViewById(R.id.txtLikeClip);
            txtVisitClip = (TextView) itemView.findViewById(R.id.txtVisitClip);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

        }
    }
}