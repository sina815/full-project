package book.course.molareza.ir.mp3player.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import book.course.molareza.ir.mp3player.activity.ActivityClipOnline;
import book.course.molareza.ir.mp3player.activity.ActivityPlayerOffline;
import book.course.molareza.ir.mp3player.struct.StructDownload;


public class AdapterDownload extends RecyclerView.Adapter<AdapterDownload.ViewHolder> {

    private List<StructDownload> items;

    public AdapterDownload(List<StructDownload> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_download, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StructDownload item = items.get(position);

        Bitmap bitmap = BitmapFactory.decodeFile(item.getPathImage());
        holder.imgSinger_download.setImageBitmap(bitmap);

        holder.txtSinger_download.setText(item.getName());
        holder.txtAlbum_download.setText(item.getAlbum());

        if (item.getType().equals("irani")) {


            holder.txtType.setText("موزیک ایرانی");

        } else if (item.getType().equals("clip")) {

            holder.txtType.setText("کلیپ");
        } else {

            holder.txtType.setText("موزیک خارجی");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSinger_download;
        private TextView txtSinger_download, txtAlbum_download, txtType;

        private CardView cardView_download;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSinger_download = (ImageView) itemView.findViewById(R.id.imgSinger_download);
            txtSinger_download = (TextView) itemView.findViewById(R.id.txtSinger_download);
            txtAlbum_download = (TextView) itemView.findViewById(R.id.txtAlbum_download);
            txtType = (TextView) itemView.findViewById(R.id.txtType);

            cardView_download = (CardView) itemView.findViewById(R.id.cardView_download);

            cardView_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StructDownload item = items.get(getPosition());

                    if (item.getType().equals("irani")) {


                        Intent intent = new Intent(G.currentActivity, ActivityPlayerOffline.class);
                        intent.putExtra("NAME", item.getName());
                        intent.putExtra("ALBUM", item.getAlbum());
                        intent.putExtra("IMAGE", item.getPathImage());
                        intent.putExtra("MP3", item.getPathfile());
                        G.currentActivity.startActivity(intent);

                    } else if (item.getType().equals("clip")) {

                        Intent intent = new Intent(G.currentActivity, ActivityClipOnline.class);
                        G.currentActivity.startActivity(intent);

                    } else {

                        Intent intent = new Intent(G.currentActivity, ActivityPlayerOffline.class);
                        intent.putExtra("NAME", item.getName());
                        intent.putExtra("ALBUM", item.getAlbum());
                        intent.putExtra("IMAGE", item.getPathImage());
                        intent.putExtra("MP3", item.getPathfile());
                        G.currentActivity.startActivity(intent);
                    }

                }
            });


        }
    }
}
