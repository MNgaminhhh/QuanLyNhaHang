package vn.mn.quanlynhahang.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.viewmodel.PieChartView;

public class ChartFragment extends Fragment {

    private PieChartView pieChartView;
    private DatabaseReference databaseRef;
    private TextView textCategory;
    private TextView textValue;
    private LinearLayout colorIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        pieChartView = view.findViewById(R.id.pieChartView);
        textCategory = view.findViewById(R.id.textCategory);
        textValue = view.findViewById(R.id.textValue);
        colorIndicator = view.findViewById(R.id.colorIndicator);

        databaseRef = FirebaseDatabase.getInstance().getReference("order");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Integer> orderEmpCounts = new HashMap<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderEmp = orderSnapshot.child("orderEmp").getValue(String.class);
                    if (orderEmp != null) {
                        orderEmpCounts.put(orderEmp, orderEmpCounts.getOrDefault(orderEmp, 0) + 1);
                    }
                }

                updateChart(orderEmpCounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        pieChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartNextFragment chartNextFragment = new ChartNextFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, chartNextFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void updateChart(Map<String, Integer> orderEmpCounts) {
        updateTextViewsAndColorIndicator(orderEmpCounts);
        pieChartView.setData(orderEmpCounts);
    }

    private void updateTextViewsAndColorIndicator(Map<String, Integer> orderEmpCounts) {
        List<Integer> colorList = new ArrayList<>();
        colorIndicator.removeAllViews();
        for (Map.Entry<String, Integer> entry : orderEmpCounts.entrySet()) {
            String category = entry.getKey();
            int value = entry.getValue();

            LinearLayout colorRow = new LinearLayout(requireContext());
            colorRow.setOrientation(LinearLayout.HORIZONTAL);
            colorRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            View colorView = new View(requireContext());
            int color = getRandomColor();
            colorView.setBackgroundColor(color);
            LinearLayout.LayoutParams colorParams = new LinearLayout.LayoutParams(
                    50,
                    50
            );
            colorParams.setMargins(100, 0, 8, 0);
            colorView.setLayoutParams(colorParams);

            colorRow.addView(colorView);

            TextView categoryTextView = new TextView(requireContext());
            categoryTextView.setText("Tên : " + category);
            LinearLayout.LayoutParams categoryParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            );
            categoryParams.setMargins(100, 0, 100, 0);
            categoryTextView.setLayoutParams(categoryParams);

            colorRow.addView(categoryTextView);

            TextView valueTextView = new TextView(requireContext());
            valueTextView.setText("Lần order : " + String.valueOf(value));
            LinearLayout.LayoutParams valueParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            valueParams.gravity = Gravity.END;
            valueParams.setMargins(100, 0, 0, 0);
            valueTextView.setLayoutParams(valueParams);
            colorRow.addView(valueTextView);

            colorIndicator.addView(colorRow);
            colorList.add(color);
        }
        pieChartView.setColorList(colorList);
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
