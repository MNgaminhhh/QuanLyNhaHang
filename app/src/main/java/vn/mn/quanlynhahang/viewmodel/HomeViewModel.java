package vn.mn.quanlynhahang.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.repository.HomeRepository;

public class HomeViewModel extends ViewModel {

    private HomeRepository homeRepository;
    private LiveData<FirebaseUser> currentUser;

    public HomeViewModel() {
        homeRepository = new HomeRepository();
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return homeRepository.getCurrentUser();
    }

    public LiveData<User> getUserData(String userId) {
        return homeRepository.getUserData(userId);
    }

    public LiveData<List<User>> getAllUsers() {
        return homeRepository.getAllUsers();
    }
    public LiveData<User> getUserByPhone(String phoneNumber) {
        return homeRepository.getUserByPhone(phoneNumber);
    }
    public LiveData<Boolean> updateUser(User user) {
        return homeRepository.updateUser(user);
    }
    public LiveData<Boolean> deleteUserByPhone(String phoneNumber) {
        return homeRepository.deleteUserByPhone(phoneNumber);
    }
    public void signOutUser(){
        homeRepository.signOutUser();
    }
}
