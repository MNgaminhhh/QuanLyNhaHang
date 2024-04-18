package vn.mn.quanlynhahang.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import vn.mn.quanlynhahang.model.User;

public class HomeRepository {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    public HomeRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        MutableLiveData<FirebaseUser> currentUserLiveData = new MutableLiveData<>();
        currentUserLiveData.setValue(firebaseAuth.getCurrentUser());
        return currentUserLiveData;
    }

    public LiveData<User> getUserData(String userId) {
        MutableLiveData<User> userDataLiveData = new MutableLiveData<>();
        DocumentReference userRef = firestore.collection("users").document(userId);
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User user = document.toObject(User.class);
                    userDataLiveData.setValue(user);
                } else {
                    userDataLiveData.setValue(null);
                }
            } else {
                userDataLiveData.setValue(null);
            }
        });
        return userDataLiveData;
    }
}
