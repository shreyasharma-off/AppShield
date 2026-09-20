package com.example.appshield.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appshield.R;
import com.example.appshield.adapters.AppAdapter;
import com.example.appshield.activities.ScanActivity;
import com.example.appshield.models.AppInfo;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class InstalledAppsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppAdapter adapter;
    private TextView txtEmpty;
    private TextInputEditText editSearch;
    private ChipGroup chipGroupRisk;
    
    private String currentRiskFilter = "All";
    private String currentSearchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_installed_apps, container, false);

        recyclerView = view.findViewById(R.id.recycler_apps);
        txtEmpty = view.findViewById(R.id.txt_empty_state);
        editSearch = view.findViewById(R.id.edit_search);
        chipGroupRisk = view.findViewById(R.id.chip_group_risk);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        List<AppInfo> appList = ScanActivity.lastScanResults;
        if (appList == null) appList = new ArrayList<>();
        
        adapter = new AppAdapter(appList, getContext());
        recyclerView.setAdapter(adapter);

        updateVisibility(appList.isEmpty());

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                adapter.filter(currentSearchQuery, currentRiskFilter);
                checkEmpty();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        chipGroupRisk.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentRiskFilter = "All";
            } else {
                int id = checkedIds.get(0);
                if (id == R.id.chip_high) currentRiskFilter = "High";
                else if (id == R.id.chip_medium) currentRiskFilter = "Medium";
                else if (id == R.id.chip_low) currentRiskFilter = "Low";
                else currentRiskFilter = "All";
            }
            adapter.filter(currentSearchQuery, currentRiskFilter);
            checkEmpty();
        });

        return view;
    }

    private void updateVisibility(boolean isEmpty) {
        if (isEmpty) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setText("No apps scanned yet");
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void checkEmpty() {
        if (adapter.getItemCount() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
            txtEmpty.setText("No applications found");
        } else {
            txtEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ScanActivity.lastScanResults != null) {
            adapter.updateData(ScanActivity.lastScanResults);
            updateVisibility(ScanActivity.lastScanResults.isEmpty());
        }
    }
}
