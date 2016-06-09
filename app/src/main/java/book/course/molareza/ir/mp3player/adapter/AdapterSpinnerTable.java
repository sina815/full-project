package book.course.molareza.ir.mp3player.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import book.course.molareza.ir.mp3player.G;
import book.course.molareza.ir.mp3player.R;
import book.course.molareza.ir.mp3player.struct.StructSpinner;


public class AdapterSpinnerTable extends BaseAdapter {

    private ArrayList<StructSpinner> items;

    public AdapterSpinnerTable(ArrayList<StructSpinner> items) {
        this.items = items;


    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {

        private TextView txtTitleSpinner;

        public ViewHolder(View view) {

            txtTitleSpinner = (TextView) view.findViewById(R.id.txtTitleSpinner);
        }

        public void fill(BaseAdapter adapter, StructSpinner item, int position) {

            txtTitleSpinner.setText(item.title);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        StructSpinner item = items.get(position);
        if (convertView == null) {

            convertView = G.inflater.inflate(R.layout.adapter_spinner, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(this, item, position);

        return convertView;
    }
}
