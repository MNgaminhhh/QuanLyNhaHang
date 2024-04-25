package vn.mn.quanlynhahang.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.model.UserCreationRequest;

public class RegisterUserRepository {

    private UserService userService;

    public RegisterUserRepository() {
        userService = UserService.userservice;
    }

    public void createUserWithEmailPasswordAndData(String email, String password, User userData, Callback<Boolean> callback) {
        userService.createUserWithEmailPasswordAndData(new UserCreationRequest(email, password, userData)).enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                callback.onSuccess(true);
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                callback.onSuccess(true);
            }
        });
    }
    public interface Callback<T> {
        void onSuccess(T data);
        void onError(Throwable throwable);
    }

}
