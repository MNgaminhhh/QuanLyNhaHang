package vn.mn.quanlynhahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, btnSignUp;
    private EditText edtPassword, edtEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignUp = (Button) findViewById(R.id.btnCreateAccount);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtPassword = (EditText) findViewById(R.id.password);
        edtEmail = (EditText) findViewById(R.id.email);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(v -> {
            loginEmailPass(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());
        });

        btnSignUp.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }

    private void loginEmailPass(String trim, String trim1) {
        if(!TextUtils.isEmpty(trim1) && !TextUtils.isEmpty(trim)){
            firebaseAuth.signInWithEmailAndPassword(trim, trim1).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    Intent home = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(home);
                }
            });
        }
    }
}