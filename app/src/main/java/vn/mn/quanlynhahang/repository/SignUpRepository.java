package vn.mn.quanlynhahang.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

import vn.mn.quanlynhahang.model.User;

public class SignUpRepository {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    public SignUpRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }
    public Task<Void> saveUserInfoToFirestore(User user, String userId) {
        return db.collection("users")
                .document(userId)
                .set(user);
    }
}