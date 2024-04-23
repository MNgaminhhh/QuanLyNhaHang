package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.ServiceAdapter;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;

public class ServiceFragment extends Fragment {

    private EditText edtTenChucVu;
    private Button btnThem, btnSua, btnXoa;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private ServiceViewModel serviceViewModel;
    private ServiceAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        edtTenChucVu = view.findViewById(R.id.edtThemChucVu);
        btnThem = view.findViewById(R.id.btnThem);
        btnSua = view.findViewById(R.id.btnSua);
        btnXoa = view.findViewById(R.id.btnXoa);
        recyclerView = view.findViewById(R.id.rvItemService);

        adapter = new ServiceAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        serviceViewModel.getServices().observe(getViewLifecycleOwner(), services -> {
            adapter.setServiceList(services);
        });

        btnThem.setOnClickListener(v -> clickAddService());
        btnSua.setOnClickListener(v -> clickUpdateService());
        btnXoa.setOnClickListener(v -> clickDeleteService());

        adapter.setOnItemClickListener((position, serviceName) -> {
            selectedPosition = position;
            edtTenChucVu.setText(serviceName);
            btnSua.setEnabled(true);
            btnXoa.setEnabled(true);
        });

        return view;
    }

    private void clickDeleteService() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            List<String> serviceList = adapter.getServiceList();
            if (selectedPosition < serviceList.size()) {
                String serviceName = serviceList.get(selectedPosition);
                serviceViewModel.deleteService(serviceName)
                        .addOnSuccessListener(aVoid -> {
                            serviceList.remove(selectedPosition);
                            adapter.notifyDataSetChanged();
                            btnSua.setEnabled(false);
                            btnXoa.setEnabled(false);
                            Toast.makeText(requireContext(), "Xóa dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Xóa dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(requireContext(), "Vui lòng chọn một dịch vụ để xóa", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Vui lòng chọn một dịch vụ để xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickUpdateService() {
        String tenChucVuMoi = edtTenChucVu.getText().toString().trim();
        if (!TextUtils.isEmpty(tenChucVuMoi)) {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                serviceViewModel.updateService(adapter.getServiceList().get(selectedPosition), tenChucVuMoi)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(requireContext(), "Cập nhật chức vụ thành công", Toast.LENGTH_SHORT).show();
                            serviceViewModel.getServices().observe(getViewLifecycleOwner(), services -> {
                                adapter.setServiceList(services);
                            });
                            edtTenChucVu.setText("");
                            btnSua.setEnabled(false);
                            btnXoa.setEnabled(false);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Cập nhật chức vụ thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(requireContext(), "Vui lòng chọn một chức vụ để cập nhật", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập tên chức vụ mới", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickAddService() {
        String tenChucVu = edtTenChucVu.getText().toString().trim();
        if (!TextUtils.isEmpty(tenChucVu)) {
            serviceViewModel.addService(tenChucVu)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(requireContext(), "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        serviceViewModel.getServices().observe(getViewLifecycleOwner(), services -> {
                            adapter.setServiceList(services);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Thêm dịch vụ thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập tên chức vụ", Toast.LENGTH_SHORT).show();
        }
    }
}
