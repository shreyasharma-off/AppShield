package com.example.appshield.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appshield.R;
import com.example.appshield.adapters.ScanHistoryAdapter;
import com.example.appshield.database.DatabaseHelper;
import com.example.appshield.models.ScanRecord;

import java.util.List;

public class ScanHistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private TextView tvEmptyHistory;
    private ImageView btnClearHistory;
    private ScanHistoryAdapter adapter;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvHistory = view.findViewById(R.id.rvHistory);
        tvEmptyHistory = view.findViewById(R.id.tvEmptyHistory);
        btnClearHistory = view.findViewById(R.id.btn_clear_history);

        dbHelper = new DatabaseHelper(requireContext());
        adapter = new ScanHistoryAdapter();
        
        rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistory.setAdapter(adapter);

        btnClearHistory.setOnClickListener(v -> onClearHistoryClicked());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        List<ScanRecord> scans = dbHelper.getAllScans();
        adapter.setData(scans);

        if (scans.isEmpty()) {
            rvHistory.setVisibility(View.GONE);
            tvEmptyHistory.setVisibility(View.VISIBLE);
        } else {
            rvHistory.setVisibility(View.VISIBLE);
            tvEmptyHistory.setVisibility(View.GONE);
        }
    }

    private void onClearHistoryClicked() {
        List<ScanRecord> current = dbHelper.getAllScans();
        if (current.isEmpty()) {
            Toast.makeText(requireContext(), "No history to clear", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Clear History")
                .setMessage("This will delete all saved scan summaries. Continue?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    dbHelper.clearHistory();
                    loadHistory();
                    Toast.makeText(requireContext(), "History cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
