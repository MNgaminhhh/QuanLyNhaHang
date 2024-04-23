package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.mn.quanlynhahang.R;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {
    private Context mContext;
    private String[] mActivityList;

    public DetailAdapter(Context mContext, String[] mActivityList) {
        this.mContext = mContext;
        this.mActivityList = mActivityList;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.textViewTitle.setText(mActivityList[position]);
    }

    @Override
    public int getItemCount() {
        return mActivityList.length;
    }


    public class DetailViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
        }
    }
}
