package vn.mn.quanlynhahang.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginRepository {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userTokensRef;

    public LoginRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userTokensRef = FirebaseDatabase.getInstance().getReference("userTokens");
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    public void saveUserTokenToDatabase(String userUid, String userToken) {
        userTokensRef.child(userUid).setValue(userToken);
    }

    public LiveData<String> getUserTokenFromDatabase(String userId) {
        MutableLiveData<String> userTokenLiveData = new MutableLiveData<>();
        DatabaseReference userTokensRef = FirebaseDatabase.getInstance().getReference().child("userTokens").child(userId);
        userTokensRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userToken = dataSnapshot.getValue(String.class);
                    String currentToken = userTokenLiveData.getValue();
                    if (currentToken == null || !currentToken.equals(userToken)) {
                        userTokenLiveData.setValue(userToken);
                        updateTokenInDatabase(userId, userToken);
                    }
                } else {
                    userTokenLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userTokenLiveData.setValue(null);
            }
        });

        return userTokenLiveData;
    }

    private void updateTokenInDatabase(String userId, String newToken) {
        DatabaseReference userTokensRef = FirebaseDatabase.getInstance().getReference().child("userTokens").child(userId);
        userTokensRef.setValue(newToken);
    }


}
