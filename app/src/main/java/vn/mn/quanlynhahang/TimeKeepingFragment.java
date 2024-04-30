package vn.mn.quanlynhahang;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vn.mn.quanlynhahang.fragment.ScheduleFragment;
import vn.mn.quanlynhahang.model.HttpRequestSender;
import vn.mn.quanlynhahang.model.Schedule;
import vn.mn.quanlynhahang.model.ScheduleDB;
import vn.mn.quanlynhahang.model.TimeInOut;
import vn.mn.quanlynhahang.model.TimeKeeping;
import vn.mn.quanlynhahang.model.TimeKeepingDB;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class TimeKeepingFragment extends Fragment {
    Button btnCheckin;
    TextView txtNoti, txtTimeIn, txtTimeOut;
    ImageView imgPic;
    MutableLiveData<TimeKeeping> timeKeeping = new MutableLiveData<>();
    MutableLiveData<Schedule> schedule = new MutableLiveData<>();
    int idToday;
    boolean isScheduled=true;
    byte[] pic1, pic2;
    public static String url1, url2;
    public static MutableLiveData<Boolean> isMatched = new MutableLiveData<>();
    int REQUEST_CODE_ID=1;
    SignUpViewModel signUpViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_keeping, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCheckin = view.findViewById(R.id.btnCheckin);
        txtNoti = view.findViewById(R.id.txtNoti);
        txtTimeIn = view.findViewById(R.id.txtTimeCheckIn);
        txtTimeOut = view.findViewById(R.id.txtTimeCheckOut);
        imgPic = view.findViewById(R.id.imgPic);

        isMatched.setValue(null);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        ScheduleDB scheduleDB = new ScheduleDB(getContext(), schedule);
        schedule.setValue(new Schedule(ScheduleFragment.user, new ArrayList<>()));
        schedule.observe(getViewLifecycleOwner(), schedules -> {
            if (schedules.getScheduleDay()!=null && schedules.getScheduleDay().size()!=0){
                if (!thisListHaveToDay(schedules.getScheduleDay())){
                    txtNoti.setText("Bạn không có ca làm trong ngày hôm nay!");
                    isScheduled = false;
                } else {
                    setCheckInEvent();
                }
            }
        });
        scheduleDB.getAllSchedule(ScheduleFragment.user);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");



        timeKeeping.setValue(new TimeKeeping());
        timeKeeping.getValue().setUser(ScheduleFragment.user);
        timeKeeping.getValue().setTimeList(new ArrayList<>());

        TimeKeepingDB timeKeepingDB = new TimeKeepingDB(getContext(), timeKeeping);
        timeKeeping.observe(getViewLifecycleOwner(), timeKeeping1 -> {
            idToday = timeKeeping1.getIndexForTimeInOutForToday();
            if (isScheduled){
                if (idToday!=-1){
                    txtTimeIn.setText("Giờ vào làm: "+ sdf.format(timeKeeping1.getTimeList().get(idToday).getTimeIn()));
                    if (timeKeeping1.getTimeList().get(idToday).getTimeOut()!=null){
                        txtTimeOut.setText("Giờ kết thúc: "+ sdf.format(timeKeeping1.getTimeList().get(idToday).getTimeOut()));
                        btnCheckin.setText("Đã kết thúc ca làm việc");
                        btnCheckin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }
                    else {
                        btnCheckin.setText("Kết thúc ca làm");
                        setCheckOutEvent();
                    }
                }
            }
        });
        timeKeepingDB.getTimeKeeping(ScheduleFragment.user);

    }
    public void setCheckInEvent(){
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_ID);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imgPic.setImageBitmap(bitmap);
        Uri imageUri = saveImageToGallery(bitmap);
        sendRecognizationRequest(imageUri);
    }
    public void sendRecognizationRequest(Uri imageUri){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        final StorageReference imageReference = storage.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        signUpViewModel.uploadImageToFirebaseStorage(imageUri).observe(getViewLifecycleOwner(), imageUrl -> {
            if (imageUrl != null){
                url2 = imageUrl;
                url1 = ScheduleFragment.user.getAvatarurl();

                isMatched.observe(getViewLifecycleOwner(), matched ->{
                    if (matched!=null){
                        if (matched){
                            TimeKeepingDB timeKeepingDB = new TimeKeepingDB(getContext(), timeKeeping);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            Date current = new Date();
                            timeKeeping.getValue().getTimeList().add(new TimeInOut(current, null));
                            timeKeepingDB.addTimeKeeping(timeKeeping.getValue());
                            idToday = timeKeeping.getValue().getTimeList().size()-1;
                            txtTimeIn.setText("Giờ vào làm: "+ sdf.format(current));
                            txtNoti.setText("Xác nhận bắt đầu ca làm việc thành công!");
                            btnCheckin.setText("Kết thúc ca làm");
                            setCheckOutEvent();
                        }
                        else {
                            txtNoti.setText("Xác thực thất bại, hãy thử lại sau");
                        }
                    }
                    else{
                        txtNoti.setText("Đang xác thực...");
                    }
                });

                new HttpRequestSender().execute();
            }
        });
    }
    private Uri saveImageToGallery(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    public void setCheckOutEvent(){
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeKeepingDB timeKeepingDB = new TimeKeepingDB(getContext(), timeKeeping);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                Date current = new Date();
                timeKeeping.getValue().getTimeList().get(idToday).setTimeOut(current);
                timeKeepingDB.addTimeKeeping(timeKeeping.getValue());
                txtTimeOut.setText("Giờ kết thúc: "+ sdf.format(current));
                txtNoti.setText("Xác nhận kết thúc ca làm việc thành công!");
                btnCheckin.setText("Đã kết thúc ca làm việc");
                btnCheckin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
    }
    public boolean thisListHaveToDay(List<Date> listDate) {
        Date today = new Date();
        for (int i = 0; i < listDate.size(); i++) {
            Date timeIn = listDate.get(i);
            if (timeIn.getYear() == today.getYear() &&
                    timeIn.getMonth() == today.getMonth() &&
                    timeIn.getDate() == today.getDate()) {
                return true;
            }
        }
        return false;
    }
//    public void sendHttpRequest(String url1, String url2){
////        Glide.with(getContext())
////                .as(byte[].class)
////                .load(url1).override(255,255).into(new SimpleTarget<byte[]>() {
////                    @Override
////                    public void onResourceReady(@NonNull byte[] resource, @Nullable Transition<? super byte[]> transition) {
////                        pic1 = resource;
////                    }
////                });
////        Glide.with(getContext())
////                .as(byte[].class)
////                .load(url2).override(255,255).into(new SimpleTarget<byte[]>() {
////                    @Override
////                    public void onResourceReady(@NonNull byte[] resource, @Nullable Transition<? super byte[]> transition) {
////                        pic2 = resource;
////                    }
////                });
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api-recognize.azurewebsites.net/api_recognize?key1=https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Fmanage-restaurant-d6e3c.appspot.com%2Fo%2F3x4.jpg%3Falt%3Dmedia%26token%3Df436856b-ac59-4329-9309-c346ebc7d317&key2=https%3A%2F%2Ffirebasestorage.googleapis.com%2Fv0%2Fb%2Fmanage-restaurant-d6e3c.appspot.com%2Fo%2F235639754_356870469360530_5096601794026267342_n.jpg%3Falt%3Dmedia%26token%3D9a562c84-7eeb-417c-be7c-830267c2663c")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            String responseBody = response.body().string();
//            Log.e("Response", responseBody);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}