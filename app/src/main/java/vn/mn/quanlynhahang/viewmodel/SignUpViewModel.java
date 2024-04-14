package vn.mn.quanlynhahang.viewmodel;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import vn.mn.quanlynhahang.model.SignUpRepository;

public class SignUpViewModel extends ViewModel {
    private SignUpRepository signUpRepository;

    public SignUpViewModel() {
        signUpRepository = new SignUpRepository();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return signUpRepository.createUserWithEmailAndPassword(email, password);
    }
}