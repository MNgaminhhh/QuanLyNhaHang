package vn.mn.quanlynhahang.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vn.mn.quanlynhahang.model.NotificationRequestBody;
import vn.mn.quanlynhahang.model.UserCreationRequest;

public interface FCMService {

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    FCMService fcmService = new Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FCMService.class);

    @Headers({"Authorization: key=AAAAXtkHZEg:APA91bFcRNalbmv5syOPBKO68U7Gi2ISlzJJ8abJMA3vNLGcuyub7IfbzDm32sx6E2Ns0JOZ6mG-atXeFb_wqRyFW5O_BvXr5-vnXMaleDBjGbGtBcIbQN4yVWbm9cINn8csT3kYFBsD",
            "Content-Type:application/json"})
    @POST("send")
    Call<ResponseBody> sendNotification(@Body NotificationRequestBody requestBody);
}
