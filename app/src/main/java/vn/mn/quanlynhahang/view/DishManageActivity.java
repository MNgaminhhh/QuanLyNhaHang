package vn.mn.quanlynhahang.view;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.DishAdapter;
import vn.mn.quanlynhahang.adapter.TableAdapter;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.DishDB;
import vn.mn.quanlynhahang.model.DishDataCallback;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;

public class DishManageActivity extends AppCompatActivity {
    ListView lstDish;
    FloatingActionButton btnAddDish;
    ArrayList<Dish> dishList = new ArrayList<>();
    DishAdapter adapter;
    public static int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    Bitmap pic;
    ImageView picDish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_manage);

        lstDish = findViewById(R.id.lstDish);
        btnAddDish = findViewById(R.id.btnAddDish);
        DishDB dishDB = new DishDB(dishList, this);
        adapter = new DishAdapter(this, R.layout.custom_dish_layout, dishList);
        lstDish.setAdapter(adapter);
        registerForContextMenu(lstDish);
        dishDB.getAllDish(new DishDataCallback() {
            @Override
            public void onDishDataLoaded(ArrayList<Dish> dish) {
                dishList = dish;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDishImageUrl(String url) {

            }
        });


        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dish newDish = new Dish();

                AlertDialog.Builder builder = new AlertDialog.Builder(DishManageActivity.this);
                builder.setTitle("Add New Dish");
                builder.setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(DishManageActivity.this);
                View view = inflater.inflate(R.layout.layout_add_dish, null);
                final EditText edtDishName = view.findViewById(R.id.edtDishName);
                final EditText edtPrice = view.findViewById(R.id.edtDishPrice);
                picDish = view.findViewById(R.id.picDish);
                final Button btnUploadDish = view.findViewById(R.id.btnUploadDish);
                builder.setView(view);

                btnUploadDish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        launcher.launch(intent);
                    }
                });
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newDish.setId(dishList.stream()
                                .mapToInt(Dish::getId)
                                .max()
                                .orElse(0)+1);
                        newDish.setDishName(edtDishName.getText().toString());
                        newDish.setPrice(Integer.parseInt(edtPrice.getText().toString()));
                        dishDB.addNewDish(imageUri, newDish);
                        dishDB.getImageUrl(newDish.getId(), new DishDataCallback() {
                            @Override
                            public void onDishDataLoaded(ArrayList<Dish> dishList) {

                            }
                            @Override
                            public void onDishImageUrl(String url) {
                                newDish.setUrlImage(url);
                                dishList.add(newDish);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                imageUri = data.getData();
                try {
                    pic = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                picDish.setImageBitmap(pic);
            }
    );
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.table_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        final DishDB dishDB = new DishDB(dishList, this);
        final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.mnuUpdate){
            final Dish newDish = dishList.get(info.position);

            imageUri =null;
            AlertDialog.Builder builder = new AlertDialog.Builder(DishManageActivity.this);
            builder.setTitle("Update Dish");
            builder.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(DishManageActivity.this);
            View view = inflater.inflate(R.layout.layout_add_dish, null);
            final EditText edtDishName = view.findViewById(R.id.edtDishName);
            final EditText edtPrice = view.findViewById(R.id.edtDishPrice);
            picDish = view.findViewById(R.id.picDish);
            final Button btnUploadDish = view.findViewById(R.id.btnUploadDish);
            builder.setView(view);
            edtDishName.setText(newDish.getDishName());
            edtPrice.setText(newDish.getPrice()+"");
            Glide.with(this).load(newDish.getUrlImage()).into(picDish);
            btnUploadDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    launcher.launch(intent);
                }
            });
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newDish.setDishName(edtDishName.getText().toString());
                    newDish.setPrice(Integer.parseInt(edtPrice.getText().toString()));
                    dishDB.updateDish(newDish.getId()+"", imageUri, newDish);
                    dishDB.getImageUrl(newDish.getId(), new DishDataCallback() {
                        @Override
                        public void onDishDataLoaded(ArrayList<Dish> dishList) {

                        }
                        @Override
                        public void onDishImageUrl(String url) {
                            newDish.setUrlImage(url);
                            dishList.set(info.position, newDish);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if (item.getItemId() == R.id.mnuDelete){
            final Dish newDish = dishList.get(info.position);
            AlertDialog.Builder builder1=new AlertDialog.Builder (DishManageActivity.this);
            builder1.setTitle("Delete Dish");
            builder1.setCancelable(false);
            builder1.setMessage("Are you sure!");
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dishDB.deleteDish(newDish.getId()+"");
                    dishList.remove(info.position);
                    adapter.notifyDataSetChanged();
                }
            });
            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        }
        return super.onContextItemSelected(item);
    }
}