package vn.mn.quanlynhahang.adapter;

import android.content.Context;
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

import java.util.List;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.model.ItemHome;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private final Context mContext;
    private final List<ItemHome> itemHomeList;

    public HomeAdapter(Context mContext, List<ItemHome> itemHomeList) {
        this.mContext = mContext;
        this.itemHomeList = itemHomeList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_itemhome, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        ItemHome home = itemHomeList.get(position);
        holder.txtTitleIteam.setText(home.getTitleName());
        holder.imgHome.setImageResource(home.getImage());
        holder.itemView.setOnClickListener(v -> {
            try {
                Fragment fragment = home.getFragmentClass().newInstance();
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemHomeList.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitleIteam;
        ImageView imgHome;
        CardView cardView;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleIteam = itemView.findViewById(R.id.txtTitleIteam);
            imgHome = itemView.findViewById(R.id.imgHome);
            cardView = itemView.findViewById(R.id.cvItemHome);
        }
    }
}
