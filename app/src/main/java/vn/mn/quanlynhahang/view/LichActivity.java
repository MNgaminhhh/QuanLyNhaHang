package vn.mn.quanlynhahang.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import vn.mn.quanlynhahang.R;

public class LichActivity extends AppCompatActivity {
    private EditText searchEditText;
    private TextView employeeDataTextView;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich);

        searchEditText = findViewById(R.id.searchEditText);
        employeeDataTextView = findViewById(R.id.employeeDataTextView);

        db = FirebaseFirestore.getInstance();

        searchEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) &&
                    (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {

                employeeDataTextView.setText("");

                String searchName = searchEditText.getText().toString().trim();

                if (!searchName.isEmpty()) {
                    db.collection("employees")
                            .whereEqualTo("name", searchName)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String name = document.getString("name");
                                        String position = document.getString("position");
                                        String startDay = document.getString("startDate");
                                        String endDay = document.getString("endDate");
                                        String workHours = document.getString("workHours");

                                        String userData = "Tên: " + name + "\n" +
                                                "Vị trí: " + position + "\n" +
                                                "Ngày bắt đầu: " + startDay + "\n" +
                                                "Ngày kết thúc: " + endDay + "\n" +
                                                "Giờ làm việc: " + workHours + "\n\n";
                                        employeeDataTextView.append(userData);
                                    }
                                } else {
                                    employeeDataTextView.setText("Không tìm thấy người dùng nào có tên là " + searchName);
                                }
                            });
                }
                return true;
            }
            return false;
        });
    }
}


