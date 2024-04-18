package vn.mn.quanlynhahang.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.repository.HomeRepository;

public class HomeViewModel extends ViewModel {

    private HomeRepository homeRepository;
    private LiveData<FirebaseUser> currentUser;

    public HomeViewModel() {
        homeRepository = new HomeRepository();
        currentUser = homeRepository.getCurrentUser();
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<User> getUserData(String userId) {
        return homeRepository.getUserData(userId);
    }
}
