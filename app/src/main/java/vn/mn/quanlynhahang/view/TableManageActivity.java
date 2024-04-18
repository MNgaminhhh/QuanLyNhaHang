package vn.mn.quanlynhahang.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.TableAdapter;
import vn.mn.quanlynhahang.model.Table;
import vn.mn.quanlynhahang.model.TableDB;
import vn.mn.quanlynhahang.model.TableDataCallback;

public class TableManageActivity extends AppCompatActivity {
    GridView gridTable;
    FloatingActionButton btnAddTable;
    ArrayList<Table> tableList = new ArrayList<>();
    TableAdapter adapter = new TableAdapter(tableList, this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_manage);

        TableDB tableDB = new TableDB(tableList);
        tableDB.getAllTable(new TableDataCallback() {
            @Override
            public void onTableDataLoaded(ArrayList<Table> table) {
                tableList = table;
                adapter.notifyDataSetChanged();
            }
        });

        gridTable = findViewById(R.id.gridTable);
        btnAddTable = findViewById(R.id.btnAddTable);
        gridTable.setAdapter(adapter);
        registerForContextMenu(gridTable);

        btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Table newTable = new Table();

                AlertDialog.Builder builder = new AlertDialog.Builder(TableManageActivity.this);
                builder.setTitle("Add New Table");
                builder.setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(TableManageActivity.this);
                View view = inflater.inflate(R.layout.layout_add_table, null);
                final RadioGroup radioGroup = view.findViewById(R.id.radioNumofDiner);
                newTable.setNumberOfDiner(2);
                builder.setView(view);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
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

                    }
                });

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newTable.setId(tableList.stream()
                                .mapToInt(Table::getId)
                                .max()
                                .orElse(0)+1);
                        tableList.add(newTable);
                        tableDB.addNewTable(newTable);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.table_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        final TableDB tableDB = new TableDB(tableList);
        final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.mnuUpdate){
            final Table newTable = tableList.get(info.position);

            AlertDialog.Builder builder = new AlertDialog.Builder(TableManageActivity.this);
            builder.setTitle("Update Table");
            builder.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(TableManageActivity.this);
            View view = inflater.inflate(R.layout.layout_add_table, null);
            final RadioGroup radioGroup = view.findViewById(R.id.radioNumofDiner);
            builder.setView(view);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
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
                }
            });

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tableDB.updateTable(newTable.getId()+"",newTable);
                    tableList.set(info.position, newTable);
                    adapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if (item.getItemId() == R.id.mnuDelete){
            final Table newTable = tableList.get(info.position);
            AlertDialog.Builder builder1=new AlertDialog.Builder (TableManageActivity.this);
            builder1.setTitle("Delete Table");
            builder1.setCancelable(false);
            builder1.setMessage("Are you sure!");
            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tableDB.deleteTable(newTable.getId()+"");
                    tableList.remove(info.position);
                    adapter.notifyDataSetChanged();
                }
            });
            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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