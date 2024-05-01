package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.AddNotificationAdapter;
import vn.mn.quanlynhahang.adapter.NotificationAdapter;
import vn.mn.quanlynhahang.model.NotifUser;
import vn.mn.quanlynhahang.model.NotificationD;
import vn.mn.quanlynhahang.model.NotificationData;
import vn.mn.quanlynhahang.model.NotificationRequestBody;
import vn.mn.quanlynhahang.model.Role;
import vn.mn.quanlynhahang.model.UserUid;
import vn.mn.quanlynhahang.repository.FCMService;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;
import vn.mn.quanlynhahang.viewmodel.LoginViewModel;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;

public class AddNotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnThongBao;
    private AddNotificationAdapter adapter;
    private List<UserUid> userUidList;
    private HomeViewModel homeViewModel;
    private EditText edtThongBao;
    private ServiceViewModel serviceViewModel;
    private Spinner spinnerRole;
    private String userId, fullname;
    private CardView cvLocQuyen;
    private List<UserUid> allUserList = new ArrayList<>();
    private List<UserUid> displayUserList = new ArrayList<>();
    private LoginViewModel loginViewModel;
    private static final String TAG = "AddNotificationFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_notification, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvUserItemNotif);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        cvLocQuyen = view.findViewById(R.id.cvLocQuyen);
        btnThongBao = view.findViewById(R.id.btnThongBao);
        edtThongBao = view.findViewById(R.id.edtThongBao);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        serviceViewModel = new ViewModelProvider(requireActivity()).get(ServiceViewModel.class);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        userId = HomeFragment.userid;
        userUidList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
        adapter = new AddNotificationAdapter(requireContext(), userUidList);
        recyclerView.setAdapter(adapter);

        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                homeViewModel.getUserData(firebaseUser.getUid()).observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        fullname = user.getFullname();
                        loadNotifiUser();
                    }
                });
            }
        });
        loadServices();
        cvLocQuyen.setOnClickListener(v -> {
            String selectedRole = spinnerRole.getSelectedItem().toString();
            filterUsersByRole(selectedRole);
        });

        btnThongBao.setOnClickListener(v -> {
            List<String> selectedUIDs = adapter.getSelectedUIDs();
            if (!selectedUIDs.isEmpty()) {
                AtomicInteger successfulCount = new AtomicInteger(0);

                for (String uid : selectedUIDs) {
                    String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                    NotifUser user = new NotifUser(uid, edtThongBao.getText().toString().trim(), userId, fullname, currentTimeMillis);

                    homeViewModel.addNotification(user).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sendNotificationToSelectedUsers(user);
                            successfulCount.getAndIncrement();
                            if (successfulCount.get() == selectedUIDs.size()) {
                                showSnackbar("Thông báo đã được gửi thành công");
                            }
                        } else {
                            Toast.makeText(requireContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                showSnackbar("Không có item nào được chọn");
            }
        });




    }
    private void showSnackbar(String message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
    }
    private void sendNotificationToSelectedUsers(NotifUser user) {
        loginViewModel.getUserTokenFromDatabase(user.getUserUid()).observe(getViewLifecycleOwner(), token -> {
            if (token != null) {
                NotificationData notificationData = new NotificationData();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                String formattedTime = sdf.format(new Date(Long.parseLong(user.getTimeSent())));
                notificationData.setTitle("Từ: " + user.getSenderName() + ", " + formattedTime);
                notificationData.setBody(user.getNotificationContent());
                NotificationD notificationD = new NotificationD();
                notificationD.setUserId(userId);
                NotificationRequestBody requestBody = new NotificationRequestBody();
                requestBody.setTo(token);
                requestBody.setNotification(notificationData);
                requestBody.setData(notificationD);
                FCMService.fcmService.sendNotification(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.e(TAG, "done");
                        Log.e(TAG, token);
                        Log.e(TAG, requestBody.toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        Log.e(TAG, "fail");
                    }
                });
            } else {
                showSnackbar("Vài tài khoản chưa đăng nhập vào ứng dụng");
            }
        });
    }

    private void loadNotifiUser() {
        homeViewModel.getUserRole().observe(getViewLifecycleOwner(), userList -> {
            if (userList != null) {
                allUserList.clear();
                allUserList.addAll(userList);
                filterUsersByRole(spinnerRole.getSelectedItem().toString());
            }
        });
    }

    private void filterUsersByRole(String role) {
        displayUserList.clear();
        if (role != null) {
            if (role.equals("Tất cả")) {
                displayUserList.addAll(allUserList);
            } else {
                for (UserUid user : allUserList) {
                    if (user.getRole() != null && user.getRole().equals(role) && !user.getUserId().equals(userId)) {
                        displayUserList.add(user);
                    }
                }
            }
            adapter.updateList(displayUserList);
        } else {

        }
    }
    public void loadServices() {
        serviceViewModel.getServices().observe(getViewLifecycleOwner(), roles -> {
            List<String> roleNames = new ArrayList<>();
            roleNames.add("Tất cả");
            for (Role role : roles) {
                roleNames.add(role.getTenChucVu());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, roleNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRole.setAdapter(adapter);
        });
    }

}