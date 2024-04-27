package vn.mn.quanlynhahang.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.fragment.DishManageFragment;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.DishDB;

public class AddDishFragment extends Fragment {
    EditText edtDishName, edtPrice;
    Button btnUploadDish, btnAddNewD, btnCancel;
    ImageView picDish;
    Dish dish = new Dish();
    public static int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    Bitmap pic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_dish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtDishName = view.findViewById(R.id.edtDishName);
        edtPrice = view.findViewById(R.id.edtDishPrice);
        btnUploadDish = view.findViewById(R.id.btnUploadDish);
        btnAddNewD = view.findViewById(R.id.btnAddNewD);
        btnCancel = view.findViewById(R.id.btnCancel);
        picDish = view.findViewById(R.id.picDish);

        DishDB dishDB = new DishDB(requireContext(), DishManageFragment.dishList);

        btnUploadDish.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        btnAddNewD.setOnClickListener(v -> {
            dish.setId(DishManageFragment.dishList.getValue().stream()
                    .mapToInt(Dish::getId)
                    .max()
                    .orElse(0) + 1);
            dish.setDishName(edtDishName.getText().toString());
            dish.setPrice(Integer.parseInt(edtPrice.getText().toString()));
            if (dish.getDishName().equals("") || dish.getPrice() == 0 || imageUri == null) {
                Toast.makeText(requireContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            dishDB.addNewDish(imageUri, dish);
            dishDB.getAllDish();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                imageUri = data.getData();
                try {
                    pic = MediaStore.Images.Media.getBitmap(
                            requireContext().getContentResolver(), imageUri
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                picDish.setImageBitmap(pic);
            }
    );
}
