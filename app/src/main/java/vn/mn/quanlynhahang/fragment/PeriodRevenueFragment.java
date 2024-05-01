package vn.mn.quanlynhahang.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.RevenueAdapter;
import vn.mn.quanlynhahang.model.RevenueItem;

public class PeriodRevenueFragment extends Fragment {
    private Button fromDateButton;
    private Button toDateButton;
    private Button confirmButton;
    private TextView thuNhapTextView;
    private ListView dataListView;
    private DatabaseReference databaseReference;
    private Calendar startDate;
    private Calendar endDate;
    private long totalRevenue = 0;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_period_revenue, container, false);

        fromDateButton = view.findViewById(R.id.fromDateButton);
        toDateButton = view.findViewById(R.id.toDateButton);
        confirmButton = view.findViewById(R.id.confirmButton);
        thuNhapTextView = view.findViewById(R.id.thuNhapTextView);
        dataListView = view.findViewById(R.id.dataListView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(startDate);
            }
        });

        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(endDate);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayRevenue(startDate, endDate);
            }
        });

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = (String) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Ngày được chọn: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void showDatePicker(final Calendar date) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.SECOND, 0);
                updateDateButton();
            }
        };

        new DatePickerDialog(requireContext(), dateSetListener, date.get(Calendar.YEAR),
                date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fromDateButton.setText(sdf.format(startDate.getTime()));
        toDateButton.setText(sdf.format(endDate.getTime()));
    }

    private void calculateAndDisplayRevenue(Calendar startDate, Calendar endDate) {
        totalRevenue = 0;
        ArrayList<RevenueItem> revenueList = new ArrayList<>();

        databaseReference.child("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long orderTimeInMillis = snapshot.child("timeOrder").child("time").getValue(Long.class);
                    Calendar orderCalendar = Calendar.getInstance();
                    orderCalendar.setTimeInMillis(orderTimeInMillis);

                    if (orderCalendar.compareTo(startDate) >= 0 && orderCalendar.compareTo(endDate) <= 0) {
                        String formattedDate = formatDate(orderCalendar);
                        long revenue = calculateOrderRevenue(snapshot.child("order"));
                        totalRevenue += revenue;
                        RevenueItem revenueItem = new RevenueItem(formattedDate, revenue);
                        revenueList.add(revenueItem);
                    }
                }

                RevenueAdapter adapter = new RevenueAdapter(requireContext(), revenueList);
                dataListView.setAdapter(adapter);

                thuNhapTextView.setText(formatCurrency(totalRevenue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi truy vấn dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private long calculateOrderRevenue(DataSnapshot orderSnapshot) {
        long orderRevenue = 0;
        for (DataSnapshot dishSnapshot : orderSnapshot.getChildren()) {
            long price = dishSnapshot.child("dish").child("price").getValue(Long.class);
            long quantity = dishSnapshot.child("quantity").getValue(Long.class);
            orderRevenue += price * quantity;
        }
        return orderRevenue;
    }

    private String formatCurrency(long amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        return formatter.format(amount) + " VND";
    }
}
