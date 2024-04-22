package vn.mn.quanlynhahang.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.ServiceAdapter;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;

public class ServiceActivity extends BaseActivity {
    private EditText edtTenChucVu;
    private Button btnThem, btnSua, btnXoa;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private ServiceViewModel serviceViewModel;
    private ServiceAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        edtTenChucVu = (EditText) findViewById(R.id.edtThemChucVu);
        btnThem = (Button) findViewById(R.id.btnThem);
        btnSua = (Button) findViewById(R.id.btnSua);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        recyclerView = (RecyclerView) findViewById(R.id.rvItemService);

        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        //button
        btnThem.setOnClickListener(v -> {
            clickAddService();
        });

        //addapter
        adapter = new ServiceAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceViewModel.getServices().observe(this, services -> {
            adapter.setServiceList(services);
        });
        adapter.setOnItemClickListener((position, serviceName) -> {
            selectedPosition = position;
            edtTenChucVu.setText(serviceName);
        });
        btnSua.setOnClickListener(v -> {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                clickUpdateService();
            } else {
                Toast.makeText(this, "Vui lòng chọn một dịch vụ để cập nhật", Toast.LENGTH_SHORT).show();
            }
        });
        btnXoa.setOnClickListener(v -> {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                clickDeleteService();
            } else {
                Toast.makeText(this, "Vui lòng chọn một dịch vụ để xóa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickDeleteService() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            List<String> serviceList = adapter.getServiceList();
            if (selectedPosition < serviceList.size()) {
                String serviceName = serviceList.get(selectedPosition);
                serviceViewModel.deleteService(serviceName)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ServiceActivity.this, "Xóa dịch vụ thành công", Toast.LENGTH_SHORT).show();
                            serviceViewModel.getServices();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ServiceActivity.this, "Xóa dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Vui lòng chọn một dịch vụ để xóa", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn một dịch vụ để xóa", Toast.LENGTH_SHORT).show();
        }
    }



    private void clickUpdateService() {
        String tenChucVuMoi = edtTenChucVu.getText().toString().trim();
        if (!TextUtils.isEmpty(tenChucVuMoi)) {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                serviceViewModel.updateService(selectedPosition, tenChucVuMoi)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ServiceActivity.this, "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
                            serviceViewModel.getServices().observe(this, services -> {
                                adapter.setServiceList(services);
                            });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ServiceActivity.this, "Cập nhật dịch vụ thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Vui lòng chọn một dịch vụ để cập nhật", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Vui lòng nhập tên chức vụ mới", Toast.LENGTH_SHORT).show();
        }
    }


    private void clickAddService() {
        String tenChucVu = edtTenChucVu.getText().toString().trim();
        if (!TextUtils.isEmpty(tenChucVu)) {
            serviceViewModel.addService(tenChucVu)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(ServiceActivity.this, "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();
                        serviceViewModel.getServices().observe(this, services -> {
                            adapter.setServiceList(services);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ServiceActivity.this, "Thêm dịch vụ thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Vui lòng nhập tên chức vụ", Toast.LENGTH_SHORT).show();
        }
    }
}