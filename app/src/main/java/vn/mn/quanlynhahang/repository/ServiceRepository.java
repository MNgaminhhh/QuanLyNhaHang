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
    public Task<Role> getRole(String roleName) {
        return roleCollection.whereEqualTo("tenChucVu", roleName)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String tenChucVu = documentSnapshot.getString("tenChucVu");
                            List<String> danhSach = (List<String>) documentSnapshot.get("danhSach");
                            return new Role(tenChucVu, danhSach);
                        }
                    }
                    return null;
                });
    }

}