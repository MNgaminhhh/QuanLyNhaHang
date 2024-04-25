package vn.mn.quanlynhahang.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.repository.RegisterUserRepository;

public class RegisterUserViewModel extends ViewModel {
    private RegisterUserRepository registerUserRepository;

    public RegisterUserViewModel() {
        registerUserRepository = new RegisterUserRepository();
    }

    public LiveData<Boolean> createUserWithEmailPasswordAndData(String email, String password, User userData) {
        MutableLiveData<Boolean> registrationResult = new MutableLiveData<>();
        registerUserRepository.createUserWithEmailPasswordAndData(email, password, userData, new RegisterUserRepository.Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                registrationResult.setValue(data);
            }

            @Override
            public void onError(Throwable throwable) {
                registrationResult.setValue(false);
            }
        });
        return registrationResult;
    }

}
