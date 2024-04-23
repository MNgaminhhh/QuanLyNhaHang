package vn.mn.quanlynhahang.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Task<QuerySnapshot> updateService(String serviceName, String newService) {
        Query query = serviceCollection.whereEqualTo("tenChucVu", serviceName);

        return query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String documentId = documentSnapshot.getId();
                DocumentReference docRef = serviceCollection.document(documentId);
                Map<String, Object> data = new HashMap<>();
                data.put("tenChucVu", newService);
                docRef.update(data);
            }
        });
    }

    public Task<QuerySnapshot> deleteService(String serviceName) {
        Query query = serviceCollection.whereEqualTo("tenChucVu", serviceName);
        return query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String serviceId = documentSnapshot.getId();
                serviceCollection.document(serviceId).delete();
            }
        });
    }

}