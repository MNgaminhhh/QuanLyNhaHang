package vn.mn.quanlynhahang.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.repository.ServiceRepository;

public class ServiceViewModel extends ViewModel {
    private ServiceRepository serviceRepository;

    public ServiceViewModel() {
        serviceRepository = new ServiceRepository();
    }

    public Task<DocumentReference> addService(String service) {
        return serviceRepository.addService(service);
    }
    public LiveData<List<String>> getServices() {
        return serviceRepository.getServices();
    }



}
