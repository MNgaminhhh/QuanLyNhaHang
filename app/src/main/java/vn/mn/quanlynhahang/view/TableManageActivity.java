package vn.mn.quanlynhahang.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.TableAdapter;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;

public class TableManageActivity extends AppCompatActivity {
//    GridView gridTable;
//    FloatingActionButton btnAddTable;
//    public static MutableLiveData<ArrayList<Table>> tableList = new MutableLiveData<>();
//    TableAdapter adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_table_manage);
//
//        TableDB tableDB = new TableDB(this);
//        tableList.setValue(new ArrayList<>());
//
//        tableList.observe(this, new Observer<ArrayList<Table>>() {
//            @Override
//            public void onChanged(ArrayList<Table> tables) {
//                adapter.setData(tables);
//                adapter.notifyDataSetChanged();
//            }
//        });
//
//        tableDB.getAllTable();
//        adapter = new TableAdapter(tableList.getValue(), this);
//
//        gridTable = findViewById(R.id.gridTable);
//        btnAddTable = findViewById(R.id.btnAddTable);
//        gridTable.setAdapter(adapter);
//        registerForContextMenu(gridTable);
//
//        btnAddTable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TableManageActivity.this, AddTableActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.table_menu, menu);
//    }
//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item){
//        final TableDB tableDB = new TableDB(this);
//        final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        if (item.getItemId() == R.id.mnuUpdate){
//            Intent intent = new Intent(TableManageActivity.this, UpdateTableActivity.class);
//            intent.putExtra("id", info.position);
//            startActivity(intent);
//        }
//        else if (item.getItemId() == R.id.mnuDelete){
//            final Table newTable = tableList.getValue().get(info.position);
//            AlertDialog.Builder builder1=new AlertDialog.Builder (TableManageActivity.this);
//            builder1.setTitle("Xóa bàn ăn");
//            builder1.setCancelable(false);
//            builder1.setMessage("Bạn có chắc chắn muốn xóa bàn này?");
//            builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    tableDB.deleteTable(newTable.getId()+"");
//                    tableList.getValue().remove(info.position);
//                    adapter.notifyDataSetChanged();
//                }
//            });
//            builder1.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alertDialog1 = builder1.create();
//            alertDialog1.show();
//        }
//        return super.onContextItemSelected(item);
//    }
}