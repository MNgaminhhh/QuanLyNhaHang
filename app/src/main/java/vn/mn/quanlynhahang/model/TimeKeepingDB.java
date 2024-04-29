package vn.mn.quanlynhahang.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TimeKeepingDB {
    Context context;
    MutableLiveData<TimeKeeping> timeKeeping;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public TimeKeepingDB(Context context, MutableLiveData<TimeKeeping> timeKeeping) {
        this.context = context;
        this.timeKeeping = timeKeeping;
    }

    public void getTimeKeeping(User user){
        firestore.collection("timekeeping").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                    TimeKeeping dbTimeKeeping = snapshot.toObject(TimeKeeping.class);
                    if (dbTimeKeeping.getUser().getPhone().equals(user.getPhone())) {
                        timeKeeping.setValue(dbTimeKeeping);
                        return;
                    }
                }
            }
        });
    }
    public void addTimeKeeping(TimeKeeping timeKeeping){
        DocumentReference documentReference = firestore.collection("timekeeping").document(timeKeeping.getUser().getPhone());
        documentReference.set(timeKeeping).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context , "Chấm công thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context , "Chấm công không thành công, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
