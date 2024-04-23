package vn.mn.quanlynhahang.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.fragment.AccountDetailFragment;
import vn.mn.quanlynhahang.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);

        loadFirst();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnItemBottom);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.account) {
                loadFragment(new AccountDetailFragment());
                return true;
            } else {
                return false;
            }
        });
    }

    private void loadFirst() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new HomeFragment());
        transaction.commit();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}
