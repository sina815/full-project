package book.course.molareza.ir.mp3player;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class AdapterMusic extends RecyclerView.Adapter<AdapterMusic.ViewHolder> {

    public List<StructMusic> items;

    public AdapterMusic(List<StructMusic> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_music, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StructMusic item = items.get(position);

        holder.txtSinger.setText(item.getName());
        holder.txtAlbum.setText(item.getAlbum());

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

        private ImageView imgSinger, imgLike, imgShare, imgVisit;

        private TextView txtSinger, txtAlbum;

        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSinger = (ImageView) itemView.findViewById(R.id.imgSinger);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            imgShare = (ImageView) itemView.findViewById(R.id.imgShare);
            imgVisit = (ImageView) itemView.findViewById(R.id.imgVisit);
            txtSinger = (TextView) itemView.findViewById(R.id.txtSinger);
            txtAlbum = (TextView) itemView.findViewById(R.id.txtAlbum);
            cardView = (CardView) itemView.findViewById(R.id.cardView);

        }
    }
}
