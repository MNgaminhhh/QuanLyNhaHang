package vn.mn.quanlynhahang.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.view.LoginActivity;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class AccountDetailFragment extends Fragment {

    private static final int REQUEST_GALLERY_IMAGE = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 1;

    private HomeViewModel homeViewModel;
    private ServiceViewModel serviceViewModel;
    private SignUpViewModel signUpViewModel;
    private TextView txtInfo;
    private EditText edtFullname, edtPhone, edtBirthday;
    private RadioGroup radioGroup;
    private RadioButton radioMale, radioFemale;

    private Button btnXoaAnhDaiDien, btnChupAnhDaiDien, btnCapNhatThongTin, btnXoaTaiKhoan, btnDangXuat;
    private ImageButton imageButton;
    private String selectedRole, phoneNumber, imageUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);
        txtInfo = view.findViewById(R.id.txtdangky);
        edtFullname = view.findViewById(R.id.edtCreateFullname);
        edtPhone = view.findViewById(R.id.edtCreatePhone);
        edtBirthday = view.findViewById(R.id.edtDateBirthday);
        radioGroup = view.findViewById(R.id.radioGender);
        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);
        btnXoaAnhDaiDien = view.findViewById(R.id.btnXoaAnhDaiDien);
        btnChupAnhDaiDien = view.findViewById(R.id.btnChupAnhDaiDien);
        btnCapNhatThongTin = view.findViewById(R.id.btnCapNhatThongTin);
        btnXoaTaiKhoan = view.findViewById(R.id.btnDeleteAccount);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        imageButton = view.findViewById(R.id.imgbtnAvatar);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();
                homeViewModel.getUserData(userId).observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        selectedRole = user.getRole();
                        phoneNumber = user.getPhone();
                        displayUserInfo(user);
                    }
                });
            }
        });

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
        btnDangXuat.setOnClickListener(v -> {
            clickSignOut();
        });

        return view;
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
    private void clickSignOut() {
        homeViewModel.signOutUser();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void clickDeleteUser() {
        homeViewModel.deleteUserData().observe(getViewLifecycleOwner(), deleteuser -> {
            if(deleteuser){
                Toast.makeText(requireContext(), "Xóa tài khoản thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                Toast.makeText(requireContext(), "Xóa tài khoản thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickUpdateUser() {
        String fullname = edtFullname.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        String gender = radioGroup.getCheckedRadioButtonId() == R.id.radioMale ? "Nam" : "Nữ";
        User updatedUser = new User(imageUrl, phoneNumber, fullname, birthday, selectedRole, gender);
        homeViewModel.updateCurrentUser(updatedUser).observe(getViewLifecycleOwner(), updateResult -> {
            if (updateResult) {
                Toast.makeText(requireContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                displayUserInfo(updatedUser);
            } else {
                Toast.makeText(requireContext(), "Cập nhật thông tin thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserInfo(User user) {
        edtFullname.setText(user.getFullname());
        edtPhone.setText(user.getPhone());
        edtBirthday.setText(user.getBirthdate());
        if (user.getGender().equals("Nam")) {
            radioMale.setChecked(true);
        } else {
            radioFemale.setChecked(true);
        }
        if (user.getAvatarurl() != null && !user.getAvatarurl().isEmpty()) {
            imageUrl = user.getAvatarurl();
            Glide.with(this).load(user.getAvatarurl()).into(imageButton);
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
}
