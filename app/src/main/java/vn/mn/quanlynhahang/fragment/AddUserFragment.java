package vn.mn.quanlynhahang.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Role;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.model.UserCreationRequest;
import vn.mn.quanlynhahang.repository.UserService;
import vn.mn.quanlynhahang.view.AccountActivity;
import vn.mn.quanlynhahang.view.AddUserActivity;
import vn.mn.quanlynhahang.viewmodel.RegisterUserViewModel;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;
import vn.mn.quanlynhahang.viewmodel.SignUpViewModel;

public class AddUserFragment extends Fragment {

    private static final int REQUEST_GALLERY_IMAGE = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 1;

    private EditText edtCreateEmail, edtCreatePassword, edtCreateFullname, edtCreatePhone, edtDateBirthday;
    private RadioGroup radioGender;
    private Spinner spinnerRole;
    private Button btnSignUp, btnXoaAnhDaiDien, btnChupAnhDaiDien;
    private ImageButton imageButton;
    private String imageUrl = "";
    private ServiceViewModel serviceViewModel;
    private RegisterUserViewModel registerUserViewModel;
    private SignUpViewModel signUpViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtCreateFullname = view.findViewById(R.id.edtCreateFullname);
        edtCreatePassword = view.findViewById(R.id.edtCreatePassword);
        edtCreateEmail = view.findViewById(R.id.edtCreateEmail);
        edtCreatePhone = view.findViewById(R.id.edtCreatePhone);
        edtDateBirthday = view.findViewById(R.id.edtDateBirthday);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        radioGender = view.findViewById(R.id.radioGender);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnXoaAnhDaiDien = view.findViewById(R.id.btnXoaAnhDaiDien);
        btnChupAnhDaiDien = view.findViewById(R.id.btnChupAnhDaiDien);
        imageButton = view.findViewById(R.id.imageButton);

        serviceViewModel = new ViewModelProvider(requireActivity()).get(ServiceViewModel.class);
        registerUserViewModel = new ViewModelProvider(requireActivity()).get(RegisterUserViewModel.class);
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);

        edtDateBirthday.setOnClickListener(v -> showDatePickerDialog());

        btnSignUp.setOnClickListener(v -> clickSignUpUser());

        btnXoaAnhDaiDien.setOnClickListener(v -> clickDeleteAvater());

        btnChupAnhDaiDien.setOnClickListener(v -> clickAddCammeraAvatar());

        imageButton.setOnClickListener(v -> clickAddAvatar());

        loadServices();
    }

    private void clickDeleteAvater() {
        imageButton.setImageResource(R.drawable.baseline_add_circle_24);
        imageUrl = "";
        Toast.makeText(requireContext(), "Đã xóa ảnh đại diện", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_IMAGE && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                uploadImageToFirebaseStorage(selectedImageUri);
            } else if (requestCode == REQUEST_CAPTURE_IMAGE && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = saveImageToGallery(photo);
                uploadImageToFirebaseStorage(imageUri);
            }
        }
    }

    private Uri saveImageToGallery(Bitmap bitmap) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, imageFileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = requireActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream outputStream = requireActivity().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        signUpViewModel.uploadImageToFirebaseStorage(imageUri).observe(getViewLifecycleOwner(), imageUrl -> {
            if (imageUrl != null) {
                this.imageUrl = imageUrl;
                Glide.with(requireContext())
                        .load(imageUri)
                        .into(imageButton);
                Toast.makeText(requireContext(), "Upload ảnh thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Lỗi khi upload ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickAddCammeraAvatar() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }

    private void clickAddAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    public void loadServices() {
        serviceViewModel.getServices().observe(getViewLifecycleOwner(), roles -> {
            List<String> roleNames = new ArrayList<>();
            for (Role role : roles) {
                roleNames.add(role.getTenChucVu());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, roleNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRole.setAdapter(adapter);
        });
    }


    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year1, month1, dayOfMonth) -> edtDateBirthday.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        datePickerDialog.show();
    }

    private void clickSignUpUser() {
        String email = edtCreateEmail.getText().toString().trim();
        String password = edtCreatePassword.getText().toString().trim();
        String fullname = edtCreateFullname.getText().toString().trim();
        String phone = edtCreatePhone.getText().toString().trim();
        String birthday = edtDateBirthday.getText().toString().trim();
        String gender = radioGender.getCheckedRadioButtonId() == R.id.radioMale ? "Nam" : "Nữ";
        String role = spinnerRole.getSelectedItem().toString().trim();
        User userData;
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(fullname) && !TextUtils.isEmpty(phone)
                && !TextUtils.isEmpty(role)
                && !TextUtils.isEmpty(birthday) && radioGender.getCheckedRadioButtonId() != -1) {
            userData = new User(imageUrl , phone, fullname, birthday, role, gender);
            registerUserViewModel.createUserWithEmailPasswordAndData(email, password, userData).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean success) {
                    if (success) {
                        Toast.makeText(requireContext(), "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Đăng ký tài khoản thất bại! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
    }

}
