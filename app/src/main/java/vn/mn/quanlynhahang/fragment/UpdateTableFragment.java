package vn.mn.quanlynhahang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;
import vn.mn.quanlynhahang.view.TableManageActivity;

public class UpdateTableFragment extends Fragment {

    EditText edtTableID;
    RadioGroup radioGroup;
    RadioButton radioButton2, radioButton4, radioButton6, radioButton8;
    TextView txtNoti;
    Button btnCancel;
    Button btnUpdate;
    Table newTable = new Table();
    private int index;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_update_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTableID = view.findViewById(R.id.edtTableID);
        radioGroup = view.findViewById(R.id.radioNumofDiner);
        radioButton2 = view.findViewById(R.id.radioNumofDiner2);
        radioButton4 = view.findViewById(R.id.radioNumofDiner4);
        radioButton6 = view.findViewById(R.id.radioNumofDiner6);
        radioButton8 = view.findViewById(R.id.radioNumofDiner8);
        txtNoti = view.findViewById(R.id.txtNoti);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnUpdate = view.findViewById(R.id.btnAddUpdateTB);

        Bundle args = getArguments();
        if (args != null) {
            index = args.getInt("id", -1);
        }
        newTable = TableManageFragment.tableList.getValue().get(index);
        int oldID = newTable.getId();
        edtTableID.setText(String.valueOf(newTable.getId()));
        switch (newTable.getNumberOfDiner()) {
            case 2:
                radioButton2.setChecked(true);
                break;
            case 4:
                radioButton4.setChecked(true);
                break;
            case 6:
                radioButton6.setChecked(true);
                break;
            case 8:
                radioButton8.setChecked(true);
                break;
        }
        newTable.setId(-1);
        TableDB tableDB = new TableDB(requireContext(), null);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioNumofDiner2) {
                newTable.setNumberOfDiner(2);
            }
            if (checkedId == R.id.radioNumofDiner4) {
                newTable.setNumberOfDiner(4);
            }
            if (checkedId == R.id.radioNumofDiner6) {
                newTable.setNumberOfDiner(6);
            }
            if (checkedId == R.id.radioNumofDiner8) {
                newTable.setNumberOfDiner(8);
            }
        });

        btnUpdate.setOnClickListener(v -> {
            String tableIdStr = edtTableID.getText().toString().trim();
            if (tableIdStr.isEmpty()) {
                txtNoti.setText("Vui lòng điền mã số bàn!");
                return;
            }
            int newTableId;
            try {
                newTableId = Integer.parseInt(tableIdStr);
            } catch (NumberFormatException e) {
                txtNoti.setText("Mã số bàn chỉ bao gồm kí tự số!");
                return;
            }
            boolean isExist = TableManageFragment.tableList.getValue().stream()
                    .map(Table::getId)
                    .anyMatch(id -> id.equals(newTableId) && !id.equals(oldID));
            if (isExist) {
                txtNoti.setText("Mã số bàn đã tồn tại!");
                return;
            }
            newTable.setId(newTableId);
            txtNoti.setText("");
            tableDB.updateTable(String.valueOf(oldID), newTable);
            TableManageFragment.tableList.getValue().set(index, newTable);
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        btnCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }
}
