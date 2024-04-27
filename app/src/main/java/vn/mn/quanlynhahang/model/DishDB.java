package vn.mn.quanlynhahang.model;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import vn.mn.quanlynhahang.fragment.DishManageFragment;
import vn.mn.quanlynhahang.view.DishManageActivity;
import vn.mn.quanlynhahang.view.TableManageActivity;

public class DishDB {
    private Context context;
    MutableLiveData<ArrayList<Dish>> dishList;
    public DishDB(Context context, MutableLiveData<ArrayList<Dish>> dishList) {
        this.context = context;
        this.dishList = dishList;
    }

    DatabaseReference database = FirebaseDatabase.getInstance().getReference("dish");
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void getAllDish(){
        ArrayList<Dish> arrayList = new ArrayList<>();
        firestore.collection("dish").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                    arrayList.add(snapshot.toObject(Dish.class));
                }
                dishList.postValue(arrayList);
            }
        });
    }
    public void addNewDish(Uri imageUri, Dish newDish){
        final StorageReference imageReference = storage.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        DocumentReference documentReference = firestore.collection("dish").document(newDish.id+"");
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        newDish.setUrlImage(uri.toString());
                        documentReference.set(newDish).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context , "Thêm món mới thành công!", Toast.LENGTH_SHORT).show();
                                getAllDish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context , "Thêm không thành công, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
    public void updateDish(String dishId, Uri imageUri, Dish updatedDish){
        DocumentReference documentReference = firestore.collection("dish").document(dishId);
        if (imageUri==null)
        {
            documentReference.set(updatedDish).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context , "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context , "Cập nhật không thành công, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            final StorageReference imageReference = storage.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updatedDish.setUrlImage(uri.toString());
                            documentReference.set(updatedDish).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context , "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context , "Cập nhật không thành công, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }
    public void deleteDish(String dishID){
        DocumentReference documentReference = firestore.collection("dish").document(dishID);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context , "Xóa thành công!", Toast.LENGTH_SHORT).show();
                getAllDish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context , "Xóa thất bại, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
