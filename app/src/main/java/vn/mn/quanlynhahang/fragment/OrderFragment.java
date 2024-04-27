package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.TableOrderStatusAdapter;
import vn.mn.quanlynhahang.model.Order;
import vn.mn.quanlynhahang.model.OrderDB;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;

public class OrderFragment extends Fragment {
    private GridView gridOrderTable;
    public static MutableLiveData<ArrayList<Order>> orderList = new MutableLiveData<>();
    public static MutableLiveData<ArrayList<Table>> tableList = new MutableLiveData<>();
    //public static ArrayList<Boolean> status = new ArrayList<>();
    private Map<String, Boolean> status = new HashMap<String, Boolean>();
    private TableOrderStatusAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridOrderTable = view.findViewById(R.id.gridOrderTable);
        adapter = new TableOrderStatusAdapter(new ArrayList<>(), status, getContext());
        gridOrderTable.setAdapter(adapter);
        TableDB tableDB = new TableDB(getContext(), tableList);
        OrderDB orderDB = new OrderDB(getContext(), orderList);
        tableList.observe(getViewLifecycleOwner(), tables -> {
            adapter.setData(tables, status);
            Collections.sort(tableList.getValue(), Comparator.comparingInt(Table::getId));
//            for (int i = 0; i < tables.size(); i++) {
//                status.put(tables.get(i).getId()+"",true);}
            adapter.notifyDataSetChanged();
        });
        orderList.observe(getViewLifecycleOwner(), orders -> {
            status.clear();
            for (Order order: orders) {
                if (!order.isPaymentStatus()){
                    status.put(order.getIdTable()+"",false);
                    adapter.setData(tableList.getValue(), status);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        tableDB.getAllTable();
        orderDB.getAllOrder();
        gridOrderTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (status.get(tableList.getValue().get(position).getId()+"")== null) {
                    SelectDishFragment selectDishFragment = new SelectDishFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", tableList.getValue().get(position).getId());
                    //bundle.putInt("stt", position);
                    selectDishFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.container, selectDishFragment)
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", tableList.getValue().get(position).getId());
                    orderDetailFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.container, orderDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

}