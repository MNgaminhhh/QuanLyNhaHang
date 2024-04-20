package vn.mn.quanlynhahang.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vn.mn.quanlynhahang.model.User;

public class SignUpRepository {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    public SignUpRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }
    public Task<Void> saveUserInfoToFirestore(User user, String userId) {
        return db.collection("users")
                .document(userId)
                .set(user);
    }
    public LiveData<String> uploadImageToFirebaseStorage(Uri imageUri) {
        MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();
        StorageReference storageReference = storage.getReference().child("avatars/" + UUID.randomUUID().toString());
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        imageUrlLiveData.setValue(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    imageUrlLiveData.setValue(null);
                });

        return imageUrlLiveData;
    }
}