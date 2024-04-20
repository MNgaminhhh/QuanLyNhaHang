package vn.mn.quanlynhahang.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.repository.SignUpRepository;

public class SignUpViewModel extends ViewModel {
    private SignUpRepository signUpRepository;

    public SignUpViewModel() {
        signUpRepository = new SignUpRepository();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return signUpRepository.createUserWithEmailAndPassword(email, password);
    }
    public Task<Void> saveUserInfoToFirestore(User user, String userId) {
        return signUpRepository.saveUserInfoToFirestore(user, userId);
    }
    public LiveData<String> uploadImageToFirebaseStorage(Uri imageUri) {
        return signUpRepository.uploadImageToFirebaseStorage(imageUri);
    }
}