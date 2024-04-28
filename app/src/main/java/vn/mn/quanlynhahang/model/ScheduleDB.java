package vn.mn.quanlynhahang.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ScheduleDB {
    Context context;
    MutableLiveData<Schedule> schedule;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ScheduleDB(Context context, MutableLiveData<Schedule> schedule) {
        this.context = context;
        this.schedule = schedule;
    }

    public void getAllSchedule(User user){
        firestore.collection("schedule").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    Schedule schedules = snapshot.toObject(Schedule.class);
                    if (schedules.getUser().getPhone().equals(user.getPhone())) {
                        schedule.setValue(schedules);
                        return;
                    }
                }
            }
        });
    }
    public void addNewSchedule(Schedule schedule){
        DocumentReference documentReference = firestore.collection("schedule").document(schedule.getUser().getPhone());
        documentReference.set(schedule).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context , "Cập nhật lịch làm việc thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context , "Đăng ký không thành công, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
