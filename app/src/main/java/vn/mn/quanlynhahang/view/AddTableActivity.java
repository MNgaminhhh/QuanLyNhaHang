package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;

public class AddTableActivity extends AppCompatActivity {
//    EditText edtTableID;
//    RadioGroup radioGroup;
//    TextView txtNoti;
//    Button btnCancel;
//    Button btnAdd;
//    Table newTable = new Table();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_table);
//
//        edtTableID = this.findViewById(R.id.edtTableID);
//        radioGroup = this.findViewById(R.id.radioNumofDiner);
//        txtNoti = this.findViewById(R.id.txtNoti);
//        btnCancel = this.findViewById(R.id.btnCancel);
//        btnAdd = this.findViewById(R.id.btnAddNewTB);
//
//        newTable.setId(-1);
//        newTable.setNumberOfDiner(2);
//
//        TableDB tableDB = new TableDB(this);
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.radioNumofDiner2){
//                    newTable.setNumberOfDiner(2);
//                }
//                if (checkedId == R.id.radioNumofDiner4){
//                    newTable.setNumberOfDiner(4);
//                }
//                if (checkedId == R.id.radioNumofDiner6){
//                    newTable.setNumberOfDiner(6);
//                }
//                if (checkedId == R.id.radioNumofDiner8){
//                    newTable.setNumberOfDiner(8);
//                }
//
//            }
//        });
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int value = -1;
//                if (edtTableID.getText().toString().trim().equals("")){
//                    txtNoti.setText("Vui lòng điền mã số bàn!");
//                }
//                try {
//                    value = Integer.parseInt(edtTableID.getText().toString());
//                } catch (NumberFormatException e){
//                    txtNoti.setText("Mã số bàn chỉ bao gồm kí tự số!");
//                }
//                int finalValue = value;
//                boolean isExist = TableManageActivity.tableList.getValue().stream()
//                        .map(Table::getId)
//                        .anyMatch(id -> id.equals(finalValue));
//                if (isExist){
//                    txtNoti.setText("Mã số bàn đã tồn tại!");
//                }
//                else {
//                    newTable.setId(value);
//                    txtNoti.setText("");
//                }
//                if (newTable.getId()==-1){
//                    return;
//                }
//                TableManageActivity.tableList.getValue().add(newTable);
//                tableDB.addNewTable(newTable);
//                Intent intent = new Intent(AddTableActivity.this, TableManageActivity.class);
//                startActivity(intent);
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AddTableActivity.this, TableManageActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}