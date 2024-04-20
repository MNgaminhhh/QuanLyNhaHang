package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.User;
import vn.mn.quanlynhahang.view.AddUserActivity;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHoldler>{
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
    public AccountViewHoldler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ADD_USER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_addaccount, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.rv_account, parent, false);
        }
        return new AccountViewHoldler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHoldler holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_USER) {
            User user = userList.get(position - 1);
            if (!Objects.equals(user.getRole(), "admin")) {
                if (user.getFullname() != null) {
                    holder.txtNameItem.setText(user.getFullname());
                    Glide.with(mContext)
                            .load(user.getAvatarurl())
                            .placeholder(R.drawable.avatar)
                            .error(R.drawable.baseline_error_24)
                            .into(holder.imgAccount);
                } else {
                    holder.txtNameItem.setText(" ");
                }
            }

        }else{
            holder.cardView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, AddUserActivity.class);
                mContext.startActivity(intent);
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
    public void setUserList(List<User> userList) {
        List<User> filteredUserList = new ArrayList<>();
        for (User user : userList) {
            if (!Objects.equals(user.getRole(), "admin")) {
                filteredUserList.add(user);
            }
        }
        this.userList = filteredUserList;
        notifyDataSetChanged();
    }

    public static class AccountViewHoldler extends RecyclerView.ViewHolder {
        private  TextView txtNameItem;
        private ImageView imgAccount;
        private CardView cardView;

        public AccountViewHoldler(@NonNull View itemView) {
            super(itemView);
            txtNameItem = (TextView) itemView.findViewById(R.id.txtNameItem);
            imgAccount = (ImageView) itemView.findViewById(R.id.imgAvatarAccount);
            cardView = (CardView) itemView.findViewById(R.id.cvItemAccount);
        }

    }

}
