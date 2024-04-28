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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.DishAdapter;
import vn.mn.quanlynhahang.model.Dish;
import vn.mn.quanlynhahang.model.DishDB;

public class DishManageFragment extends Fragment {

    private ListView lstDish;
    private FloatingActionButton btnAddDish;
    public static MutableLiveData<ArrayList<Dish>> dishList = new MutableLiveData<>();
    private DishAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dish_manage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstDish = view.findViewById(R.id.lstDish);
        btnAddDish = view.findViewById(R.id.btnAddDish);

        if (dishList.getValue() == null) {
            dishList.setValue(new ArrayList<>());
        }
        adapter = new DishAdapter(requireContext(), R.layout.custom_dish_layout, dishList.getValue());

        lstDish.setAdapter(adapter);
        registerForContextMenu(lstDish);

        dishList.observe(getViewLifecycleOwner(), dishes -> {
            adapter.setData(dishes);
            adapter.notifyDataSetChanged();
        });

        DishDB dishDB = new DishDB(requireContext(), dishList);
        dishDB.getAllDish();

        btnAddDish.setOnClickListener(v -> {
            AddDishFragment addDishFragment = new AddDishFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, addDishFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.dish_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final DishDB dishDB = new DishDB(requireContext(), dishList);
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.mnuUpdate) {
            UpdateDishFragment updateDishFragment = new UpdateDishFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", info.position);
            updateDishFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, updateDishFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.getItemId() == R.id.mnuDelete) {
            final Dish newDish = dishList.getValue().get(info.position);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
            builder1.setTitle("Xóa món ăn");
            builder1.setCancelable(false);
            builder1.setMessage("Bạn có chắc chắn muốn xóa món ăn này?");
            builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dishDB.deleteDish(String.valueOf(newDish.getId()));
                    dishList.getValue().remove(info.position);
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
        } else if (item.getItemId() == R.id.mnuState) {
            final Dish newDish = dishList.getValue().get(info.position);
            newDish.changStocksStatus();
            dishDB.updateDish(newDish.getId()+"", null, newDish);
            dishList.getValue().set(info.position, newDish);
            adapter.notifyDataSetChanged();
            //Toast.makeText(getContext(), "Đổi trạng thái thành công!", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }
}
