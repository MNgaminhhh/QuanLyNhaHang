package vn.mn.quanlynhahang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button dayRevenueButton, weekRevenueButton, monthRevenueButton, yearRevenueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ các thành phần giao diện
        dayRevenueButton = findViewById(R.id.dayRevenueButton);
        weekRevenueButton = findViewById(R.id.weekRevenueButton);
        monthRevenueButton = findViewById(R.id.monthRevenueButton);
        yearRevenueButton = findViewById(R.id.yearRevenueButton);

        // Thiết lập sự kiện cho các nút
        dayRevenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DailyRevenueActivity.class));
            }
        });

        weekRevenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeeklyRevenueActivity.class));
            }
        });

        monthRevenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MonthlyRevenueActivity.class));
            }
        });

        yearRevenueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, YearlyRevenueActivity.class));
            }
        });
    }
}
