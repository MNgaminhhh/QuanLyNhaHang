package vn.mn.quanlynhahang.fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Role;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.view.AccountActivity;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class UserDetailFragment extends Fragment {

    private static final int REQUEST_GALLERY_IMAGE = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    private HomeViewModel homeViewModel;
    private ServiceViewModel serviceViewModel;

    private SignUpViewModel signUpViewModel;
    private String phoneNumber;
    private TextView txtInfo;
    private EditText edtFullname, edtPhone, edtBirthday;
    private RadioGroup radioGroup;
    private RadioButton radioMale, radioFemale;
    private Spinner spinnerRole;

    private Button btnXoaAnhDaiDien, btnChupAnhDaiDien, btnCapNhatThongTin, btnXoaTaiKhoan;
    private ImageButton imageButton;

    private String imageUrl;
    private String selectedRole = "";

    public UserDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtInfo = view.findViewById(R.id.txtdangky);
        edtFullname = view.findViewById(R.id.edtCreateFullname);
        edtPhone = view.findViewById(R.id.edtCreatePhone);
        edtBirthday = view.findViewById(R.id.edtDateBirthday);
        radioGroup = view.findViewById(R.id.radioGender);
        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        btnXoaAnhDaiDien = view.findViewById(R.id.btnXoaAnhDaiDien);
        btnChupAnhDaiDien = view.findViewById(R.id.btnChupAnhDaiDien);
        btnCapNhatThongTin = view.findViewById(R.id.btnCapNhatThongTin);
        btnXoaTaiKhoan = view.findViewById(R.id.btnDeleteAccount);
        imageButton = view.findViewById(R.id.imgbtnAvatar);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        serviceViewModel = new ViewModelProvider(requireActivity()).get(ServiceViewModel.class);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        Bundle extras = getArguments();
        if (extras != null) {
            phoneNumber = extras.getString("userId");
        }
        homeViewModel.getUserByPhone(phoneNumber).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                displayUserInfo(user);
            } else {
                Toast.makeText(requireContext(), "Không tìm thấy người dùng !", Toast.LENGTH_SHORT).show();
            }
        });
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadServices();
        edtBirthday.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        btnXoaAnhDaiDien.setOnClickListener(v -> {
            clickDeleteAvatar();
        });
        btnChupAnhDaiDien.setOnClickListener(v -> {
            clickAddCameraAvatar();
        });
        imageButton.setOnClickListener(v -> {
            clickAddAvatar();
        });
        btnCapNhatThongTin.setOnClickListener(v -> {
            clickUpdateUser();
        });
        btnXoaTaiKhoan.setOnClickListener(v -> {
            clickDeleteUser();
        });
    }

    private void clickDeleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận xóa tài khoản");
        builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            deleteUserFromDatabase();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteUserFromDatabase() {
        homeViewModel.deleteUserByPhone(phoneNumber).observe(getViewLifecycleOwner(), deleteResult -> {
            if (deleteResult) {
                Toast.makeText(requireContext(), "Xóa người dùng thành công!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            } else {
                Toast.makeText(requireContext(), "Xóa người dùng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_IMAGE && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                uploadImageToFirebaseStorage(selectedImageUri);
            } else if (requestCode == REQUEST_CAPTURE_IMAGE && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = saveImageToGallery(photo);
                uploadImageToFirebaseStorage(imageUri);
            }
        }
    }

    private Uri saveImageToGallery(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        signUpViewModel.uploadImageToFirebaseStorage(imageUri).observe(getViewLifecycleOwner(), imageUrl -> {
            if (imageUrl != null) {
                this.imageUrl = imageUrl;
                Glide.with(requireContext())
                        .load(imageUri)
                        .into(imageButton);
                Toast.makeText(requireContext(), "Upload ảnh thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Lỗi khi upload ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickAddCameraAvatar() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }

    private void clickAddAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    private void clickDeleteAvatar() {
        imageButton.setImageResource(R.drawable.baseline_add_circle_24);
        imageUrl = "";
        Toast.makeText(requireContext(), "Đã xóa ảnh đại diện", Toast.LENGTH_SHORT).show();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> edtBirthday.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    public void loadServices() {
        serviceViewModel.getServices().observe(getViewLifecycleOwner(), roles -> {
            List<String> roleNames = new ArrayList<>();
            for (Role role : roles) {
                roleNames.add(role.getTenChucVu());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, roleNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRole.setAdapter(adapter);
        });
    }

    private void displayUserInfo(User user) {
        txtInfo.setText("Thông tin: " + user.getFullname() + "");
        edtFullname.setText(user.getFullname());
        edtPhone.setText(user.getPhone());
        edtBirthday.setText(user.getBirthdate());
        if (user.getGender().equals("Nam")) {
            radioMale.setChecked(true);
        } else {
            radioFemale.setChecked(true);
        }
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            String userRole = user.getRole();
            SpinnerAdapter adapter = spinnerRole.getAdapter();
            if (adapter != null) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (userRole.equals(adapter.getItem(i))) {
                        spinnerRole.setSelection(i);
                        break;
                    }
                }
            }
        }


        if (user.getAvatarurl() != null && !user.getAvatarurl().isEmpty()) {
            imageUrl = user.getAvatarurl();
            Glide.with(requireContext()).load(user.getAvatarurl()).into(imageButton);
        }
    }

    private void clickUpdateUser() {
        String fullname = edtFullname.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        String gender = radioGroup.getCheckedRadioButtonId() == R.id.radioMale ? "Nam" : "Nữ";
        String role = selectedRole.trim();
        User updatedUser = new User(imageUrl, phoneNumber, fullname, birthday, role, gender);
        homeViewModel.updateUser(updatedUser).observe(getViewLifecycleOwner(), updateResult -> {
            if (updateResult) {
                Toast.makeText(requireContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                displayUserInfo(updatedUser);
            } else {
                Toast.makeText(requireContext(), "Cập nhật thông tin thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
