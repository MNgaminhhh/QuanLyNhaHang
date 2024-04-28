package vn.mn.quanlynhahang.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.Table;

public class DishAdapter extends ArrayAdapter<Dish> {
    Context  context;
    int resource;
    ArrayList<Dish> dishList;
    public DishAdapter(@NonNull Context context, int resource, ArrayList<Dish> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.dishList = objects;
    }
    public void setData(ArrayList<Dish> data){
        this.dishList.clear();
        this.dishList.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        convertView = inflater.inflate(resource, null);

        ImageView dishImage = convertView.findViewById(R.id.dishImage);
        TextView txtDishName = convertView.findViewById(R.id.txtDishName);
        TextView txtDishPrice = convertView.findViewById(R.id.txtDishPrice);

        Dish dish = dishList.get(position);

        txtDishName.setText("Món: " + dish.getDishName() + "");
        txtDishPrice.setText("Giá: " + dish.getPrice()+ " VNĐ");
        if (!dish.getInStocks()) {
            txtDishName.setPaintFlags(txtDishName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtDishPrice.setPaintFlags(txtDishName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        Glide.with(context).load(dish.getUrlImage()).placeholder(R.drawable.baseline_sync_24)
                .error(R.drawable.imageerror).into(dishImage);
        if (resource==R.layout.custom_select_dish_layout && dish.getInStocks()){
            Button btnAdd = convertView.findViewById(R.id.btnAdd);
            Button btnMinus = convertView.findViewById(R.id.btnMinus);
            TextView txtQuantity = convertView.findViewById(R.id.txtQuantity);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtQuantity.setText((Integer.parseInt(txtQuantity.getText().toString())+1)+"");
                }
            });
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int value = Integer.parseInt(txtQuantity.getText().toString());
                    if (value>0) {
                        txtQuantity.setText((value-1)+"");
                    }
                }
            });
        }
        return convertView;
    }
}
