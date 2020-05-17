package com.example.mymoviememoir;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class DashboardFragment extends Fragment {
    private TextView welcomeTv;
    private TextView dateTv;

    public DashboardFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        welcomeTv = view.findViewById(R.id.dashboard_welcome);
        dateTv = view.findViewById(R.id.dashboard_date);
        String firstName = getArguments().getString("firstName");
        welcomeTv.setText("Hello " + firstName + "!");
        return view;
    }
}
