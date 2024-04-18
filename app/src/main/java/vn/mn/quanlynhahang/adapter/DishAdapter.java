package vn.mn.quanlynhahang.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Dish;

public class DishAdapter extends ArrayAdapter<Dish> {
    Activity context;
    int resource;
    ArrayList<Dish> dishList;
    public DishAdapter(Activity context, int resource, ArrayList<Dish> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.dishList = objects;
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

        txtDishName.setText(dish.getDishName());
        txtDishPrice.setText(dish.getPrice()+" VNĐ");
        Glide.with(context).load(dish.getUrlImage()).into(dishImage);
        return convertView;
    }
}
