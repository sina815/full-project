package book.course.molareza.ir.mp3player.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.struct.StructOtherApp;


public class AdapterInfo extends RecyclerView.Adapter<AdapterInfo.ViewHolder> {

    public List<StructOtherApp> items;

    public AdapterInfo(List<StructOtherApp> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        StructOtherApp item = items.get(position);

        holder.txtInfo.setText(Html.fromHtml(item.text));

        if (item.getBitmapImage() != null) {
            holder.imgInfo.setImageBitmap(item.getBitmapImage());
        } else {
            holder.imgInfo.setImageResource(R.mipmap.splash);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgInfo;
        private TextView txtInfo;

        public ViewHolder(View itemView) {
            super(itemView);

            imgInfo = (ImageView) itemView.findViewById(R.id.imgInfo);
            txtInfo = (TextView) itemView.findViewById(R.id.txtInfo);


        }
    }
}
