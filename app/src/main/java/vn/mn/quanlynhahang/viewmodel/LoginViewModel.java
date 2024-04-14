package vn.mn.quanlynhahang.viewmodel;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import vn.mn.quanlynhahang.model.UserRepository;

public class LoginViewModel extends ViewModel {
    private UserRepository userRepository;

    public LoginViewModel() {
        userRepository = new UserRepository();
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return userRepository.signInWithEmailAndPassword(email, password);
    }
}