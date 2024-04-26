package vn.mn.quanlynhahang.adapter;

import static android.app.PendingIntent.getService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.Role;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {


    private Context mContext;
    private List<Role> roleList;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, Role role);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, Role role);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ServiceAdapter(Context mContext) {
        this.mContext = mContext;
        roleList = new ArrayList<>();
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
        notifyDataSetChanged();
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Role role = roleList.get(position);
        holder.bind(role);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pos, roleList.get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(pos, roleList.get(position));
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return roleList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtNameService);
        }

        public void bind(Role role) {
            textView.setText(role.getTenChucVu());
        }
    }


    //    private Context mContext;
//    private List<String> serviceList;
//    private OnItemClickListener listener;
//    private OnItemLongClickListener longClickListener;
//    public interface OnItemClickListener {
//        void onItemClick(int position, String serviceName);
//    }
//    public interface OnItemLongClickListener {
//        void onItemLongClick(int position, String serviceName);
//    }
//    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
//        this.longClickListener = listener;
//    }
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//
//    public ServiceAdapter(Context mContext) {
//        this.mContext = mContext;
//        serviceList = new ArrayList<>();
//    }
//    public void setServiceList(List<String> serviceList) {
//        this.serviceList = serviceList;
//        notifyDataSetChanged();
//    }
//    public List<String> getServiceList() {
//        return serviceList;
//    }
//
//    @NonNull
//    @Override
//    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view;
//        view = LayoutInflater.from(mContext).inflate(R.layout.item_service, parent, false);
//        return new ServiceViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
//        String service = serviceList.get(position);
//        holder.bind(service);
//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) {
//                int pos = holder.getAdapterPosition();
//                if (pos != RecyclerView.NO_POSITION) {
//                    listener.onItemClick(pos, serviceList.get(position));
//                }
//            }
//        });
//        holder.itemView.setOnLongClickListener(v -> {
//            if (longClickListener != null) {
//                int pos = holder.getAdapterPosition();
//                if (pos != RecyclerView.NO_POSITION) {
//                    longClickListener.onItemLongClick(pos, serviceList.get(position));
//                    return true;
//                }
//            }
//            return false;
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return serviceList.size();
//    }
//
//
//    public class ServiceViewHolder extends RecyclerView.ViewHolder {
//        private TextView textView;
//        public ServiceViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.txtNameService);
//        }
//        public void bind(String service) {
//            textView.setText(service);
//        }
//    }
}
