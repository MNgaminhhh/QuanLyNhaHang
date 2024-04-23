package vn.mn.quanlynhahang.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.TableAdapter;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;

public class TableManageFragment extends Fragment {

    private GridView gridTable;
    private FloatingActionButton btnAddTable;
    public static MutableLiveData<ArrayList<Table>> tableList = new MutableLiveData<>();
    private TableAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridTable = view.findViewById(R.id.gridTable2);
        btnAddTable = view.findViewById(R.id.btnAddTable);

        adapter = new TableAdapter(new ArrayList<>(), getContext());
        gridTable.setAdapter(adapter);
        registerForContextMenu(gridTable);

        tableList.observe(getViewLifecycleOwner(), tables -> {
            adapter.setData(tables);
            adapter.notifyDataSetChanged();
        });

        TableDB tableDB = new TableDB(getContext(), tableList);
        tableDB.getAllTable();

        btnAddTable.setOnClickListener(v -> {
            AddTableFragment addTableFragment = new AddTableFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, addTableFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.table_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final TableDB tableDB = new TableDB(requireContext(), tableList);
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.mnuUpdate) {
            UpdateTableFragment updateTableFragment = new UpdateTableFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", info.position);
            updateTableFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, updateTableFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.mnuDelete) {
            final Table newTable = tableList.getValue().get(info.position);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
            builder1.setTitle("Xóa bàn ăn");
            builder1.setCancelable(false);
            builder1.setMessage("Bạn có chắc chắn muốn xóa bàn này?");
            builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tableDB.deleteTable(newTable.getId() + "");
                    tableList.getValue().remove(info.position);
                    adapter.notifyDataSetChanged();
                }
            });
            builder1.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog1 = builder1.create();
            alertDialog1.show();
        }
        return super.onContextItemSelected(item);
    }
}
