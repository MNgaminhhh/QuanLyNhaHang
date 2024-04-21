package vn.mn.quanlynhahang;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyRevenueActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView selectedDateTextView, dailyRevenueTextView;
    private Button exportRevenueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_revenue);

        calendarView = findViewById(R.id.calendarView);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        dailyRevenueTextView = findViewById(R.id.dailyRevenueTextView);
        exportRevenueButton = findViewById(R.id.exportRevenueButton);

        exportRevenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selectedDateInMillis = calendarView.getDate();
                selectedDateTextView.setText("Ngày đã chọn: " + formatDate(selectedDateInMillis));

                dailyRevenueTextView.setText("Doanh thu của ngày: " + calculateDailyRevenue(selectedDateInMillis));
            }
        });

    }

    // Phương thức để chuyển đổi timestamp thành định dạng ngày/tháng/năm
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    // Phương thức để tính toán doanh thu của ngày đã chọn
    private double calculateDailyRevenue(long timestamp) {
        // Thực hiện tính toán doanh thu của ngày đã chọn
        return 0.0;
    }
}
