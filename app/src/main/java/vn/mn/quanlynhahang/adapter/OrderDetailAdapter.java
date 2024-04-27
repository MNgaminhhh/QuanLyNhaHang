package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.Order;
import vn.mn.quanlynhahang.model.OrderQuantity;

public class OrderDetailAdapter extends ArrayAdapter<Order> {
    Context context;
    int resource;
    Order order;

    public OrderDetailAdapter(@NonNull Context context, int resource, Order order) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.order = order;
    }
    public void setData(Order order){
        this.order = order;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return order.getOrder().size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        convertView = inflater.inflate(resource, null);

        TextView txtStt = convertView.findViewById(R.id.txtSTT);
        TextView txtTenMon = convertView.findViewById(R.id.txtTenMon);
        TextView txtSoLuong = convertView.findViewById(R.id.txtSoLuong);
        TextView txtDonGia = convertView.findViewById(R.id.txtDonGia);

        List<Map.Entry<String, OrderQuantity>> orderList = new ArrayList<>(order.getOrder().entrySet());
        Map.Entry<String, OrderQuantity> orderView = orderList.get(position);

        txtStt.setText((position+1)+"");
        txtTenMon.setText(orderView.getValue().getDish().getDishName());
        txtSoLuong.setText(orderView.getValue().getQuantity()+"");
        txtDonGia.setText(orderView.getValue().getDish().getPrice()+"");

        return convertView;
    }
}
