package com.example.appshield.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appshield.R;
import com.example.appshield.activities.AboutActivity;
import com.example.appshield.activities.MainActivity;

public class DashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        view.findViewById(R.id.card_installed_apps).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new InstalledAppsFragment());
        });

        view.findViewById(R.id.card_scan_history).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).loadFragment(new ScanHistoryFragment());
        });

        view.findViewById(R.id.card_about).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AboutActivity.class));
        });

        return view;
    }
}
