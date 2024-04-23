package vn.mn.quanlynhahang.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Table;

public class TableAdapter extends BaseAdapter {
    ArrayList<Table> tableList;
    Context context;
    LayoutInflater layoutInflater;
    public TableAdapter(ArrayList<Table> tableList, Context context) {
        this.tableList = tableList;
        this.context = context;
    }
    public void setData(ArrayList<Table> data){
        this.tableList = data;
    }
    @Override
    public int getCount() {
        return tableList.size();
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
            convertView = layoutInflater.inflate(R.layout.custom_table_layout, null);
        ImageView gridImage = convertView.findViewById(R.id.gridImage);
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
        return convertView;
    }
}
