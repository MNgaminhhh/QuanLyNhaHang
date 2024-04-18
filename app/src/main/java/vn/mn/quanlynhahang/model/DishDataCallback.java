package vn.mn.quanlynhahang.model;

import java.util.ArrayList;

public interface DishDataCallback {
    void onDishDataLoaded(ArrayList<Dish> dishList);
    void onDishImageUrl(String url);
}
