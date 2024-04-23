package vn.mn.quanlynhahang.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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

import java.util.ArrayList;

import vn.mn.quanlynhahang.view.TableManageActivity;

public class TableDB {
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("table");
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    MutableLiveData<ArrayList<Table>> tableListLiveData;

    public TableDB(Context context, MutableLiveData<ArrayList<Table>> tableListLiveData) {
        this.context = context;
        this.tableListLiveData = tableListLiveData;
    }

    public void getAllTable(){
        ArrayList<Table> arrayList = new ArrayList<>();
        firestore.collection("table").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                arrayList.add(snapshot.toObject(Table.class));
            }
            tableListLiveData.postValue(arrayList);
        });
    }
    public void addNewTable(Table table){
        DocumentReference documentReference = firestore.collection("table").document(table.id+"");
        documentReference.set(table).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context , "Thêm bàn mới thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context , "Thêm không thành công, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateTable(String tableId, Table updatedTable){
        deleteTable(tableId);
        DocumentReference documentReference = firestore.collection("table").document(updatedTable.id+"");
        documentReference.set(updatedTable).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void deleteTable(String tableId){
        DocumentReference documentReference = firestore.collection("table").document(tableId);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context , "Xóa thất bại, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
