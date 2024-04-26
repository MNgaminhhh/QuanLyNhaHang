package vn.mn.quanlynhahang.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import vn.mn.quanlynhahang.model.Role;
import vn.mn.quanlynhahang.repository.ServiceRepository;

public class ServiceViewModel extends ViewModel {

    private final ServiceRepository repository;

    public ServiceViewModel() {
        repository = new ServiceRepository();
    }

    public LiveData<List<Role>> getServices() {
        return repository.getRoles();
    }


    public Task<DocumentReference> addRole(Role role) {
        return repository.addRole(role);
    }

    public Task<Void> updateRole(String tenChucVu, Role newRole) {
        return repository.updateRole(tenChucVu, newRole);
    }

    public Task<Void> deleteRole(String tenChucVu) {
        return repository.deleteRole(tenChucVu);
    }
    public Task<Role> getRole(String roleName){
        return repository.getRole(roleName);
    }

}
