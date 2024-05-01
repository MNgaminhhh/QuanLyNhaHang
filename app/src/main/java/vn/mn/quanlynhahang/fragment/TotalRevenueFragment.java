package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.mn.quanlynhahang.R;

public class TotalRevenueFragment extends Fragment {
    private EditText yearEditText;
    private Button calculateButton;
    private TextView totalYearlyRevenueTextView;
    private ListView monthlyRevenueListView;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_revenue, container, false);

        yearEditText = view.findViewById(R.id.yearEditText);
        calculateButton = view.findViewById(R.id.calculateButton);
        totalYearlyRevenueTextView = view.findViewById(R.id.totalYearlyRevenueTextView);
        monthlyRevenueListView = view.findViewById(R.id.monthlyRevenueListView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("order");

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputYear = yearEditText.getText().toString().trim();
                if (TextUtils.isEmpty(inputYear)) {
                    Toast.makeText(requireContext(), "Vui lòng nhập năm", Toast.LENGTH_SHORT).show();
                } else {
                    int year = Integer.parseInt(inputYear);
                    calculateYearlyRevenue(year);
                }
            }
        });

        return view;
    }

    private void calculateYearlyRevenue(int year) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalYearlyRevenue = 0;
                Map<String, Long> monthlyRevenueMap = new HashMap<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long orderTimeInMillis = snapshot.child("timeOrder").child("time").getValue(Long.class);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(orderTimeInMillis);
                    int orderYear = calendar.get(Calendar.YEAR);
                    if (orderYear == year) {
                        long orderRevenue = calculateOrderRevenue(snapshot);
                        totalYearlyRevenue += orderRevenue;

                        int orderMonth = calendar.get(Calendar.MONTH) + 1; // January is 0
                        String key = String.format(Locale.getDefault(), "%02d", orderMonth);
                        if (monthlyRevenueMap.containsKey(key)) {
                            long currentRevenue = monthlyRevenueMap.get(key);
                            monthlyRevenueMap.put(key, currentRevenue + orderRevenue);
                        } else {
                            monthlyRevenueMap.put(key, orderRevenue);
                        }
                    }
                }

                NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                String formattedTotalYearlyRevenue = formatter.format(totalYearlyRevenue) + " VND";
                totalYearlyRevenueTextView.setText(formattedTotalYearlyRevenue);

                ArrayList<String> monthlyRevenueList = new ArrayList<>();
                for (Map.Entry<String, Long> entry : monthlyRevenueMap.entrySet()) {
                    String month = entry.getKey();
                    long revenue = entry.getValue();
                    String formattedRevenue = formatter.format(revenue);
                    String item = String.format(Locale.getDefault(), "Tháng %s - Thu nhập %s VND", month, formattedRevenue);
                    monthlyRevenueList.add(item);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_list_item_1, monthlyRevenueList);
                monthlyRevenueListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private long calculateOrderRevenue(DataSnapshot orderSnapshot) {
        long orderRevenue = 0;
        for (DataSnapshot dishSnapshot : orderSnapshot.child("order").getChildren()) {
            long price = dishSnapshot.child("dish").child("price").getValue(Long.class);
            long quantity = dishSnapshot.child("quantity").getValue(Long.class);
            orderRevenue += price * quantity;
        }
        return orderRevenue;
    }
}
