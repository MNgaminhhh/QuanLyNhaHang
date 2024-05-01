package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.OrderEmp;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderEmp> orderList;

    public OrderAdapter(Context context, List<OrderEmp> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderEmp order = orderList.get(position);
        holder.textViewTen.setText(order.getOrderEmpName());
        holder.textViewNgay.setText(formatDate(order.getOrderTime()));
        holder.textViewLanOrder.setText(String.valueOf(order.getOrderCount()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTen, textViewNgay, textViewLanOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTen = itemView.findViewById(R.id.textViewTen);
            textViewNgay = itemView.findViewById(R.id.textViewNgay);
            textViewLanOrder = itemView.findViewById(R.id.textViewLanOrder);
        }
    }

    private String formatDate(String time) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(time);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
