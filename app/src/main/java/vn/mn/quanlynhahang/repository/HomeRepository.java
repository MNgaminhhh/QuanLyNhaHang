package vn.mn.quanlynhahang.repository;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
                //error
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
    public LiveData<Boolean> updateCurrentUser(User updatedUser) {
        MutableLiveData<Boolean> updateResultLiveData = new MutableLiveData<>();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference userRef = firestore.collection("users").document(userId);

            Map<String, Object> data = new HashMap<>();
            data.put("fullname", updatedUser.getFullname());
            data.put("birthdate", updatedUser.getBirthdate());
            data.put("phone", updatedUser.getPhone());
            data.put("gender", updatedUser.getGender());
            data.put("role", updatedUser.getRole());
            data.put("avatarurl", updatedUser.getAvatarurl());

            userRef.update(data)
                    .addOnSuccessListener(aVoid -> {
                        updateResultLiveData.setValue(true);
                    })
                    .addOnFailureListener(e -> {
                        updateResultLiveData.setValue(false);
                    });
        } else {
            updateResultLiveData.setValue(false);
        }

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
        Log.e("EEEEEEEEEEX", userId);
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
        Log.e("EEEEEEEEEE", uid);
        UserService.userservice.deleteUserUid(uid).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                deleteResultLiveData.setValue(true);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                deleteResultLiveData.setValue(false);
            }
        });
    }

    public LiveData<Boolean> deleteUserData() {
        MutableLiveData<Boolean> deleteUserDataLiveData = new MutableLiveData<>();
        String user = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        firestore.collection("users").document(user).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseAuth.getCurrentUser().delete().addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful()) {
                        deleteUserByCurrentUser(deleteUserDataLiveData);
                    } else {
                        deleteUserDataLiveData.setValue(false);
                    }
                });
            } else {
                deleteUserDataLiveData.setValue(false);
            }
        }).addOnFailureListener(e -> {
            deleteUserDataLiveData.setValue(false);
        });
        return deleteUserDataLiveData;
    }
    public void deleteUserByCurrentUser(MutableLiveData<Boolean> deleteLiveData) {
        Objects.requireNonNull(firebaseAuth.getCurrentUser()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deleteLiveData.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                deleteLiveData.setValue(false);
            }
        });
    }
    //        public void deleteUserByUid(String uid, MutableLiveData<Boolean> deleteResultLiveData) {
//        deleteResultLiveData.setValue(true);
//    }
    public void signOutUser() {
        firebaseAuth.signOut();
    }
}
