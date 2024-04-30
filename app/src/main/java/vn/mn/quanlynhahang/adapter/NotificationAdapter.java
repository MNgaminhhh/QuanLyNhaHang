package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.NotifUser;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<NotifUser> notifUserList;
    private Context mContext;

    public NotificationAdapter(Context mContext, List<NotifUser> notifUserList) {
        this.mContext = mContext;
        this.notifUserList = notifUserList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_notif, parent, false);
        return new NotificationViewHolder(view);
    }
    public void updateData(List<NotifUser> newData) {
        notifUserList.clear();
        notifUserList.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        int reversePosition = getItemCount() - 1 - position;
        NotifUser user = notifUserList.get(reversePosition);
        holder.txtName.setText(user.getSenderName());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
        String formattedTime = sdf.format(new Date(Long.parseLong(user.getTimeSent())));
        holder.txtTime.setText(formattedTime);
        holder.txtContent.setText(user.getNotificationContent());
    }

    @Override
    public int getItemCount() {
        return notifUserList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtTime;
        private TextView txtContent;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtNameNotif);
            txtTime = (TextView) itemView.findViewById(R.id.txtTimeNotif);
            txtContent = (TextView) itemView.findViewById(R.id.txtContentNotif);
        }
    }
}
