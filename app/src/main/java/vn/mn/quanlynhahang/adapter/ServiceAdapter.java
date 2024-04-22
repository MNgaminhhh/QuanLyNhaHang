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

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private Context mContext;
    private List<String> serviceList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position, String serviceName);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public ServiceAdapter(Context mContext) {
        this.mContext = mContext;
        serviceList = new ArrayList<>();
    }
    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
        notifyDataSetChanged();
    }
    public List<String> getServiceList() {
        return serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        String service = serviceList.get(position);
        holder.bind(service);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(pos, serviceList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txtNameService);
        }
        public void bind(String service) {
            textView.setText(service);
        }
    }
}
