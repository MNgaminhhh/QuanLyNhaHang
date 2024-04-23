package vn.mn.quanlynhahang.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.fragment.DishManageFragment;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.DishDB;
import vn.mn.quanlynhahang.model.TableDB;

public class AddDishActivity extends AppCompatActivity {
//    EditText edtDishName, edtPrice;
//    Button btnUploadDish, btnAddNewD, btnCancel;
//    ImageView picDish;
//    Dish dish = new Dish();
//    public static int PICK_IMAGE_REQUEST = 1;
//    Uri imageUri;
//    Bitmap pic;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_dish);
//
//        edtDishName = this.findViewById(R.id.edtDishName);
//        edtPrice = this.findViewById(R.id.edtDishPrice);
//        btnUploadDish = this.findViewById(R.id.btnUploadDish);
//        btnAddNewD = this.findViewById(R.id.btnAddNewD);
//        btnCancel = this.findViewById(R.id.btnCancel);
//        picDish = this.findViewById(R.id.picDish);
//
//        DishDB dishDB = new DishDB(this);
//
//        btnUploadDish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                launcher.launch(intent);
//            }
//        });
//
//        btnAddNewD.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dish.setId(DishManageFragment.dishList.getValue().stream()
//                        .mapToInt(Dish::getId)
//                        .max()
//                        .orElse(0)+1);
//                dish.setDishName(edtDishName.getText().toString());
//                dish.setPrice(Integer.parseInt(edtPrice.getText().toString()));
//                if (dish.getDishName().equals("") || dish.getPrice()==0 || imageUri==null){
//                    Toast.makeText(AddDishActivity.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                dishDB.addNewDish(imageUri, dish);
//                dishDB.getAllDish();
//                Intent intent = new Intent(AddDishActivity.this, DishManageActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AddDishActivity.this, DishManageActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                Intent data = result.getData();
//                imageUri = data.getData();
//                try {
//                    pic = MediaStore.Images.Media.getBitmap(
//                            getContentResolver(), imageUri
//                    );
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                picDish.setImageBitmap(pic);
//            }
//    );
}