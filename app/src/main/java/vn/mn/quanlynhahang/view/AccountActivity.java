package vn.mn.quanlynhahang.view;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.AccountAdapter;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class AccountActivity extends BaseActivity {
    private RecyclerView rvAccount;
    private AccountAdapter accountAdapter;
    private List<User> userList;
    private HomeViewModel homeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        rvAccount = (RecyclerView) findViewById(R.id.rvAccount);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        userList = new ArrayList<>();
        rvAccount.setLayoutManager(new GridLayoutManager(this, 2));
        accountAdapter = new AccountAdapter(this, userList);
        rvAccount.setAdapter(accountAdapter);

        loadAccountData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadAccountData() {
        homeViewModel.getAllUsers().observe(this, userList -> {
            if (userList != null) {
                accountAdapter.setUserList(userList);
                accountAdapter.notifyDataSetChanged();
            }
        });
    }


}