package vn.mn.quanlynhahang.view;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.fragment.AccountDetailFragment;
import vn.mn.quanlynhahang.fragment.HomeFragment;
import vn.mn.quanlynhahang.fragment.NotificationFragment;
import vn.mn.quanlynhahang.fragment.OrderFragment;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;
    private Toolbar toolbar;
    private ImageButton imgBack;
    private TextView textView;
    private HomeViewModel homeViewModel;
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
            } else if (id == R.id.notification) {
                loadFragment(new NotificationFragment());
                return true;
            }  else if (id == R.id.order) {
                loadFragment(new OrderFragment());
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
