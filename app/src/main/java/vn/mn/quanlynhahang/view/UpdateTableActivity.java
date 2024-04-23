package vn.mn.quanlynhahang.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;

public class UpdateTableActivity extends AppCompatActivity {
//    EditText edtTableID;
//    RadioGroup radioGroup;
//    RadioButton radioButton2, radioButton4, radioButton6, radioButton8;
//    TextView txtNoti;
//    Button btnCancel;
//    Button btnUpdate;
//    Table newTable = new Table();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_update_table);
//
//        edtTableID = this.findViewById(R.id.edtTableID);
//        radioGroup = this.findViewById(R.id.radioNumofDiner);
//        radioButton2 = this.findViewById(R.id.radioNumofDiner2);
//        radioButton4 = this.findViewById(R.id.radioNumofDiner4);
//        radioButton6 = this.findViewById(R.id.radioNumofDiner6);
//        radioButton8 = this.findViewById(R.id.radioNumofDiner8);
//        txtNoti = this.findViewById(R.id.txtNoti);
//        btnCancel = this.findViewById(R.id.btnCancel);
//        btnUpdate = this.findViewById(R.id.btnAddUpdateTB);
//
//        Intent intent = getIntent();
//        int index = intent.getIntExtra("id", -1);
//        newTable = TableManageActivity.tableList.getValue().get(index);
//        int oldID = newTable.getId();
//        edtTableID.setText(newTable.getId()+"");
//        switch (newTable.getNumberOfDiner()) {
//            case 2:
//                radioButton2.setChecked(true);
//                break;
//            case 4:
//                radioButton4.setChecked(true);
//                break;
//            case 6:
//                radioButton6.setChecked(true);
//                break;
//            case 8:
//                radioButton8.setChecked(true);
//                break;
//        }
//        newTable.setId(-1);
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
//            }
//        });
//
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
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
//                        .anyMatch(id -> (id.equals(finalValue) && !id.equals(oldID)));
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
//                tableDB.updateTable(oldID+"",newTable);
//                TableManageActivity.tableList.getValue().set(index, newTable);
//                Intent intent = new Intent(UpdateTableActivity.this, TableManageActivity.class);
//                startActivity(intent);
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UpdateTableActivity.this, TableManageActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}