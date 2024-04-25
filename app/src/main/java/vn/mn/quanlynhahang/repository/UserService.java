package vn.mn.quanlynhahang.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.mn.quanlynhahang.model.UserCreationRequest;

public interface UserService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    UserService userservice = new Retrofit.Builder()
            .baseUrl("https://www.mn-tech.tech/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(UserService.class);
    @DELETE("api/user")
    Call<String> deleteUserUid(@Query("uid") String uid);
    @POST("api/user")
    Call<String> createUserWithEmailPasswordAndData(@Body UserCreationRequest request);
}
