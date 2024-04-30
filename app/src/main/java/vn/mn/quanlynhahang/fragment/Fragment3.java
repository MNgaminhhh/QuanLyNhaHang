package vn.mn.quanlynhahang.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import vn.mn.quanlynhahang.R;
import vn.mn.quanlynhahang.view.LoginActivity;

public class Fragment3 extends Fragment {

    private Button btnStart;
    private View mView;

    public Fragment3() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_3, container, false);
        btnStart = mView.findViewById(R.id.btnStart);

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        return mView;
    }
}