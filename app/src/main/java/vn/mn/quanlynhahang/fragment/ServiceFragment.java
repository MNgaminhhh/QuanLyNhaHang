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

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.ServiceAdapter;
import vn.mn.quanlynhahang.model.Role;
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
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtTenChucVu = view.findViewById(R.id.edtThemChucVu);
        btnThem = view.findViewById(R.id.btnThem);
        btnSua = view.findViewById(R.id.btnSua);
        btnXoa = view.findViewById(R.id.btnXoa);
        recyclerView = view.findViewById(R.id.rvItemService);

        adapter = new ServiceAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        serviceViewModel.getServices().observe(getViewLifecycleOwner(), roles -> {
            List<Role> filteredRoles = new ArrayList<>();
            for (Role role : roles) {
                if (!TextUtils.equals(role.getTenChucVu(), "admin")) {
                    filteredRoles.add(role);
                }
            }
            adapter.setRoleList(filteredRoles);
        });

        btnThem.setOnClickListener(v -> clickAddRole());
        btnSua.setOnClickListener(v -> clickUpdateRole());
        btnXoa.setOnClickListener(v -> clickDeleteRole());

        adapter.setOnItemClickListener((position, role) -> {
            selectedPosition = position;
            edtTenChucVu.setText(role.getTenChucVu());
            btnSua.setEnabled(true);
            btnXoa.setEnabled(true);
        });

        adapter.setOnItemLongClickListener((position, role) -> {
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", role.getTenChucVu());
            bundle.putInt("position", position);
            detailFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void clickDeleteRole() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            List<Role> roleList = adapter.getRoleList();
            if (selectedPosition < roleList.size()) {
                String tenChucVu = roleList.get(selectedPosition).getTenChucVu();
                serviceViewModel.deleteRole(tenChucVu).addOnSuccessListener(aVoid -> {
                            roleList.remove(selectedPosition);
                            adapter.notifyDataSetChanged();
                            btnSua.setEnabled(false);
                            btnXoa.setEnabled(false);
                            Toast.makeText(requireContext(), "Xóa chức vụ thành công", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Xóa chức vụ thất bại", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(requireContext(), "Vui lòng chọn một chức vụ để xóa", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Vui lòng chọn một chức vụ để xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickUpdateRole() {
        String tenChucVuMoi = edtTenChucVu.getText().toString().trim();
        if (!TextUtils.isEmpty(tenChucVuMoi)) {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                Role newRole = new Role(tenChucVuMoi, new ArrayList<>());
                serviceViewModel.updateRole(adapter.getRoleList().get(selectedPosition).getTenChucVu(), newRole)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(requireContext(), "Cập nhật chức vụ thành công", Toast.LENGTH_SHORT).show();
                            serviceViewModel.getServices().observe(getViewLifecycleOwner(), roles -> {
                                adapter.setRoleList(roles);
                            });
                            edtTenChucVu.setText("");
                            btnSua.setEnabled(false);
                            btnXoa.setEnabled(false);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Cập nhật chức vụ thất bại: ", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(requireContext(), "Vui lòng chọn một chức vụ để cập nhật", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập tên chức vụ mới", Toast.LENGTH_SHORT).show();
        }
    }

    private void clickAddRole() {
        String tenChucVu = edtTenChucVu.getText().toString().trim();
        if (!TextUtils.isEmpty(tenChucVu)) {
            Role newRole = new Role(tenChucVu, new ArrayList<>());
            serviceViewModel.addRole(newRole)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(requireContext(), "Thêm chức vụ thành công", Toast.LENGTH_SHORT).show();
                        serviceViewModel.getServices().observe(getViewLifecycleOwner(), roles -> {
                            adapter.setRoleList(roles);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Thêm chức vụ thất bại!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập tên chức vụ", Toast.LENGTH_SHORT).show();
        }
    }

}
