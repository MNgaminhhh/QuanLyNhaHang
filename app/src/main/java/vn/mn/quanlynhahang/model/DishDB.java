package vn.mn.quanlynhahang.model;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class DishDB {
    ArrayList<Dish> dishList;
    private Context context;

    public DishDB(ArrayList<Dish> dishList, Context context) {
        this.dishList = dishList;
        this.context = context;
    }

    DatabaseReference database = FirebaseDatabase.getInstance().getReference("dish");
    StorageReference storage = FirebaseStorage.getInstance().getReference();

    public void getAllDish(DishDataCallback callback){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dishList.add(dataSnapshot.getValue(Dish.class));
                }
                callback.onDishDataLoaded(dishList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addNewDish(Uri imageUri, Dish newDish){
        final StorageReference imageReference = storage.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        newDish.setUrlImage(uri.toString());
                        database.child(newDish.getId()+"").setValue(newDish);
                    }
                });
            }
        });
    }
    public void getImageUrl(int id, DishDataCallback callback){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("EEEEEEEEE","got it");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Dish dish = dataSnapshot.getValue(Dish.class);
                    Log.e("EEEEEEEEE",dish.toString());
                    if (id == dish.getId())
                    {
                        callback.onDishImageUrl(dish.getUrlImage());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
    public void updateDish(String dishId, Uri imageUri, Dish updatedDish){
        if (imageUri==null)
        {
            database.child(dishId).setValue(updatedDish);
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
                            database.child(dishId).setValue(updatedDish);
                        }
                    });
                }
            });
        }
    }
    public void deleteDish(String dishID){
        database.child(dishID).removeValue();
    }
}
