package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.NotifUser;
import vn.mn.quanlynhahang.model.UserUid;

public class EditNotificationAdapter extends RecyclerView.Adapter<EditNotificationAdapter.EditNotificationViewHolder> {
    private final List<NotifUser> notifUserList;
    private final Context mContext;

    public EditNotificationAdapter(Context mContext, List<NotifUser> notifUserList) {
        this.notifUserList = notifUserList;
        this.mContext = mContext;
    }
    public List<String> getSelectedTime() {
        List<String> selectedTime = new ArrayList<>();
        for (NotifUser user : notifUserList) {
            if (user.isSelected()) {
                selectedTime.add(user.getTimeSent());
            }
        }
        return selectedTime;
    }
    public void selectAllItems(boolean isChecked) {
        for (NotifUser notification : notifUserList) {
            notification.setSelected(isChecked);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EditNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notif2, parent, false);
        return new EditNotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditNotificationViewHolder holder, int position) {
        NotifUser user = notifUserList.get(position);
        String senderName = user.getSenderName();
        if (!senderName.startsWith("Đến: ")) {
            senderName = "Đến: " + senderName;
        }
        holder.txtName.setText(senderName);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String formattedTime = sdf.format(new Date(Long.parseLong(user.getTimeSent())));
        holder.txtTime.setText(formattedTime);
        holder.txtContent.setText(user.getNotificationContent());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            user.setSelected(isChecked);
        });
    }



    @Override
    public int getItemCount() {
        return notifUserList.size();
    }

    public void updateList(List<NotifUser> newList) {
        notifUserList.clear();
        notifUserList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class EditNotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtTime;
        private final TextView txtContent;
        private final CheckBox checkBox;

        public EditNotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtNameNotif);
            txtTime = itemView.findViewById(R.id.txtTimeNotif);
            txtContent = itemView.findViewById(R.id.txtContentNotif);
            checkBox = itemView.findViewById(R.id.cbTICK);
        }
    }
}
