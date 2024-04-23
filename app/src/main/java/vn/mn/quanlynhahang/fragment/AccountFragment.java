package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.AccountAdapter;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class AccountFragment extends Fragment {

    private RecyclerView rvAccount;
    private AccountAdapter accountAdapter;
    private List<User> userList;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAccount = view.findViewById(R.id.rvAccount);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        userList = new ArrayList<>();
        rvAccount.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        accountAdapter = new AccountAdapter(requireContext(), userList);
        rvAccount.setAdapter(accountAdapter);

        loadAccountData();
    }

    private void loadAccountData() {
        homeViewModel.getAllUsers().observe(getViewLifecycleOwner(), userList -> {
            if (userList != null) {
                accountAdapter.setUserList(userList);
                accountAdapter.notifyDataSetChanged();
            }
        });
    }
}
