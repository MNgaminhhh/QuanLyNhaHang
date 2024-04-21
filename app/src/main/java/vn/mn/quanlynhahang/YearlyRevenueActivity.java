package vn.mn.quanlynhahang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class YearlyRevenueActivity extends Activity {
    private Spinner yearSpinner;
    private ListView monthlyRevenueListView;
    private TextView yearlyRevenueTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_revenue);

        yearSpinner = findViewById(R.id.yearSpinner);
        monthlyRevenueListView = findViewById(R.id.monthlyRevenueListView);
        yearlyRevenueTextView = findViewById(R.id.yearlyRevenueTextView);
        backButton = findViewById(R.id.backButton);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getYears());
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về trang MainActivity
                Intent intent = new Intent(YearlyRevenueActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedYear = (String) yearSpinner.getSelectedItem();
                List<Pair<String, Double>> monthlyRevenues = getMonthlyRevenues(selectedYear);

                List<String> formattedRevenues = new ArrayList<>();
                double yearlyRevenue = 0;
                for (Pair<String, Double> pair : monthlyRevenues) {
                    String month = pair.first;
                    double revenue = pair.second;
                    formattedRevenues.add(month + ": " + String.format("%.2f", revenue));
                    yearlyRevenue += revenue;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(YearlyRevenueActivity.this,
                        android.R.layout.simple_list_item_1, formattedRevenues);
                monthlyRevenueListView.setAdapter(adapter);

                yearlyRevenueTextView.setText("Tổng thu nhập của năm " + selectedYear + ": " + String.format("%.2f", yearlyRevenue));
            }
        });
    }

    // Hàm để lấy danh sách các năm từ 2022 đến 2024
    private String[] getYears() {
        String[] years = new String[3];
        for (int i = 0; i < 3; i++) {
            years[i] = String.valueOf(2022 + i);
        }
        return years;
    }

    // Hàm để lấy danh sách thu nhập hàng tháng cho một năm cụ thể, đã sắp xếp theo thứ tự tăng dần của các tháng
    private List<Pair<String, Double>> getMonthlyRevenues(String selectedYear) {
        List<Pair<String, Double>> monthlyRevenues = new ArrayList<>();
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add("Tháng " + i);
        }
        // Sắp xếp các tháng theo thứ tự tăng dần
        Collections.sort(months, new MonthComparator());
        // Tạo thu nhập ngẫu nhiên cho mỗi tháng và thêm vào danh sách
        for (String month : months) {
            double revenue = Math.random() * 1000; // Số ngẫu nhiên từ 0 đến 1000 làm giả định
            monthlyRevenues.add(new Pair<>(month, revenue));
        }
        return monthlyRevenues;
    }

    // Lớp so sánh tùy chỉnh để sắp xếp các tháng theo thứ tự tăng dần
    private class MonthComparator implements Comparator<String> {
        @Override
        public int compare(String month1, String month2) {
            int monthNumber1 = Integer.parseInt(month1.replaceAll("[\\D]", ""));
            int monthNumber2 = Integer.parseInt(month2.replaceAll("[\\D]", ""));
            return Integer.compare(monthNumber1, monthNumber2);
        }
    }
}
