package vn.mn.quanlynhahang.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class AddUserActivity extends BaseActivity {
    private static final int REQUEST_GALLERY_IMAGE = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 1;
    private EditText edtCreateEmail, edtCreatePassword, edtCreateFullname, edtCreatePhone, edtDateBirthday;
    private RadioGroup radioGender;
    private Uri uploadedImageUri;

    private Spinner spinnerRole;
    private Button btnSignUp, btnXoaAnhDaiDien, btnChupAnhDaiDien;
    private TextView txtdangky;
    private ImageButton imageButton;
    private String imageUrl = "";
    private ServiceViewModel serviceViewModel;
    private SignUpViewModel signUpViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        edtCreateFullname = (EditText) findViewById(R.id.edtCreateFullname);
        edtCreatePassword = (EditText) findViewById(R.id.edtCreatePassword);
        edtCreateEmail = (EditText) findViewById(R.id.edtCreateEmail);
        edtCreatePhone = (EditText) findViewById(R.id.edtCreatePhone);
        edtDateBirthday = (EditText) findViewById(R.id.edtDateBirthday);
        spinnerRole = (Spinner) findViewById(R.id.spinnerRole);
        radioGender = (RadioGroup) findViewById(R.id.radioGender);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnXoaAnhDaiDien = (Button) findViewById(R.id.btnXoaAnhDaiDien);
        btnChupAnhDaiDien = (Button) findViewById(R.id.btnChupAnhDaiDien);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        edtDateBirthday.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        btnSignUp.setOnClickListener(v ->{
            clickSignUpUser();
        });
        loadServices();

        btnXoaAnhDaiDien.setOnClickListener(v -> {
            clickDeleteAvater();
        });
        btnChupAnhDaiDien.setOnClickListener(v -> {
            clickAddCammeraAvatar();
        });
        imageButton.setOnClickListener(v -> {
            clickAddAvatar();
        });
    }

    private void clickDeleteAvater() {
        imageButton.setImageResource(R.drawable.baseline_add_circle_24);
        imageUrl = "";
        Toast.makeText(AddUserActivity.this, "Đã xóa ảnh đại diện", Toast.LENGTH_SHORT).show();
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
                Uri imageUri = getImageUri(photo);
                uploadImageToFirebaseStorage(imageUri);
            }
        }
    }

    private Uri getImageUri(Bitmap inBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inBitmap, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        signUpViewModel.uploadImageToFirebaseStorage(imageUri).observe(this, imageUrl -> {
            if (imageUrl != null) {
                this.imageUrl = imageUrl;
                Glide.with(AddUserActivity.this)
                        .load(imageUri)
                        .into(imageButton);
                Toast.makeText(AddUserActivity.this, "Upload ảnh thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddUserActivity.this, "Lỗi khi upload ảnh!", Toast.LENGTH_SHORT).show();
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

    private void loadServices() {
        serviceViewModel.getServices().observe(this, services -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddUserActivity.this, android.R.layout.simple_spinner_item, services);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRole.setAdapter(adapter);
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddUserActivity.this,
                (view, year1, month1, dayOfMonth) -> edtDateBirthday.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    private void clickSignUpUser() {
        String email = edtCreateEmail.getText().toString().trim();
        String password = edtCreatePassword.getText().toString().trim();
        String fullname = edtCreateFullname.getText().toString().trim();
        String phone = edtCreatePhone.getText().toString().trim();
        String birthday = edtDateBirthday.getText().toString().trim();
        String gender = radioGender.getCheckedRadioButtonId() == R.id.radioMale ? "Nam" : "Nữ";
        String role = spinnerRole.getSelectedItem().toString().trim();
        User user;
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(role)
                && !TextUtils.isEmpty(birthday) && radioGender.getCheckedRadioButtonId() != -1) {
            user = new User(imageUrl , phone, fullname, birthday, role, gender);
            signUpViewModel.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            signUpViewModel.saveUserInfoToFirestore(user, userId)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(AddUserActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddUserActivity.this, AccountActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(AddUserActivity.this, "Lưu thông tin người dùng thất bại!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(AddUserActivity.this, "Đăng ký tài khoản thất bại! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(AddUserActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
    }
}