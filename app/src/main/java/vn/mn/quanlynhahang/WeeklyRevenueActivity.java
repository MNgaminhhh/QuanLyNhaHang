package vn.mn.quanlynhahang;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeeklyRevenueActivity extends Activity {
    private TextView fromDateButton;
    private TextView toDateButton;
    private ListView dataListView;
    private Button confirmButton;
    private Button backButton;

    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;

    private List<String> selectedDates;
    private ArrayAdapter<String> selectedDatesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_revenue);

        fromDateButton = findViewById(R.id.fromDateButton);
        toDateButton = findViewById(R.id.toDateButton);
        dataListView = findViewById(R.id.dataListView);
        confirmButton = findViewById(R.id.confirmButton);
        backButton = findViewById(R.id.backButton);

        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog(fromDateButton);
            }
        });

        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarDialog(toDateButton);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic to confirm selected dates and update ListView
                updateListView();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(WeeklyRevenueActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        selectedDates = new ArrayList<>();
        selectedDatesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedDates);
        dataListView.setAdapter(selectedDatesAdapter);
    }

    private void showCalendarDialog(final TextView dateTextView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(WeeklyRevenueActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dateTextView.setText(selectedDate);

                        if (dateTextView == fromDateButton) {
                            fromDateCalendar = calendar;
                        } else if (dateTextView == toDateButton) {
                            toDateCalendar = calendar;
                        }
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updateListView() {
        selectedDates.clear();
        double totalRevenue = 0.0;

        if (fromDateCalendar != null && toDateCalendar != null) {
            Calendar currentDate = (Calendar) fromDateCalendar.clone();

            while (!currentDate.after(toDateCalendar)) {
                double dailyRevenue = calculateDailyRevenue(currentDate);
                totalRevenue += dailyRevenue;

                String selectedDate = currentDate.get(Calendar.DAY_OF_MONTH) + "/" +
                        (currentDate.get(Calendar.MONTH) + 1) + "/" +
                        currentDate.get(Calendar.YEAR) + " - Doanh thu: " + dailyRevenue;
                selectedDates.add(selectedDate);
                currentDate.add(Calendar.DATE, 1);
            }

            selectedDatesAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Vui lòng chọn cả từ ngày và đến ngày", Toast.LENGTH_SHORT).show();
        }

        // Hiển thị tổng doanh thu trong TextView "Tổng doanh thu"
        TextView thuNhapTextView = findViewById(R.id.thuNhapTextView);
        thuNhapTextView.setText("Tổng doanh thu: " + totalRevenue);
    }


    private double calculateDailyRevenue(Calendar date) {
        // Đoạn code này để tính toán doanh thu dựa trên ngày được chọn.
        // Bạn cần thay đổi để phù hợp với logic của ứng dụng của bạn.
        // Ví dụ: nếu bạn lưu trữ doanh thu theo ngày trong cơ sở dữ liệu, bạn có thể truy vấn và tính tổng doanh thu cho ngày cụ thể ở đây.
        // Trong ví dụ này, tạm thời trả về một số ngẫu nhiên.
        return Math.random() * 1000; // Đây là một giả định, bạn cần thay đổi phần này để tính toán doanh thu thực sự.
    }

}


