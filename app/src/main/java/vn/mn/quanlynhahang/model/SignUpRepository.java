package vn.mn.quanlynhahang.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpRepository {
    private FirebaseAuth firebaseAuth;

    public SignUpRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }
}