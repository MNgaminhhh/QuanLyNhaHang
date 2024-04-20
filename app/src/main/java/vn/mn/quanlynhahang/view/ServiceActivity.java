package vn.mn.quanlynhahang.view;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.ServiceAdapter;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;

public class ServiceActivity extends BaseActivity {
    private EditText edtTenChucVu;
    private Button btnThem;

    private ServiceViewModel serviceViewModel;
    private ServiceAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        edtTenChucVu = (EditText) findViewById(R.id.edtThemChucVu);
        btnThem = (Button) findViewById(R.id.btnThem);
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