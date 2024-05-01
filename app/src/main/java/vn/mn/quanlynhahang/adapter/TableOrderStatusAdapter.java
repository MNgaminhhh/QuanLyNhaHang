package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Table;

public class TableOrderStatusAdapter extends BaseAdapter {
    ArrayList<Table> tableList;
    Map<String, Boolean> state;
    Context context;
    LayoutInflater layoutInflater;
    public TableOrderStatusAdapter(ArrayList<Table> tableList, Map<String, Boolean> state, Context context) {
        this.tableList = tableList;
        this.context = context;
        this.state = state;
    }
    public void setData(ArrayList<Table> data, Map<String, Boolean> state){
        this.tableList = data;
        this.state = state;
    }
    @Override
    public int getCount() {
        return tableList != null ? tableList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null)
            layoutInflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.custom_order_layout, null);
        ImageView gridImage = convertView.findViewById(R.id.gridImage);
        ImageView imgStatus = convertView.findViewById(R.id.imgState);
        TextView gridTableNumber = convertView.findViewById(R.id.txtTableNumber);
        Table table = tableList.get(position);
        switch (table.getNumberOfDiner()) {
            case 2:
                gridImage.setImageResource(R.drawable.table2);
                break;
            case 4:
                gridImage.setImageResource(R.drawable.table4);
                break;
            case 6:
                gridImage.setImageResource(R.drawable.table6);
                break;
            case 8:
                gridImage.setImageResource(R.drawable.table8);
                break;
        }
        gridTableNumber.setText(table.getId()+"");
        if (state.get(tableList.get(position).getId()+"")== null){
            imgStatus.setImageResource(R.drawable.greendot);
        }
        else {
            imgStatus.setImageResource(R.drawable.reddot);
        }
        return convertView;
    }
}
