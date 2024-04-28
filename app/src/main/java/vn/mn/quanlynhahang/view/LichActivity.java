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

        // Ánh xạ EditText và TextView từ layout
        searchEditText = findViewById(R.id.searchEditText);
        employeeDataTextView = findViewById(R.id.employeeDataTextView);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lắng nghe sự kiện thay đổi trong EditText khi người dùng nhập tên
        searchEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) &&
                    (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                // Xóa nội dung TextView
                employeeDataTextView.setText("");

                // Lấy tên người dùng nhập
                String searchName = searchEditText.getText().toString().trim();

                // Nếu tên không trống, thực hiện truy vấn
                if (!searchName.isEmpty()) {
                    // Truy vấn Firestore để tìm người dùng có tên tương ứng
                    db.collection("employees")
                            .whereEqualTo("name", searchName)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Hiển thị dữ liệu của người dùng
                                        String name = document.getString("name");
                                        String position = document.getString("position");
                                        String startDay = document.getString("startDate");
                                        String endDay = document.getString("endDate");
                                        String workHours = document.getString("workHours");

                                        // Hiển thị thông tin người dùng trên TextView
                                        String userData = "Tên: " + name + "\n" +
                                                "Vị trí: " + position + "\n" +
                                                "Ngày bắt đầu: " + startDay + "\n" +
                                                "Ngày kết thúc: " + endDay + "\n" +
                                                "Giờ làm việc: " + workHours + "\n\n";
                                        employeeDataTextView.append(userData);
                                    }
                                } else {
                                    // Xảy ra lỗi khi truy vấn Firestore
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


