package vn.mn.quanlynhahang.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.HomeAdapter;
import vn.mn.quanlynhahang.model.ItemHome;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class HomeActivity extends BaseActivity {
    private TextView txtUserDetails;
    private HomeViewModel homeViewModel;
    private List<ItemHome> itemHomeList;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtUserDetails = findViewById(R.id.txtUserDetails);
        recyclerView = (RecyclerView) findViewById(R.id.rvItemHome);

        itemHomeList = createItemHome();
        HomeAdapter adapter = new HomeAdapter(this, itemHomeList);
        GridLayoutManager layoutManager = new GridLayoutManager (this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getCurrentUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                homeViewModel.getUserData(firebaseUser.getUid()).observe(HomeActivity.this, user -> {
                    if (user != null) {
                        String userDetails = "Xin chào, " + user.getFullname();
                        txtUserDetails.setText(userDetails);
                    } else {
                        txtUserDetails.setText(".....");
                    }
                });
            }
        });
    }

    private List<ItemHome> createItemHome() {
        List<ItemHome> itemHomeList = new ArrayList<>();
        itemHomeList.add(new ItemHome(R.drawable.avatar, "Nhân Viên", new Intent(this, AccountActivity.class)));
        itemHomeList.add(new ItemHome(R.drawable.avatar, "Chức Vụ", new Intent(this, ServiceActivity.class)));
        return itemHomeList;
    }
}
