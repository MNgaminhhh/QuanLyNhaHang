package vn.mn.quanlynhahang.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.fragment.TimeKeepingFragment;
import vn.mn.quanlynhahang.fragment.AccountDetailFragment;
import vn.mn.quanlynhahang.fragment.AccountFragment;
import vn.mn.quanlynhahang.fragment.AddDishFragment;
import vn.mn.quanlynhahang.fragment.AddNotificationFragment;
import vn.mn.quanlynhahang.fragment.AddTableFragment;
import vn.mn.quanlynhahang.fragment.AddUserFragment;
import vn.mn.quanlynhahang.fragment.ChartFragment;
import vn.mn.quanlynhahang.fragment.ChartNextFragment;
import vn.mn.quanlynhahang.fragment.DailyRevenueFragment;
import vn.mn.quanlynhahang.fragment.DetailFragment;
import vn.mn.quanlynhahang.fragment.DishManageFragment;
import vn.mn.quanlynhahang.fragment.EditNotificationFragment;
import vn.mn.quanlynhahang.fragment.HomeFragment;
import vn.mn.quanlynhahang.fragment.NotificationFragment;
import vn.mn.quanlynhahang.fragment.OrderFragment;
import vn.mn.quanlynhahang.fragment.PeriodRevenueFragment;
import vn.mn.quanlynhahang.fragment.ScheduleFragment;
import vn.mn.quanlynhahang.fragment.ServiceFragment;
import vn.mn.quanlynhahang.fragment.TableManageFragment;
import vn.mn.quanlynhahang.fragment.TotalRevenueFragment;
import vn.mn.quanlynhahang.fragment.UpdateDishFragment;
import vn.mn.quanlynhahang.fragment.UpdateTableFragment;
import vn.mn.quanlynhahang.fragment.UserDetailFragment;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;
    private Toolbar toolbar;
    private ImageButton imgBack;
    private TextView textView;
    private HomeViewModel homeViewModel;
    private ConstraintLayout btnBackback;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        btnBackback = findViewById(R.id.btnBackback);
        btnBackback.setVisibility(View.GONE);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (currentFragment instanceof DishManageFragment || currentFragment instanceof AccountFragment || currentFragment instanceof ServiceFragment ||
                    currentFragment instanceof TableManageFragment || currentFragment instanceof ScheduleFragment || currentFragment instanceof TimeKeepingFragment ||
                    currentFragment instanceof DailyRevenueFragment || currentFragment instanceof ChartFragment || currentFragment instanceof AddDishFragment ||
                    currentFragment instanceof AddTableFragment || currentFragment instanceof AddUserFragment || currentFragment instanceof ChartNextFragment ||
                    currentFragment instanceof DetailFragment || currentFragment instanceof TotalRevenueFragment || currentFragment instanceof PeriodRevenueFragment ||
                    currentFragment instanceof UserDetailFragment || currentFragment instanceof UpdateDishFragment || currentFragment instanceof UpdateTableFragment ||
                    currentFragment instanceof EditNotificationFragment || currentFragment instanceof AddNotificationFragment) {
                btnBackback.setVisibility(View.VISIBLE);
            } else {
                btnBackback.setVisibility(View.GONE);
            }
        });
        btnBackback.setOnClickListener(v -> onBackPressed());
        setSupportActionBar(bottomAppBar);

        loadFirst();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnItemBottom);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                btnBackback.setVisibility(View.GONE);
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.account) {
                btnBackback.setVisibility(View.GONE);
                loadFragment(new AccountDetailFragment());
                return true;
            } else if (id == R.id.notification) {
                btnBackback.setVisibility(View.GONE);
                loadFragment(new NotificationFragment());
                return true;
            }  else if (id == R.id.order) {
                btnBackback.setVisibility(View.GONE);
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
