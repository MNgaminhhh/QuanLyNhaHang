package vn.mn.quanlynhahang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
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
import java.util.Calendar;
import java.util.Locale;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.view.PeriodRevenueActivity;
import vn.mn.quanlynhahang.view.TotalRevenueActivity;

public class DailyRevenueFragment extends Fragment {
    private Button btnXemDoanhThu;
    private TextView textDate;
    private TextView textRevenue;
    private CalendarView calendarView;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_revenue, container, false);

        btnXemDoanhThu = view.findViewById(R.id.btnXemDoanhThu);
        textDate = view.findViewById(R.id.textDate);
        textRevenue = view.findViewById(R.id.textRevenue);
        calendarView = view.findViewById(R.id.calendarView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button btnKhoangThoiGian = view.findViewById(R.id.btnKhoangThoiGian);
        btnKhoangThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeriodRevenueFragment periodRevenueFragment = new PeriodRevenueFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, periodRevenueFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button btnTongDoanhThu = view.findViewById(R.id.btnTongDoanhThu);
        btnTongDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalRevenueFragment totalRevenueFragment = new TotalRevenueFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, totalRevenueFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnXemDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selectedDateInMillis = calendarView.getDate();

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTimeInMillis(selectedDateInMillis);
                int selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH);
                int selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1;
                int selectedYear = selectedCalendar.get(Calendar.YEAR);

                String formattedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth, selectedYear);
                textDate.setText(formattedDate);

                calculateAndDisplayRevenue(selectedDay, selectedMonth, selectedYear);
            }
        });

        return view;
    }

    private void calculateAndDisplayRevenue(int selectedDay, int selectedMonth, int selectedYear) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalRevenue = 0;
                for (DataSnapshot snapshot : dataSnapshot.child("order").getChildren()) {
                    long orderTimeInMillis = snapshot.child("timeOrder").child("time").getValue(Long.class);
                    Calendar orderCalendar = Calendar.getInstance();
                    orderCalendar.setTimeInMillis(orderTimeInMillis);
                    int orderDay = orderCalendar.get(Calendar.DAY_OF_MONTH);
                    int orderMonth = orderCalendar.get(Calendar.MONTH) + 1;
                    int orderYear = orderCalendar.get(Calendar.YEAR);

                    if (selectedDay == orderDay && selectedMonth == orderMonth && selectedYear == orderYear) {
                        totalRevenue += calculateOrderRevenue(snapshot.child("order"));
                    }
                }

                if (totalRevenue > 0) {
                    NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
                    String formattedRevenue = formatter.format(totalRevenue) + " VND";
                    textRevenue.setText(formattedRevenue);
                } else {
                    textRevenue.setText("0 VND");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi truy vấn dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
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
}
