package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.OrderAdapter;
import vn.mn.quanlynhahang.model.OrderEmp;

public class ChartNextFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<OrderEmp> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart_next, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(requireContext(), orderList);
        recyclerView.setAdapter(orderAdapter);

        // Kết nối với Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("order");

        // Truy vấn dữ liệu từ Firebase
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear(); // Xóa dữ liệu cũ để tránh trùng lặp

                Set<String> uniqueOrders = new HashSet<>();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderEmpName = orderSnapshot.child("orderEmp").getValue(String.class);
                    long orderTime = orderSnapshot.child("timeOrder").child("time").getValue(Long.class);

                    String key = orderEmpName + formatDate(orderTime);

                    uniqueOrders.add(key);
                }

                for (String uniqueOrder : uniqueOrders) {
                    String orderEmpName = uniqueOrder.substring(0, uniqueOrder.length() - 8);
                    String orderTime = uniqueOrder.substring(uniqueOrder.length() - 8);

                    int orderCount = countOrders(orderEmpName, orderTime, dataSnapshot);

                    OrderEmp orderEmp = new OrderEmp(orderEmpName, orderTime, orderCount);
                    orderList.add(orderEmp);
                }

                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        return view;
    }

    private int countOrders(String orderEmpName, String orderTime, DataSnapshot dataSnapshot) {
        int count = 0;
        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
            String name = orderSnapshot.child("orderEmp").getValue(String.class);
            long time = orderSnapshot.child("timeOrder").child("time").getValue(Long.class);
            String formattedTime = formatDate(time);
            if (orderEmpName.equals(name) && orderTime.equals(formattedTime)) {
                count++;
            }
        }
        return count;
    }

    private String formatDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
