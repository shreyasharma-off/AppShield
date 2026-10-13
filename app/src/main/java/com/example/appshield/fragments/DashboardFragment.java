package com.example.appshield.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appshield.R;
import com.example.appshield.activities.AboutActivity;
import com.example.appshield.activities.MainActivity;
import com.example.appshield.activities.MethodologyActivity;
import com.example.appshield.activities.ScanActivity;
import com.example.appshield.database.DatabaseHelper;
import com.example.appshield.models.ScanRecord;
import java.util.List;
import android.content.SharedPreferences;

public class DashboardFragment extends Fragment {

    private TextView txtTotalApps, txtHighRisk, txtMediumRisk, txtLowRisk;
    private DatabaseHelper dbHelper;
    private static final String PREFS_NAME = "appshield_session";
    private static final String KEY_SCAN_COMPLETED = "scan_completed";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        txtTotalApps = view.findViewById(R.id.txt_total_apps);
        txtHighRisk = view.findViewById(R.id.txt_high_risk);
        txtMediumRisk = view.findViewById(R.id.txt_medium_risk);
        txtLowRisk = view.findViewById(R.id.txt_low_risk);
        dbHelper = new DatabaseHelper(requireContext());

        setupExploreRows(view);
        updateOverview();

        view.findViewById(R.id.btn_scan_device).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ScanActivity.class));
        });

        return view;
    }

    private void setupExploreRows(View view) {
        // Setup Apps row
        View rowApps = view.findViewById(R.id.card_apps);
        ((TextView) rowApps.findViewById(R.id.txt_row_title)).setText(R.string.installed_apps);
        ((TextView) rowApps.findViewById(R.id.txt_row_body)).setText(R.string.installed_apps_body);
        ((ImageView) rowApps.findViewById(R.id.img_row_icon)).setImageResource(R.drawable.ic_apps);
        rowApps.setOnClickListener(v -> ((MainActivity) requireActivity()).setSelectedNavItem(R.id.nav_apps));

        // Setup History row
        View rowHistory = view.findViewById(R.id.card_history);
        ((TextView) rowHistory.findViewById(R.id.txt_row_title)).setText(R.string.scan_history);
        ((TextView) rowHistory.findViewById(R.id.txt_row_body)).setText(R.string.scan_history_body);
        ((ImageView) rowHistory.findViewById(R.id.img_row_icon)).setImageResource(R.drawable.ic_clock);
        rowHistory.setOnClickListener(v -> ((MainActivity) requireActivity()).setSelectedNavItem(R.id.nav_history));

        // Setup Methodology row
        View rowMethod = view.findViewById(R.id.card_methodology);
        ((TextView) rowMethod.findViewById(R.id.txt_row_title)).setText(R.string.risk_methodology);
        ((TextView) rowMethod.findViewById(R.id.txt_row_body)).setText(R.string.risk_methodology_body);
        ((ImageView) rowMethod.findViewById(R.id.img_row_icon)).setImageResource(R.drawable.ic_help);
        rowMethod.setOnClickListener(v -> startActivity(new Intent(getContext(), MethodologyActivity.class)));

        // Setup Settings row
        View rowSettings = view.findViewById(R.id.card_settings);
        ((TextView) rowSettings.findViewById(R.id.txt_row_title)).setText(R.string.settings_about);
        ((TextView) rowSettings.findViewById(R.id.txt_row_body)).setText(R.string.settings_about_body);
        ((ImageView) rowSettings.findViewById(R.id.img_row_icon)).setImageResource(R.drawable.ic_settings);
        rowSettings.setOnClickListener(v -> ((MainActivity) requireActivity()).setSelectedNavItem(R.id.nav_settings));
    }



    private void updateOverview() {
        boolean scanCompleted = requireContext()
                .getSharedPreferences(
                        "appshield_session",
                        android.content.Context.MODE_PRIVATE
                )
                .getBoolean("scan_completed", false);

        if (!scanCompleted) {
            txtTotalApps.setText("0");
            txtHighRisk.setText("0");
            txtMediumRisk.setText("0");
            txtLowRisk.setText("0");
            return;
        }

        List<ScanRecord> scans = dbHelper.getAllScans();

        if (scans != null && !scans.isEmpty()) {
            ScanRecord latest = scans.get(0);

            txtTotalApps.setText(String.valueOf(latest.getTotalApps()));
            txtHighRisk.setText(String.valueOf(latest.getHighRiskCount()));
            txtMediumRisk.setText(String.valueOf(latest.getMediumRiskCount()));
            txtLowRisk.setText(String.valueOf(latest.getLowRiskCount()));
        } else {
            txtTotalApps.setText("0");
            txtHighRisk.setText("0");
            txtMediumRisk.setText("0");
            txtLowRisk.setText("0");
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        updateOverview();
    }
}
