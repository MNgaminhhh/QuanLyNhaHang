package vn.mn.quanlynhahang.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtCreatePassword, edtCreateFullname, edtCreateEmail, edtCreatePhone, edtDateBirthday;
    private RadioGroup radioGender;
    private Button btnSignUp;
    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtCreateFullname = findViewById(R.id.edtCreateFullname);
        edtCreatePassword = findViewById(R.id.edtCreatePassword);
        edtCreateEmail = findViewById(R.id.edtCreateEmail);
        edtCreatePhone = findViewById(R.id.edtCreatePhone);
        edtDateBirthday = findViewById(R.id.edtDateBirthday);
        radioGender = findViewById(R.id.radioGender);
        btnSignUp = findViewById(R.id.btnSignUp);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        edtDateBirthday.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignUpActivity.this,
                    (view, year1, month1, dayOfMonth) -> edtDateBirthday.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                    year, month, day);
            datePickerDialog.show();
        });

        btnSignUp.setOnClickListener(v -> {
            clickSignUpViewModel();
        });
    }

    private void clickSignUpViewModel() {
        if (!TextUtils.isEmpty(edtCreateEmail.getText().toString().trim())
                && !TextUtils.isEmpty(edtCreatePassword.getText().toString().trim())
                && !TextUtils.isEmpty(edtCreateFullname.getText().toString().trim())
                && !TextUtils.isEmpty(edtCreatePhone.getText().toString().trim())
                && !TextUtils.isEmpty(edtDateBirthday.getText().toString().trim())
                && radioGender.getCheckedRadioButtonId() != -1) {
            String email = edtCreateEmail.getText().toString().trim();
            String password = edtCreatePassword.getText().toString().trim();
            String fullname = edtCreateFullname.getText().toString().trim();
            String phone = edtCreatePhone.getText().toString().trim();
            String birthday = edtDateBirthday.getText().toString().trim();
            String gender = radioGender.getCheckedRadioButtonId() == R.id.radioMale ? "Nam" : "Nữ";

            User user = new User(null , phone, fullname, birthday, null, gender);
            signUpViewModel.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            signUpViewModel.saveUserInfoToFirestore(user, userId)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            showToast("Đăng ký tài khoản thành công!");
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            showToast("Lưu thông tin người dùng thất bại!");
                                        }
                                    });
                        } else {
                            showToast("Đăng ký tài khoản thất bại! Vui lòng thử lại sau.");
                        }
                    });
        } else {
            showToast("Vui lòng nhập đầy đủ thông tin!");
        }
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
