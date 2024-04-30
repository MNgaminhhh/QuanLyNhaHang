package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.EditNotificationAdapter;
import vn.mn.quanlynhahang.model.NotifUser;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class EditNotificationFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditNotificationAdapter adapter;
    private List<NotifUser> notifUserList;
    private EditText edtText;
    private HomeViewModel homeViewModel;
    private String userId, selectedDate;
    private Spinner spinnerDate;
    private Button btnDelete, btnSuaThongTin;
    private List<NotifUser> allList = new ArrayList<>();
    private List<NotifUser> displayList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvAccountNotif);
        spinnerDate = view.findViewById(R.id.spinnerDate);
        btnDelete = view.findViewById(R.id.btnXoaThongBao);
        btnSuaThongTin = view.findViewById(R.id.btnSuaThongBao);
        edtText = view.findViewById(R.id.edtSuaThongBao);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        notifUserList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new EditNotificationAdapter(requireContext(), notifUserList);
        recyclerView.setAdapter(adapter);
        

        spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(spinnerAdapter);

        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                userId = firebaseUser.getUid();
                loadNotifications();
            }
        });
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate = spinnerDate.getSelectedItem().toString();
                filterNotificationsByDate(selectedDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        btnDelete.setOnClickListener(v -> deleteSelectedItems());
        btnSuaThongTin.setOnClickListener(v -> editSelectedItems(edtText.getText().toString().trim()));
    }


    private void editSelectedItems(String newNotificationContent) {
        List<String> selectedItems = adapter.getSelectedTime();
        if (!selectedItems.isEmpty()) {
            for (String keyId : selectedItems) {
                homeViewModel.updateNotification(keyId, newNotificationContent).observe(getViewLifecycleOwner(), result -> {
                    if (result) {
                        Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        NotificationFragment notificationFragment = new NotificationFragment();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.container, notificationFragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
                loadNotifications();
            }
        } else {
            Toast.makeText(requireContext(), "Không có item nào được chọn", Toast.LENGTH_SHORT).show();
        }
    }



    private void deleteSelectedItems() {
        List<String> selectedItems = adapter.getSelectedTime();
        if (!selectedItems.isEmpty()) {
            for (String time : selectedItems) {
                homeViewModel.deleteNotification(time).observe(getViewLifecycleOwner(), result -> {
                    if (result) {
                        Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        NotificationFragment notificationFragment = new NotificationFragment();
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.container, notificationFragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
                loadNotifications();
            }
        } else {
            Toast.makeText(requireContext(), "Không có item nào được chọn", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadNotifications() {
        homeViewModel.loadNotificationUser(userId).observe(getViewLifecycleOwner(), notificationList -> {
            if (notificationList != null) {
                allList.clear();
                allList.addAll(notificationList);
                displayList.clear();
                final int[] observerCount = {0};
                for (NotifUser notification : notificationList) {
                    homeViewModel.getUserData(notification.getUserUid()).observe(getViewLifecycleOwner(), userData -> {
                        if (userData != null) {
                            String fullname = userData.getFullname();
                            notification.setSenderName("Đến: " + fullname);
                        }
                        observerCount[0]++;
                        if (observerCount[0] == notificationList.size()) {
                            displayList.addAll(allList);
                            adapter.notifyDataSetChanged();
                            List<String> notificationDates = extractDates(displayList);
                            updateSpinnerDates(notificationDates);
                        }
                    });
                }
            }
        });
    }


    private void filterNotificationsByDate(String selectedDate) {
        if (selectedDate.equals("Tất cả")) {
            displayList.clear();
            displayList.addAll(allList);
        } else {
            displayList.clear();
            for (NotifUser notification : allList) {
                String timeSent = notification.getTimeSent();
                String notificationDate = extractDate(timeSent);
                if (notificationDate.equals(selectedDate)) {
                    displayList.add(notification);
                }
            }
        }
        adapter.updateList(displayList);
        adapter.notifyDataSetChanged();
    }


    private List<String> extractDates(List<NotifUser> notificationList) {
        Set<String> dateSet = new HashSet<>();
        for (NotifUser notification : notificationList) {
            String timeSent = notification.getTimeSent();
            String date = extractDate(timeSent);
            dateSet.add(date);
        }
        List<String> dates = new ArrayList<>(dateSet);
        dates.add(0, "Tất cả");
        return dates;
    }

    private void updateSpinnerDates(List<String> notificationDates) {
        spinnerAdapter.clear();
        spinnerAdapter.addAll(notificationDates);
        spinnerAdapter.notifyDataSetChanged();
    }

    private String extractDate(String timestamp) {
        long milliseconds = Long.parseLong(timestamp);
        Date date = new Date(milliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
