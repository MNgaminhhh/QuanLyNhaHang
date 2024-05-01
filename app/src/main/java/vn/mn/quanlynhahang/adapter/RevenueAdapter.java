package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import vn.mn.quanlynhahang.model.RevenueItem;

public class RevenueAdapter extends ArrayAdapter<RevenueItem> {
    public RevenueAdapter(@NonNull Context context, @NonNull List<RevenueItem> revenueList) {
        super(context, 0, revenueList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        RevenueItem item = getItem(position);

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(item.getDate() + " - Thu nhập: " + item.getRevenue());

        return convertView;
    }
}
