package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotifUser user = notifUserList.get(position);
        holder.txtName.setText(user.getSenderName());
        holder.txtTime.setText(user.getTimeSent());
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
