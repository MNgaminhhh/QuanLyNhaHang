package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.DetailAdapter;
import vn.mn.quanlynhahang.model.Role;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;

public class DetailFragment extends Fragment {
    private RecyclerView recyclerViewActivities;
    private DetailAdapter adapter;
    private Button btnSave;
    private String title;
    private ServiceViewModel serviceViewModel;
    private Map<String, String> titletofragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSave = view.findViewById(R.id.btnLuu);
        serviceViewModel = new ViewModelProvider(requireActivity()).get(ServiceViewModel.class);

        titletofragment = new HashMap<>();
        titletofragment.put("Quản lý chức vụ", "ServiceFragment");
        titletofragment.put("Quản lý nhân viên", "AccountFragment");
        titletofragment.put("Quản lý món ăn", "DishManageFragment");
        titletofragment.put("Quản lý bàn ăn", "TableManageFragment");
        titletofragment.put("Chấm công", "TimeKeepingFragment");
        titletofragment.put("Lịch làm việc", "ScheduleFragment");
        titletofragment.put("Quản lý doanh thu", "DailyRevenueFragment");
        titletofragment.put("Quản lý chất lượng nhân viên", "ChartFragment");

        Bundle extras = getArguments();
        if (extras != null) {
            title = extras.getString("title");

            TextView textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewTitle.setText(title);

            recyclerViewActivities = view.findViewById(R.id.rvQuyenTruyCap);
            recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));
            String[] activityList = titletofragment.keySet().toArray(new String[0]);

            adapter = new DetailAdapter(requireContext(), activityList);
            recyclerViewActivities.setAdapter(adapter);
        }
        loadCheckBoxRole();
        btnSave.setOnClickListener(v -> {
            List<String> selectedItems = adapter.getSelectedItems();
            saveSelectedItems(selectedItems);
        });
    }

    private void loadCheckBoxRole() {
        serviceViewModel.getRole(title)
                .addOnSuccessListener(role -> {
                    if (role != null) {
                        List<String> selectedItems = role.getDanhSach();
                        List<String> selectedClasses = new ArrayList<>();
                        for (String selectedItem : selectedItems) {
                            for (Map.Entry<String, String> entry : titletofragment.entrySet()) {
                                if (entry.getValue().equals(selectedItem)) {
                                    String correspondingClass = entry.getKey();
                                    selectedClasses.add(correspondingClass);
                                    break;
                                }
                            }
                        }
                        String[] selectedArray = selectedClasses.toArray(new String[0]);
                        adapter.setSelectedItems(selectedArray);
                    }
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(requireView(), "Failed: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
    }

    private void saveSelectedItems(List<String> selectedItems) {
        if (selectedItems.isEmpty()) {
            return;
        }
        List<String> selectedClasses = new ArrayList<>();
        for (String item : selectedItems) {
            String correspondingClass = titletofragment.get(item);
            if (correspondingClass != null) {
                selectedClasses.add(correspondingClass);
            }
        }
        Role newRole = new Role(title, selectedClasses);
        serviceViewModel.updateRole(title, newRole)
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(requireView(), "Dịch vụ đã được cập nhật thành công", Snackbar.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(requireView(), "Cập nhật dịch vụ thất bại: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
    }
}
