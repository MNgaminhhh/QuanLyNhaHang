package vn.mn.quanlynhahang.view;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.DishAdapter;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.DishDB;

public class DishManageActivity extends AppCompatActivity {
//    ListView lstDish;
//    FloatingActionButton btnAddDish;
//    public static MutableLiveData<ArrayList<Dish>> dishList = new MutableLiveData<>();
//    DishAdapter adapter;
//    public static int PICK_IMAGE_REQUEST = 1;
//    Uri imageUri;
//    Bitmap pic;
//    ImageView picDish;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dish_manage);
//
//        lstDish = findViewById(R.id.lstDish);
//        btnAddDish = findViewById(R.id.btnAddDish);
//        dishList.setValue(new ArrayList<Dish>());
//        dishList.observe(this, new Observer<ArrayList<Dish>>() {
//            @Override
//            public void onChanged(ArrayList<Dish> dishes) {
//                adapter.setData(dishes);
//                Log.e("XXXXXXXXX",dishes.size()+"");
//                adapter.notifyDataSetChanged();
//            }
//        });
//        DishDB dishDB = new DishDB(this);
//        dishDB.getAllDish();
//        adapter = new DishAdapter(this, R.layout.custom_dish_layout, dishList.getValue());
//        lstDish.setAdapter(adapter);
//        registerForContextMenu(lstDish);
//
//        btnAddDish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DishManageActivity.this, AddDishActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.table_menu, menu);
//    }
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item){
//        final DishDB dishDB = new DishDB(this);
//        final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        if (item.getItemId() == R.id.mnuUpdate){
//            Intent intent = new Intent(DishManageActivity.this, UpdateDishActivity.class);
//            intent.putExtra("id", info.position);
//            startActivity(intent);
//        }
//        else if (item.getItemId() == R.id.mnuDelete){
//            final Dish newDish = dishList.getValue().get(info.position);
//            AlertDialog.Builder builder1=new AlertDialog.Builder (DishManageActivity.this);
//            builder1.setTitle("Xóa món ăn");
//            builder1.setCancelable(false);
//            builder1.setMessage("Bạn có chắc chắn muốn xóa món ăn này?");
//            builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dishDB.deleteDish(newDish.getId()+"");
//                }
//            });
//            builder1.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog1 = builder1.create();
//            alertDialog1.show();
//        }
//        return super.onContextItemSelected(item);
//    }
}