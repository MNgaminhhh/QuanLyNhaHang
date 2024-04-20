package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class AddUserActivity extends BaseActivity {
    private EditText edtCreateEmail, edtCreatePassword, edtCreateFullname, edtCreatePhone, edtDateBirthday;
    private RadioGroup radioGender;
    private Spinner spinnerRole;
    private Button btnSignUp;
    private TextView txtdangky;

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
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        edtDateBirthday.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        btnSignUp.setOnClickListener(v ->{
            clickSignUpUser();
        });
        loadServices();
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

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(role)
                && !TextUtils.isEmpty(birthday) && radioGender.getCheckedRadioButtonId() != -1) {
            User user = new User(null , phone, fullname, birthday, role, gender);
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