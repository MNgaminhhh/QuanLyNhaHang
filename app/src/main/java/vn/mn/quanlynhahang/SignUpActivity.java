package vn.mn.quanlynhahang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    EditText edtCreatePassword, edtCreateUsername, edtCreateEmail;
    Button btnSignUp;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtCreateUsername = (EditText) findViewById(R.id.edtCreateUsername);
        edtCreatePassword = (EditText) findViewById(R.id.edtCreatePassword);
        edtCreateEmail = (EditText) findViewById(R.id.edtCreateEmail);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

            }
        };
        btnSignUp.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(edtCreateEmail.getText().toString().trim())
            && !TextUtils.isEmpty(edtCreatePassword.getText().toString().trim())
            && !TextUtils.isEmpty(edtCreateUsername.getText().toString().trim())){
                String email = edtCreateEmail.getText().toString().trim();
                String password = edtCreatePassword.getText().toString().trim();
                String username = edtCreateUsername.getText().toString().trim();

                createUserEmailAccount(email, password, username);
            }else {
                Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createUserEmailAccount(String email, String password, String username){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thất bại! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}