package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    EditText edtCreatePassword, edtCreateUsername, edtCreateEmail;
    Button btnSignUp;

    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtCreateUsername = (EditText) findViewById(R.id.edtCreateUsername);
        edtCreatePassword = (EditText) findViewById(R.id.edtCreatePassword);
        edtCreateEmail = (EditText) findViewById(R.id.edtCreateEmail);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        btnSignUp.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(edtCreateEmail.getText().toString().trim())
                    && !TextUtils.isEmpty(edtCreatePassword.getText().toString().trim())
                    && !TextUtils.isEmpty(edtCreateUsername.getText().toString().trim())) {
                String email = edtCreateEmail.getText().toString().trim();
                String password = edtCreatePassword.getText().toString().trim();

                signUpViewModel.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thất bại! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}