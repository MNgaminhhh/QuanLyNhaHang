package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.fragment.AddNotificationFragment;
import vn.mn.quanlynhahang.model.UserUid;

public class AddNotificationAdapter extends RecyclerView.Adapter<AddNotificationAdapter.AddNotificationViewHolder> {
    private Context mContext;
    private List<UserUid> userUidList;

    public AddNotificationAdapter(Context mContext, List<UserUid> userUidList) {
        this.mContext = mContext;
        this.userUidList = userUidList;
    }
    public void updateList(List<UserUid> newList) {
        userUidList.clear();
        userUidList.addAll(newList);
        notifyDataSetChanged();
    }

    public List<String> getSelectedUIDs() {
        List<String> selectedUIDs = new ArrayList<>();
        for (UserUid user : userUidList) {
            if (user.isSelected()) {
                selectedUIDs.add(user.getUserId());
            }
        }
        return selectedUIDs;
    }

    @NonNull
    @Override
    public AddNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(mContext).inflate(R.layout.item_addnotif, parent, false);
        return new AddNotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddNotificationViewHolder holder, int position) {
        UserUid user = userUidList.get(position);
        holder.txtName.setText("Tên: "  + user.getFullname());
        holder.txtRole.setText("Quyền: " + user.getRole());
        Glide.with(mContext)
                .load(user.getAvatarurl())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.imageerror)
                .into(holder.imgAvatar);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            user.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return userUidList.size();
    }


    public static class AddNotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtRole;
        private ImageView imgAvatar;
        private CheckBox checkBox;
        public AddNotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(vn.mn.quanlynhahang.R.id.txtNameItem);
            txtRole = itemView.findViewById(R.id.txtRoleItem);
            imgAvatar = itemView.findViewById(R.id.imgAvatarAccount2);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
