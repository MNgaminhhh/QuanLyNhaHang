package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnSignUp;
    private EditText edtPassword, edtEmail;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignUp = (Button) findViewById(R.id.btnCreateAccount);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtPassword = (EditText) findViewById(R.id.password);
        edtEmail = (EditText) findViewById(R.id.email);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                loginViewModel.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            FirebaseUser user = authResult.getUser();
                            if (user != null) {
                                Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(home);
                            }
                        });
            }
        });

        btnSignUp.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }
}