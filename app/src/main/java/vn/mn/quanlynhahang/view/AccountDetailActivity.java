package vn.mn.quanlynhahang.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class AccountDetailActivity extends BaseActivity {
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

    private Button btnXoaAnhDaiDien, btnChupAnhDaiDien, btnCapNhatThongTin, btnXoaTaiKhoan, btnDangXuat;
    private ImageButton imageButton;

    private String imageUrl;
    private String selectedRole = "";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        txtInfo = (TextView) findViewById(R.id.txtdangky);
        edtFullname = (EditText) findViewById(R.id.edtCreateFullname);
        edtPhone = (EditText) findViewById(R.id.edtCreatePhone);
        edtBirthday = (EditText) findViewById(R.id.edtDateBirthday);
        radioGroup = (RadioGroup) findViewById(R.id.radioGender);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        btnXoaAnhDaiDien = (Button) findViewById(R.id.btnXoaAnhDaiDien);
        btnChupAnhDaiDien = (Button) findViewById(R.id.btnChupAnhDaiDien);
        btnCapNhatThongTin = (Button) findViewById(R.id.btnCapNhatThongTin);
        btnXoaTaiKhoan = (Button) findViewById(R.id.btnDeleteAccount);
        btnDangXuat = (Button) findViewById(R.id.btnDangXuat);
        imageButton = (ImageButton) findViewById(R.id.imgbtnAvatar);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        homeViewModel.getCurrentUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();
                homeViewModel.getUserData(userId).observe(this, user -> {
                    if (user != null) {
                        displayUserInfo(user);
                    }
                });
            }
        });

        edtBirthday.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        btnXoaAnhDaiDien.setOnClickListener(v -> {
            clickDeleteAvater();
        });
        btnChupAnhDaiDien.setOnClickListener(v -> {
            clickAddCammeraAvatar();
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
    }

    private void clickSignOut() {
        homeViewModel.signOutUser();
    }

    private void clickDeleteUser() {
    }

    private void clickUpdateUser() {
        String fullname = edtFullname.getText().toString().trim();
        String birthday = edtBirthday.getText().toString().trim();
        String gender = radioGroup.getCheckedRadioButtonId() == R.id.radioMale ? "Nam" : "Nữ";
        String role = selectedRole.trim();
        User updatedUser = new User(imageUrl, phoneNumber, fullname, birthday, role, gender);
        homeViewModel.updateUser(updatedUser).observe(this, updateResult -> {
            if (updateResult) {
                Toast.makeText(AccountDetailActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                displayUserInfo(updatedUser);
            } else {
                Toast.makeText(AccountDetailActivity.this, "Cập nhật thông tin thất bại!", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
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
        signUpViewModel.uploadImageToFirebaseStorage(imageUri).observe(this, imageUrl -> {
            if (imageUrl != null) {
                this.imageUrl = imageUrl;
                Glide.with(AccountDetailActivity.this)
                        .load(imageUri)
                        .into(imageButton);
                Toast.makeText(AccountDetailActivity.this, "Upload ảnh thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AccountDetailActivity.this, "Lỗi khi upload ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clickAddCammeraAvatar() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }

    private void clickAddAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }
    private void clickDeleteAvater() {
        imageButton.setImageResource(R.drawable.baseline_add_circle_24);
        imageUrl = "";
        Toast.makeText(AccountDetailActivity.this, "Đã xóa ảnh đại diện", Toast.LENGTH_SHORT).show();
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AccountDetailActivity.this,
                (view, year1, month1, dayOfMonth) -> edtBirthday.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

}