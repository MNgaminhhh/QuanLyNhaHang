package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;

import java.util.ArrayList;

public class AddTableFragment extends Fragment {

    EditText edtTableID;
    RadioGroup radioGroup;
    TextView txtNoti;
    Button btnCancel;
    Button btnAdd;
    Table newTable = new Table();
    ArrayList<Table> tableList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtTableID = view.findViewById(R.id.edtTableID);
        radioGroup = view.findViewById(R.id.radioNumofDiner);
        txtNoti = view.findViewById(R.id.txtNoti);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnAdd = view.findViewById(R.id.btnAddNewTB);

        newTable.setId(-1);
        newTable.setNumberOfDiner(2);

        TableDB tableDB = new TableDB(requireContext(), null);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioNumofDiner2){
                newTable.setNumberOfDiner(2);
            }
            if (checkedId == R.id.radioNumofDiner4){
                newTable.setNumberOfDiner(4);
            }
            if (checkedId == R.id.radioNumofDiner6){
                newTable.setNumberOfDiner(6);
            }
            if (checkedId == R.id.radioNumofDiner8){
                newTable.setNumberOfDiner(8);
            }
        });

        btnAdd.setOnClickListener(v -> {
            int value = -1;
            if (edtTableID.getText().toString().trim().equals("")) {
                txtNoti.setText("Vui lòng điền mã số bàn!");
            }
            int finalValue = value;
            boolean isExist = TableManageFragment.tableList.getValue().stream()
                        .map(Table::getId)
                        .anyMatch(id -> id.equals(finalValue));
            if (isExist) {
                txtNoti.setText("Mã số bàn đã tồn tại!");
            } else {
                newTable.setId(value);
                txtNoti.setText("");
            }
            if (newTable.getId() == -1) {
                return;
            }
            tableDB.addNewTable(newTable);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

    }
}
