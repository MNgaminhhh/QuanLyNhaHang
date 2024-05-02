package vn.mn.quanlynhahang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.fragment.AddUserFragment;
import vn.mn.quanlynhahang.fragment.UserDetailFragment;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.view.AddUserActivity;
import vn.mn.quanlynhahang.view.UserDetailActivitty;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private static final int VIEW_TYPE_ADD_USER = 0;
    private static final int VIEW_TYPE_USER = 1;
    private Context mContext;
    private List<User> userList;

    public AccountAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ADD_USER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_addaccount, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.rv_account, parent, false);
        }
        return new AccountViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_USER) {
            User user = userList.get(position - 1);
            if (user.getFullname() != null) {
                holder.txtNameItem.setText(user.getFullname());
                holder.txtRoleItem.setText(user.getRole());
                Glide.with(mContext)
                        .load(user.getAvatarurl())
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.imageerror)
                        .into(holder.imgAccount);
            } else {
                holder.txtNameItem.setText(" ");
                holder.txtRoleItem.setText(" ");
                Glide.with(mContext)
                        .load(user.getAvatarurl())
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.imageerror)
                        .into(holder.imgAccount);
            }
            holder.cvItemUser.setOnClickListener(v -> {
                navigateToUserDetailFragment(user.getPhone());
            });

        } else {
            holder.cardView.setOnClickListener(v -> {
                navigateToAddUserFragment();
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_ADD_USER : VIEW_TYPE_USER;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUserList(List<User> userList) {
        this.userList = new ArrayList<>(userList);
        notifyDataSetChanged();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNameItem, txtRoleItem;
        private ImageView imgAccount;

        private CardView cardView, cvItemUser;

        public AccountViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            txtNameItem = itemView.findViewById(R.id.txtNameItem);
            txtRoleItem = itemView.findViewById(R.id.txtRoleItem);
            imgAccount = itemView.findViewById(R.id.imgAvatarAccount2);
            if (viewType == VIEW_TYPE_ADD_USER) {
                cvItemUser = null;
                cardView = itemView.findViewById(R.id.cvItemAccount);
                if (cardView != null) {
                    cardView.setVisibility(View.VISIBLE);
                }
            } else {
                cardView = null;
                cvItemUser = itemView.findViewById(R.id.cvItemAccount2);
                if (cvItemUser != null) {
                    cvItemUser.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void navigateToUserDetailFragment(String userId) {
        Fragment userDetailFragment = new UserDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        userDetailFragment.setArguments(bundle);
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, userDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToAddUserFragment() {
        Fragment addUserFragment = new AddUserFragment();
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, addUserFragment)
                .addToBackStack(null)
                .commit();
    }
}
