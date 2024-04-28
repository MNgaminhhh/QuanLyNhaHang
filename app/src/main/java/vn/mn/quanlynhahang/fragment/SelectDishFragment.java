package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.DishAdapter;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.DishDB;
import vn.mn.quanlynhahang.model.Order;
import vn.mn.quanlynhahang.model.OrderDB;
import vn.mn.quanlynhahang.model.OrderQuantity;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class SelectDishFragment extends Fragment {
    ListView lstDish;
    FloatingActionButton btnOrder;
    public static MutableLiveData<ArrayList<Dish>> dishList = new MutableLiveData<>();
    public static MutableLiveData<ArrayList<Order>> orderList = new MutableLiveData<>();
    private DishAdapter adapter;
    Map<String, OrderQuantity> dishOrder = new HashMap<String, OrderQuantity>();
    Order order = new Order();
    int index;
    int orderID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_dish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstDish = view.findViewById(R.id.lstDish);
        btnOrder = view.findViewById(R.id.btnOrder);

        if (dishList.getValue() == null) {
            dishList.setValue(new ArrayList<>());
        }
        adapter = new DishAdapter(requireContext(), R.layout.custom_select_dish_layout, dishList.getValue());
        lstDish.setAdapter(adapter);

        OrderDB orderDB = new OrderDB(getContext(), orderList);
        dishList.observe(getViewLifecycleOwner(), dishes -> {
            adapter.setData(dishes);
            adapter.notifyDataSetChanged();
            orderDB.getAllOrder();
        });

        Bundle args = getArguments();
        if (args != null) {
            index = args.getInt("id", -1);
            orderID = args.getInt("oid", -1);
        }
        DishDB dishDB = new DishDB(requireContext(), dishList);
        dishDB.getAllDish();


        orderList.observe(getViewLifecycleOwner(), orders -> {
            if (orderID!=-1){
                order = orderList.getValue().stream()
                        .filter(order -> order.getId()==orderID)
                        .findFirst()
                        .orElse(null);
                for (int i = 0; i < dishList.getValue().size(); i++) {
                    int lvID = dishList.getValue().get(i).getId();
                    OrderQuantity orderQuantity = order.getOrder().get("D"+lvID);
                    if (orderQuantity!= null){
                        View viewChild = lstDish.getChildAt(i);
                        TextView txtQuantity = viewChild.findViewById(R.id.txtQuantity);
                        txtQuantity.setText(orderQuantity.getQuantity()+"");
                    }
                }
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lstDish.getChildCount(); i++) {
                    View view = lstDish.getChildAt(i);
                    TextView txtQuantity = view.findViewById(R.id.txtQuantity);
                    String quantity = txtQuantity.getText().toString();
                    int value = Integer.parseInt(quantity);
                    if (value>0){
                        dishOrder.put("D"+dishList.getValue().get(i).getId()+"", new OrderQuantity(dishList.getValue().get(i), value));
                    }
                }
                if (orderID==-1)
                {
                    order.setId(orderList.getValue().stream()
                            .mapToInt(Order::getId)
                            .max()
                            .orElse(0) + 1);
                    order.setTimeOrder(new Date());
                    if (dishOrder.size()==0){
                        Toast.makeText(getContext(), "Vui lòng chọn món!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    order.setOrder(dishOrder);
                    order.setIdTable(index);
                    order.setPaymentStatus(false);
                    order.setOrderEmp(HomeFragment.currentUserName);
                    orderDB.addNewOrder(order);
                }
                else {
                    orderDB.updateOrder(orderID, dishOrder);
                }
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}