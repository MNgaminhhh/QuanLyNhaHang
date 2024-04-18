package vn.mn.quanlynhahang.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity {
    private TextView txtUserDetails;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtUserDetails = findViewById(R.id.txtUserDetails);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        homeViewModel.getCurrentUser().observe(this, firebaseUser -> {
            if (firebaseUser != null) {
                homeViewModel.getUserData(firebaseUser.getUid()).observe(HomeActivity.this, user -> {
                    if (user != null) {
                        String userDetails = "Xin chào, " + user.getFullname();
                        txtUserDetails.setText(userDetails);
                    } else {
                        txtUserDetails.setText(".....");
                    }
                });
            }
        });
    }
}
