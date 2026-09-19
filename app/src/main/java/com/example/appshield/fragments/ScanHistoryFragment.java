package com.example.appshield.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * ScanHistoryFragment shows all past scan summaries stored in SQLite.
 *
 * Navigation:
 *   - The Dashboard (Ankit's code) should replace or add this fragment via the fragment manager.
 *
 * Key behaviour:
 *   - onResume() reloads data so the list is always fresh.
 *   - Empty state: rvHistory hidden, tvEmptyHistory shown.
 *   - "Clear History" button: if empty → Toast; otherwise → AlertDialog → on confirm, clear + Toast.
 */
public class ScanHistoryFragment extends Fragment {

    // ── Views ────────────────────────────────────────────────────────────────
    private RecyclerView        rvHistory;
    private TextView            tvEmptyHistory;
    private Button              btnClearHistory;

    // ── Adapter & DB ──────────────────────────────────────────────────────────
    private ScanHistoryAdapter  adapter;
    private DatabaseHelper      dbHelper;

    // ── Fragment lifecycle ────────────────────────────────────────────────────

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind views
        rvHistory      = view.findViewById(R.id.rvHistory);
        tvEmptyHistory = view.findViewById(R.id.tvEmptyHistory);
        btnClearHistory = view.findViewById(R.id.btnClearHistory);

        // Set up database helper
        dbHelper = new DatabaseHelper(requireContext());

        // Set up RecyclerView
        adapter = new ScanHistoryAdapter();
        rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvHistory.setAdapter(adapter);

        // Wire the Clear History button
        btnClearHistory.setOnClickListener(v -> onClearHistoryClicked());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload history every time the fragment becomes visible
        // (covers the case where a scan was run while we were away).
        loadHistory();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Fetches all scan records from the DB and updates the UI.
     * Shows the empty-state message when the list is empty.
     */
    private void loadHistory() {
        List<ScanRecord> scans = dbHelper.getAllScans();
        adapter.setData(scans);

        if (scans.isEmpty()) {
            // No history — hide the list, show the empty message
            rvHistory.setVisibility(View.GONE);
            tvEmptyHistory.setVisibility(View.VISIBLE);
        } else {
            // History exists — show the list, hide the empty message
            rvHistory.setVisibility(View.VISIBLE);
            tvEmptyHistory.setVisibility(View.GONE);
        }
    }

    /**
     * Handles the "Clear History" button click.
     *
     * - If the list is already empty, shows a Toast and does nothing.
     * - Otherwise, shows an AlertDialog asking the user to confirm.
     *   On confirm: clears the DB, reloads the list, shows a Toast.
     *   On cancel: does nothing (data is preserved).
     */
    private void onClearHistoryClicked() {
        List<ScanRecord> current = dbHelper.getAllScans();

        if (current.isEmpty()) {
            // Nothing to clear
            Toast.makeText(requireContext(),
                    getString(R.string.history_toast_nothing_to_clear),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Confirm before destroying data
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.history_dialog_title))
                .setMessage(getString(R.string.history_dialog_message))
                .setPositiveButton(getString(R.string.history_dialog_clear), (dialog, which) -> {
                    // User confirmed — delete all rows and refresh
                    dbHelper.clearHistory();
                    loadHistory();
                    Toast.makeText(requireContext(),
                            getString(R.string.history_toast_cleared),
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.history_dialog_cancel), null)
                .show();
    }
}
