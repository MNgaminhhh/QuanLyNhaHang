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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import vn.mn.quanlynhahang.model.NotifUser;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.model.UserUid;
import vn.mn.quanlynhahang.view.AccountActivity;
import vn.mn.quanlynhahang.view.AddUserActivity;

public class HomeRepository {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private DatabaseReference databaseReference;
    public HomeRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
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


    public LiveData<List<UserUid>> getUserRole() {
        MutableLiveData<List<UserUid>> mutableLiveData = new MutableLiveData<>();
        firestore.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<UserUid> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    UserUid userUid = document.toObject(UserUid.class);
                    userUid.setUserId(document.getId());
                    userList.add(userUid);
                }
                mutableLiveData.setValue(userList);
            } else {
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
    public LiveData<Boolean> deleteNotification(String keyId) {
        MutableLiveData<Boolean> deleteResultLiveData = new MutableLiveData<>();

        DatabaseReference notificationRef = databaseReference.child("notification").child(keyId);

        notificationRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    deleteResultLiveData.setValue(true);
                })
                .addOnFailureListener(e -> {
                    deleteResultLiveData.setValue(false);
                });

        return deleteResultLiveData;
    }


    public LiveData<Boolean> updateNotification(String keyId, String newNotificationContent) {
        MutableLiveData<Boolean> updateResultLiveData = new MutableLiveData<>();

        DatabaseReference notificationRef = databaseReference.child("notification").child(keyId);
        notificationRef.child("notificationContent").setValue(newNotificationContent)
                .addOnSuccessListener(aVoid -> {
                    updateResultLiveData.setValue(true);
                })
                .addOnFailureListener(e -> {
                    updateResultLiveData.setValue(false);
                });

        return updateResultLiveData;
    }



    public MutableLiveData<List<NotifUser>> getNotifications(String userUid) {
        MutableLiveData<List<NotifUser>> mutableLiveData = new MutableLiveData<>();
        DatabaseReference notificationRef = databaseReference.child("notification");
        List<NotifUser> notificationList = new ArrayList<>();
        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotifUser notification = snapshot.getValue(NotifUser.class);
                    if (notification != null && notification.getUserUid().equals(userUid)) {
                        NotifUser userNotification = new NotifUser(notification.getUserUid(), notification.getNotificationContent(),notification.getSenderUid(),
                                notification.getSenderName(), notification.getTimeSent());
                        notificationList.add(userNotification);
                    }
                }
                mutableLiveData.postValue(notificationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return mutableLiveData;
    }


    public LiveData<List<NotifUser>> loadNotificationUser(String userUid) {
        MutableLiveData<List<NotifUser>> notificationLiveData = new MutableLiveData<>();
        DatabaseReference notificationRef = databaseReference.child("notification");

        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<NotifUser> notificationList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotifUser notification = snapshot.getValue(NotifUser.class);
                    if (notification != null && notification.getSenderUid().equals(userUid)) {
                        notificationList.add(notification);
                    }
                }
                notificationLiveData.setValue(notificationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notificationLiveData.setValue(null);
            }
        });

        return notificationLiveData;
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

    public Task<Void> addNotification(NotifUser notifUser) {
        DatabaseReference notificationRef = databaseReference.child("notification");
        String key = notifUser.getTimeSent();
        return notificationRef.child(key).setValue(notifUser);
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
                deleteResultLiveData.setValue(true);
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
