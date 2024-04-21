package vn.mn.quanlynhahang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MonthlyRevenueActivity extends Activity {
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button calculateButton;
    private TextView monthRevenueTextView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_revenue);

        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        calculateButton = findViewById(R.id.calculateButton);
        monthRevenueTextView = findViewById(R.id.monthRevenueTextView);
        backButton = findViewById(R.id.backButton);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getYears());
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedMonth = (String) monthSpinner.getSelectedItem();
                String selectedYear = (String) yearSpinner.getSelectedItem();
                double monthlyRevenue = getMonthlyRevenue(selectedMonth, selectedYear);
                monthRevenueTextView.setText("Tổng thu nhập của tháng " + selectedMonth + " năm " + selectedYear + ": " + monthlyRevenue);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về trang MainActivity
                finish();
            }
        });
    }

    // Hàm giả định để tính tổng thu nhập cho tháng và năm được chọn
    private double getMonthlyRevenue(String selectedMonth, String selectedYear) {
        // Trong ví dụ này, tôi chỉ trả về một số ngẫu nhiên làm giả định
        return Math.random() * 1000;
    }

    // Phương thức để lấy danh sách các năm từ 2022 đến 2024
    private String[] getYears() {
        String[] years = new String[3];
        for (int i = 0; i < 3; i++) {
            years[i] = String.valueOf(2022 + i);
        }
        return years;
    }
}
