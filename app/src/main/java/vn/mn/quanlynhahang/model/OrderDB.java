package vn.mn.quanlynhahang.model;

import android.content.Context;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDB {
    Context context;
    MutableLiveData<ArrayList<Order>> orderList = new MutableLiveData<>();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("order");

    public OrderDB(Context context, MutableLiveData<ArrayList<Order>> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void getAllOrder(){
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Order> orders = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    orders.add(order);
                }
                orderList.postValue(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addNewOrder(Order order){
        database.child(order.getId()+"").setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context , "Tạo order thành công cho bàn số "+order.getIdTable(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateOrder(int orderID, Map<String, OrderQuantity> dishOrder){
        Map<String, Object> updateValue = new HashMap<>();
        updateValue.put("order", dishOrder);
        database.child(orderID+"").updateChildren(updateValue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Cập nhật Order thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Cập nhật thất bại, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateStatus(int orderID, String method){
        Map<String, Object> updateValue = new HashMap<>();
        updateValue.put("paymentStatus", true);
        updateValue.put("paymentMethod", method);
        database.child(orderID+"").updateChildren(updateValue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Xác nhận thanh toán thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Thanh toán thất bại, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteOrder(int orderID){
        database.child(orderID+"").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Hủy Order thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Hủy Order thất bại, hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
