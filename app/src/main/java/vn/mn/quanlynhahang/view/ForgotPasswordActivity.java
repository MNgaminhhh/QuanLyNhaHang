package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import vn.mn.quanlynhahang.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnResetPassword;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edt_email);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Snackbar.make(v, "Nhập địa chỉ email để đặt lại mật khẩu", Snackbar.LENGTH_SHORT).show();
                return;
            }
            auth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            boolean isNewUser = result.getSignInMethods().isEmpty();
                            if (!isNewUser) {
                                auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Snackbar.make(v, "Chúng tôi đã gửi một email đặt lại mật khẩu đến địa chỉ email của bạn", Snackbar.LENGTH_SHORT).show();
                                                Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Snackbar.make(v, "Không thể gửi email đặt lại mật khẩu! Vui lòng thử lại sau.", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Snackbar.make(v, "Địa chỉ email không tồn tại trong hệ thống. Vui lòng kiểm tra lại!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }
}