package vn.mn.quanlynhahang.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.adapter.HomeAdapter;
import vn.mn.quanlynhahang.model.ItemHome;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;
import vn.mn.quanlynhahang.viewmodel.ServiceViewModel;

public class HomeFragment extends Fragment {

    private TextView txtUserDetails;
    private ImageView imgAvatarHome;
    private HomeViewModel homeViewModel;
    private List<ItemHome> itemHomeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private String roleUser;
    private ServiceViewModel serviceViewModel;
    public static String currentUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtUserDetails = view.findViewById(R.id.txtUserDetails);
        recyclerView = view.findViewById(R.id.rvItemHome);
        imgAvatarHome = view.findViewById(R.id.imgAvatarHome);
        serviceViewModel = new ViewModelProvider(requireActivity()).get(ServiceViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                homeViewModel.getUserData(firebaseUser.getUid()).observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        Log.e("EEEXXXXXXXX", firebaseUser.getUid());
                        roleUser = user.getRole();
                        createItemHome(roleUser);
                        Log.e("SSSSSSSSSSXX", roleUser);
                        String userDetails = "Xin chào, " + user.getFullname();
                        currentUserName = user.getFullname();
                        txtUserDetails.setText(userDetails);
                        Glide.with(requireContext())
                                .load(user.getAvatarurl())
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.imageerror)
                                .into(imgAvatarHome);
                    } else {
                        txtUserDetails.setText(".....");
                    }
                });
            }
        });

        homeAdapter = new HomeAdapter(requireContext(), itemHomeList);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(homeAdapter);
    }

    private void createItemHome(String roleAccoutUser) {
        itemHomeList.clear();
        itemHomeList.add(new ItemHome(R.drawable.icon_table,"Order", OrderFragment.class));
        serviceViewModel.getRole(roleAccoutUser).addOnSuccessListener(role -> {
            if (role != null) {
                List<String> danhSach = role.getDanhSach();
                for (String item : danhSach) {
                    Log.e("SSSSSSSSSSXXX", item);
                    int image;
                    String titleName;
                    Class<? extends Fragment> fragmentClass;
                    switch (item) {
                        case "AccountFragment":
                            image = R.drawable.icon_staff;
                            titleName = "Nhân Viên";
                            fragmentClass = AccountFragment.class;
                            break;
                        case "DishManageFragment":
                            image = R.drawable.icon_dish;
                            titleName = "Món Ăn";
                            fragmentClass = DishManageFragment.class;
                            break;
                        case "ServiceFragment":
                            image = R.drawable.icon_service;
                            titleName = "Chức Vụ";
                            fragmentClass = ServiceFragment.class;
                            break;
                        case "TableManageFragment":
                            image = R.drawable.icon_table;
                            titleName = "Bàn Ăn";
                            fragmentClass = TableManageFragment.class;
                            break;
                        default:
                            continue;
                    }
                    itemHomeList.add(new ItemHome(image, titleName, fragmentClass));
                }
                homeAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e -> {
            Log.e("SSSSSSSSSSXXX", "error");
        });
    }
}
