package vn.mn.quanlynhahang.repository;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.view.AccountActivity;
import vn.mn.quanlynhahang.view.AddUserActivity;

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
    public LiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> mutableLiveData = new MutableLiveData<>();
        CollectionReference usersRef = firestore.collection("users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    userList.add(user);
                }
                mutableLiveData.setValue(userList);
            } else {
                // Handle error
            }
        });
        return mutableLiveData;
    }
    public LiveData<User> getUserByPhone(String phoneNumber) {
        MutableLiveData<User> userData = new MutableLiveData<>();

        CollectionReference usersRef = firestore.collection("users");
        Query query = usersRef.whereEqualTo("phone", phoneNumber);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    userData.setValue(user);
                    break;
                }
            } else {
                userData.setValue(null);
            }
        });

        return userData;
    }
    public LiveData<Boolean> updateUser(User user) {
        MutableLiveData<Boolean> updateResultLiveData = new MutableLiveData<>();
        Query query = firestore.collection("users").whereEqualTo("phone", user.getPhone());

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String documentId = documentSnapshot.getId();
                        DocumentReference userRef = firestore.collection("users").document(documentId);
                        Map<String, Object> data = new HashMap<>();
                        data.put("fullname", user.getFullname());
                        data.put("birthdate", user.getBirthdate());
                        data.put("gender", user.getGender());
                        data.put("role", user.getRole());
                        data.put("avatarurl", user.getAvatarurl());

                        userRef.update(data)
                                .addOnSuccessListener(aVoid -> {
                                    updateResultLiveData.setValue(true);
                                })
                                .addOnFailureListener(e -> {
                                    updateResultLiveData.setValue(false);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    updateResultLiveData.setValue(false);
                });

        return updateResultLiveData;
    }

    public LiveData<Boolean> deleteUserByPhone(String phoneNumber) {
        MutableLiveData<Boolean> deleteResultLiveData = new MutableLiveData<>();

        CollectionReference usersRef = firestore.collection("users");
        Query query = usersRef.whereEqualTo("phone", phoneNumber);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String userId = document.getId();
                    deleteDocumentById(userId, deleteResultLiveData);
                    return;
                }
                deleteResultLiveData.setValue(false);
            } else {
                deleteResultLiveData.setValue(false);
            }
        });

        return deleteResultLiveData;
    }

    private void deleteDocumentById(String userId, MutableLiveData<Boolean> deleteResultLiveData) {
        DocumentReference userRef = firestore.collection("users").document(userId);
        userRef.delete()
                .addOnSuccessListener(aVoid -> {
                    deleteUserByUid(userId, deleteResultLiveData);
                })
                .addOnFailureListener(e -> {
                    deleteResultLiveData.setValue(false);
                });
    }

    public void deleteUserByUid(String uid, MutableLiveData<Boolean> deleteResultLiveData) {
        deleteResultLiveData.setValue(true);
    }
    public void signOutUser() {
        firebaseAuth.signOut();
    }
}
