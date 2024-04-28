package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.AccountAdapter;
import vn.mn.quanlynhahang.adapter.NotificationAdapter;
import vn.mn.quanlynhahang.model.NotifUser;
import vn.mn.quanlynhahang.model.UserUid;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotifUser> notifUserList;
    private HomeViewModel homeViewModel;
    private String userId;
    private FloatingActionButton fabCreateNotif;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvItemNotif);
        fabCreateNotif = view.findViewById(R.id.btnAddNotif);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        notifUserList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NotificationAdapter(requireContext(), notifUserList);
        recyclerView.setAdapter(adapter);

        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                userId = firebaseUser.getUid();
                loadNotifiAccount();
            }
        });

        fabCreateNotif.setOnClickListener(v ->{
            AddNotificationFragment addNotificationFragment = new AddNotificationFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, addNotificationFragment)
                    .addToBackStack(null)
                    .commit();
        });

    }

    private void loadNotifiAccount() {
        Log.e("UUUUUUDDDS", userId);
        homeViewModel.getNotifications(userId).observe(getViewLifecycleOwner(), ntUser -> {
            if(ntUser != null) {
                List<NotifUser> notifUsers = ntUser;
                notifUserList.clear();
                for (NotifUser userid : notifUsers) {
                    Log.e("UUUUUUDDD", userid.toString());
                    NotifUser notifUser = new NotifUser();
                    notifUser.setNotificationContent(userid.getNotificationContent());
                    notifUser.setSenderName(userid.getSenderName());
                    notifUser.setTimeSent(userid.getTimeSent());
                    notifUserList.add(notifUser);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


}