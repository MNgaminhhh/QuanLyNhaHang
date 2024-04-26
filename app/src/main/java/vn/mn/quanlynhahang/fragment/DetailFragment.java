package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.DetailAdapter;

public class DetailFragment extends Fragment {
    private RecyclerView recyclerViewActivities;
    private DetailAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle extras = getArguments();
        if (extras != null) {
            String title = extras.getString("title");

            TextView textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewTitle.setText(title);
            recyclerViewActivities = view.findViewById(R.id.rvQuyenTruyCap);
            recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));

            String[] activityList = {"Quản lý chức vụ", "Quản lý nhân viên", "Quản lý cơ sở vật chất", "Quản lý tài chính"};
            adapter = new DetailAdapter(requireContext(), activityList);
            recyclerViewActivities.setAdapter(adapter);
        }
    }
}
