package vn.mn.quanlynhahang.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRepository {
    private FirebaseAuth firebaseAuth;

    public LoginRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }
}
