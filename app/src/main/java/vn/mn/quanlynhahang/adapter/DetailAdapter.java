package vn.mn.quanlynhahang.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.mn.quanlynhahang.R;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {
    private Context mContext;
    private String[] mActivityList;
    private boolean[] mCheckedItems;
    private List<String> mSelectedItems;

    public DetailAdapter(Context mContext, String[] mActivityList) {
        this.mContext = mContext;
        this.mActivityList = mActivityList;
        this.mCheckedItems = new boolean[mActivityList.length];
        this.mSelectedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_detail, parent, false);
        return new DetailViewHolder(view);
    }
    public void setSelectedItems(String[] selectedItems) {
        for (int i = 0; i < mActivityList.length; i++) {
            mCheckedItems[i] = false;
        }
            Log.e("RRRRRRRRRRRRR", String.valueOf(selectedItems.length));
        for (String item : selectedItems) {
            for (int i = 0; i < mActivityList.length; i++) {
                if (mActivityList[i].equals(item)) {
                    mCheckedItems[i] = true;
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.textViewTitle.setText(mActivityList[position]);
        holder.checkBox.setChecked(mCheckedItems[position]);
        holder.checkBox.setOnClickListener(v -> {
            mCheckedItems[position] = holder.checkBox.isChecked();
            if (holder.checkBox.isChecked()) {
                mSelectedItems.add(mActivityList[position]);
            } else {
                mSelectedItems.remove(mActivityList[position]);
            }
        });
    }

    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();
        for (int i = 0; i < mActivityList.length; i++) {
            if (mCheckedItems[i]) {
                selectedItems.add(mActivityList[i]);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemCount() {
        return mActivityList.length;
    }


    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public CheckBox checkBox;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
