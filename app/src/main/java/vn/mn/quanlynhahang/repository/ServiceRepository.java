package vn.mn.quanlynhahang.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

import vn.mn.quanlynhahang.model.Role;

public class ServiceRepository {
    private final FirebaseFirestore db;
    private final CollectionReference roleCollection;

    public ServiceRepository() {
        db = FirebaseFirestore.getInstance();
        roleCollection = db.collection("roles");
    }

    public Task<DocumentReference> addRole(Role role) {
        Map<String, Object> data = new HashMap<>();
        data.put("tenChucVu", role.getTenChucVu());
        data.put("danhSach", role.getDanhSach());
        return roleCollection.add(data);
    }


    public LiveData<List<Role>> getRoles() {
        MutableLiveData<List<Role>> rolesLiveData = new MutableLiveData<>();
        roleCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Role> roles = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            String tenChucVu = documentSnapshot.getString("tenChucVu");
                            List<String> danhSach = (List<String>) documentSnapshot.get("danhSach");
                            roles.add(new Role(tenChucVu, danhSach));
                        }
                        rolesLiveData.setValue(roles);
                    } else {
                        // Handle failure
                        rolesLiveData.setValue(null);
                    }
                });
        return rolesLiveData;
    }


    public Task<Void> updateRole(String tenChucVu, Role newRole) {
        Query query = roleCollection.whereEqualTo("tenChucVu", tenChucVu);
        return query.get().onSuccessTask(queryDocumentSnapshots -> {
            List<Task<Void>> updateTasks = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String documentId = documentSnapshot.getId();
                DocumentReference docRef = roleCollection.document(documentId);
                Map<String, Object> data = new HashMap<>();
                data.put("tenChucVu", newRole.getTenChucVu());
                data.put("danhSach", newRole.getDanhSach());
                updateTasks.add(docRef.update(data));
            }
            return Tasks.whenAll(updateTasks);
        });
    }


    public Task<Void> deleteRole(String tenChucVu) {
        Query query = roleCollection.whereEqualTo("tenChucVu", tenChucVu);
        return query.get().onSuccessTask(queryDocumentSnapshots -> {
            List<Task<Void>> deleteTasks = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String roleId = documentSnapshot.getId();
                deleteTasks.add(roleCollection.document(roleId).delete());
            }
            return Tasks.whenAll(deleteTasks);
        });
    }

//    private FirebaseFirestore db;
//    private CollectionReference serviceCollection;
//    public ServiceRepository() {
//        db = FirebaseFirestore.getInstance();
//        serviceCollection = db.collection("services");
//    }
//
//    public Task<DocumentReference> addService(String service) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("tenChucVu", service);
//        return serviceCollection.add(data);
//    }
//    public LiveData<List<String>> getServices() {
//        MutableLiveData<List<String>> servicesLiveData = new MutableLiveData<>();
//        db.collection("services")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<String> services = new ArrayList<>();
//                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        String serviceName = documentSnapshot.getString("tenChucVu");
//                        services.add(serviceName);
//                    }
//                    servicesLiveData.setValue(services);
//                })
//                .addOnFailureListener(e -> {
//
//                });
//        return servicesLiveData;
//    }
//    public Task<QuerySnapshot> updateService(String serviceName, String newService) {
//        Query query = serviceCollection.whereEqualTo("tenChucVu", serviceName);
//
//        return query.get().addOnSuccessListener(queryDocumentSnapshots -> {
//            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                String documentId = documentSnapshot.getId();
//                DocumentReference docRef = serviceCollection.document(documentId);
//                Map<String, Object> data = new HashMap<>();
//                data.put("tenChucVu", newService);
//                docRef.update(data);
//            }
//        });
//    }
//
//    public Task<QuerySnapshot> deleteService(String serviceName) {
//        Query query = serviceCollection.whereEqualTo("tenChucVu", serviceName);
//        return query.get().addOnSuccessListener(queryDocumentSnapshots -> {
//            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                String serviceId = documentSnapshot.getId();
//                serviceCollection.document(serviceId).delete();
//            }
//        });
//    }
//
//    public void saveSelectedItem(String selectedItem) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("tenChucVu", selectedItem);
//        serviceCollection.add(data)
//                .addOnSuccessListener(documentReference -> {
//                })
//                .addOnFailureListener(e -> {
//                });
//    }
}