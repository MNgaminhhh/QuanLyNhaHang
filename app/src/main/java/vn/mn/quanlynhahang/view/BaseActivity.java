package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.mn.quanlynhahang.R;

public class BaseActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnItemBottom);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(this,"account", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.account) {
                Intent intent = new Intent(BaseActivity.this, AccountDetailActivity.class);
                startActivity(intent);
                Toast.makeText(this,"account", Toast.LENGTH_SHORT).show();
                return true;
            }else
                return false;
        });
    }
}
