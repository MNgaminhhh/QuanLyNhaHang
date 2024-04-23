package vn.mn.quanlynhahang.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import vn.mn.quanlynhahang.view.AccountActivity;
import vn.mn.quanlynhahang.view.DishManageActivity;
import vn.mn.quanlynhahang.view.ServiceActivity;
import vn.mn.quanlynhahang.view.TableManageActivity;
import vn.mn.quanlynhahang.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment {

    private TextView txtUserDetails;
    private ImageView imgAvatarHome;
    private HomeViewModel homeViewModel;
    private List<ItemHome> itemHomeList;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;

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
        itemHomeList = createItemHome();
        HomeAdapter adapter = new HomeAdapter(requireContext(), itemHomeList);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (firebaseUser != null) {
                homeViewModel.getUserData(firebaseUser.getUid()).observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        String userDetails = "Xin chào, " + user.getFullname();
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
    }

    private List<ItemHome> createItemHome() {
        List<ItemHome> itemHomeList = new ArrayList<>();
        itemHomeList.add(new ItemHome(R.drawable.avatar, "Nhân Viên", AccountFragment.class));
        itemHomeList.add(new ItemHome(R.drawable.avatar, "Chức Vụ", ServiceFragment.class));
//        itemHomeList.add(new ItemHome(R.drawable.avatar, "Bàn Ăn", TableManageFragment.class));
//        itemHomeList.add(new ItemHome(R.drawable.avatar, "Menu", DishManageFragment.class));
        return itemHomeList;
    }

}
