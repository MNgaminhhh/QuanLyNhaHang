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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
    private FloatingActionButton fabCreateNotif, fabNotif, fabEditeNotif;
    private Animation rotateOpen, rotateClose, formBottom, toBottom;
    private boolean isOpen = false;
    private String userId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvItemNotif);
        fabNotif = view.findViewById(R.id.btnNotif);
        fabCreateNotif = view.findViewById(R.id.btnAddNotif);
        fabEditeNotif= view.findViewById(R.id.btnEditNotif);

        rotateOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim);
        formBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.form_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        userId = HomeFragment.userid;

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        homeViewModel.getNotifications(userId).observe(getViewLifecycleOwner(), ntUser -> {
            notifUserList = new ArrayList<>();
            for (NotifUser userid : ntUser) {
                NotifUser notifUser = new NotifUser();
                notifUser.setNotificationContent(userid.getNotificationContent());
                notifUser.setSenderName("Từ: " + userid.getSenderName());
                notifUser.setTimeSent(userid.getTimeSent());
                notifUserList.add(notifUser);
            }
            adapter = new NotificationAdapter(requireContext(), notifUserList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        fabNotif.setOnClickListener(v -> {
            if (isOpen) {
                closeMenu();
            } else {
                openMenu();
            }
        });

        fabCreateNotif.setOnClickListener(v ->{
            isOpen = false;
            AddNotificationFragment addNotificationFragment = new AddNotificationFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, addNotificationFragment)
                    .addToBackStack(null)
                    .commit();
        });

        fabEditeNotif.setOnClickListener(v ->{
            isOpen = false;
            EditNotificationFragment editNotificationFragment = new EditNotificationFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, editNotificationFragment)
                    .addToBackStack(null)
                    .commit();
        });
//        loadNotifiAccount();
    }
    private void openMenu() {
        fabNotif.startAnimation(rotateOpen);
        fabCreateNotif.startAnimation(formBottom);
        fabEditeNotif.startAnimation(formBottom);
        fabCreateNotif.setClickable(true);
        fabEditeNotif.setClickable(true);
        isOpen = true;
    }

    private void closeMenu() {
        fabNotif.startAnimation(rotateClose);
        fabCreateNotif.startAnimation(toBottom);
        fabEditeNotif.startAnimation(toBottom);
        fabCreateNotif.setClickable(false);
        fabEditeNotif.setClickable(false);
        isOpen = false;
    }
    private void loadNotifiAccount() {
        homeViewModel.getNotifications(userId).observe(getViewLifecycleOwner(), ntUser -> {
            if(ntUser != null) {
                List<NotifUser> notifUsers = ntUser;
                notifUserList.clear();
                for (NotifUser userid : notifUsers) {
                    NotifUser notifUser = new NotifUser();
                    notifUser.setNotificationContent(userid.getNotificationContent());
                    notifUser.setSenderName("Từ: " + userid.getSenderName());
                    notifUser.setTimeSent(userid.getTimeSent());
                    notifUserList.add(notifUser);
                }
                adapter = new NotificationAdapter(requireContext(), notifUserList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }


}