package vn.mn.quanlynhahang.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.OrderDetailAdapter;
import vn.mn.quanlynhahang.model.Order;
import vn.mn.quanlynhahang.model.OrderDB;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class OrderDetailFragment extends Fragment {
    int id;
    Button btnBack, btnEditOrder, btnCancel, btnPaid;
    TextView txtTableId, txtOrderId, txtOrderEmp, txtTimeIn, txtThanhTien;
    ListView lstOrder;
    RadioGroup radPayment;
    RadioButton radCash, radMomo;
    ImageButton btnMomo;
    public static MutableLiveData<ArrayList<Order>> orderList = new MutableLiveData<>();
    Order order = new Order();
    OrderDetailAdapter adapter;
    String paymentMethod = "cash";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        btnEditOrder = view.findViewById(R.id.btnEditOrder);
        txtOrderId = view.findViewById(R.id.txtOrderId);
        txtTableId = view.findViewById(R.id.txtTableId);
        txtTimeIn = view.findViewById(R.id.txtTimeIn);
        txtThanhTien = view.findViewById(R.id.txtThanhTien);
        txtOrderEmp = view.findViewById(R.id.txtOrderEmp);
        lstOrder = view.findViewById(R.id.lstOrder);
        radPayment = view.findViewById(R.id.radPayment);
        radCash = view.findViewById(R.id.radCash);
        radMomo = view.findViewById(R.id.radMomo);
        btnMomo = view.findViewById(R.id.btnMomo);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnPaid = view.findViewById(R.id.btnPaid);


        radPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radMomo){
                    btnMomo.setVisibility(View.VISIBLE);
                    paymentMethod="momo";
                }
                else {
                    btnMomo.setVisibility(View.INVISIBLE);
                    paymentMethod="cash";
                }
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            id = args.getInt("id", -1);}

        order.setOrder(new HashMap<>());
        adapter = new OrderDetailAdapter(getContext(), R.layout.custom_order_list_layout, order);
        lstOrder.setAdapter(adapter);

        orderList.observe(getViewLifecycleOwner(), orders -> {
            for (Order orderValue : orders){
                if (orderValue.getIdTable() == id && !orderValue.isPaymentStatus()){
                    order = orderValue;
                    txtOrderId.setText("Bàn: " + order.getIdTable()+"");
                    txtTableId.setText("Mã hóa đơn: " + order.getId());
                    txtOrderEmp.setText("Nhân viên: "+ order.getOrderEmp());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    txtTimeIn.setText("Ngày tạo: "+sdf.format(order.getTimeOrder()));
                    txtThanhTien.setText(order.totalPrice()+" VNĐ");
                    adapter.setData(order);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        });
        OrderDB orderDB = new OrderDB(getContext(), orderList);
        orderDB.getAllOrder();

        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        btnEditOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDishFragment selectDishFragment = new SelectDishFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("oid", order.getId());
                selectDishFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, selectDishFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                builder1.setTitle("Hủy Order");
                builder1.setCancelable(false);
                builder1.setMessage("Có chắn chắn muốn hủy Order này?");
                builder1.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderDB.deleteOrder(order.getId());
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                builder1.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();



            }
        });
        btnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDB.updateStatus(order.getId(), paymentMethod);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btnMomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("total", order.totalPrice());
                MomoFragment momoFragment = new MomoFragment();
                momoFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, momoFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}