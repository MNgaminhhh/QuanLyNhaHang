package vn.mn.quanlynhahang.viewmodel;

import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import vn.mn.quanlynhahang.repository.LoginRepository;

public class LoginViewModel extends ViewModel {
    private LoginRepository userRepository;

    public LoginViewModel() {
        userRepository = new LoginRepository();
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return userRepository.signInWithEmailAndPassword(email, password);
    }
}