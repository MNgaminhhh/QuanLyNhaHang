package vn.mn.quanlynhahang.repository;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServiceRepository {
    private FirebaseFirestore db;
    private CollectionReference serviceCollection;
    public ServiceRepository() {
        db = FirebaseFirestore.getInstance();
        serviceCollection = db.collection("services");
    }

    public Task<DocumentReference> addService(String service) {
        Map<String, Object> data = new HashMap<>();
        data.put("tenChucVu", service);
        return serviceCollection.add(data);
    }
    public LiveData<List<String>> getServices() {
        MutableLiveData<List<String>> servicesLiveData = new MutableLiveData<>();
        db.collection("services")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> services = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String serviceName = documentSnapshot.getString("tenChucVu");
                        services.add(serviceName);
                    }
                    servicesLiveData.setValue(services);
                })
                .addOnFailureListener(e -> {

                });
        return servicesLiveData;
    }

}