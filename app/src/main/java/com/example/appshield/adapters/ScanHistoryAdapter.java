package com.example.appshield.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appshield.R;
import com.example.appshield.models.ScanRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * ScanHistoryAdapter binds a list of ScanRecord objects into the RecyclerView
 * inside ScanHistoryFragment. Each row uses item_scan_record.xml.
 */
public class ScanHistoryAdapter extends RecyclerView.Adapter<ScanHistoryAdapter.ViewHolder> {

    // The current data backing the list.
    private List<ScanRecord> scanList = new ArrayList<>();

    // ── ViewHolder ────────────────────────────────────────────────────────────

    /**
     * ViewHolder caches references to the views in item_scan_record.xml
     * so we don't call findViewById repeatedly during scroll.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvScanDate;
        TextView tvTotalApps;
        TextView tvHighCount;
        TextView tvMediumCount;
        TextView tvLowCount;

        ViewHolder(View itemView) {
            super(itemView);
            tvScanDate    = itemView.findViewById(R.id.tvScanDate);
            tvTotalApps   = itemView.findViewById(R.id.tvTotalApps);
            tvHighCount   = itemView.findViewById(R.id.tvHighCount);
            tvMediumCount = itemView.findViewById(R.id.tvMediumCount);
            tvLowCount    = itemView.findViewById(R.id.tvLowCount);
        }
    }

    // ── RecyclerView.Adapter callbacks ────────────────────────────────────────

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanRecord record = scanList.get(position);

        holder.tvScanDate.setText(record.getScanDate());
        holder.tvTotalApps.setText("Total apps: " + record.getTotalApps());
        holder.tvHighCount.setText("High: "   + record.getHighRiskCount());
        holder.tvMediumCount.setText("Medium: " + record.getMediumRiskCount());
        holder.tvLowCount.setText("Low: "    + record.getLowRiskCount());
    }

    @Override
    public int getItemCount() {
        return scanList.size();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Replaces the adapter's data and refreshes the list.
     * Called from ScanHistoryFragment.onResume() and after clearing history.
     *
     * @param data New list of ScanRecord objects (may be empty, never null).
     */
    public void setData(List<ScanRecord> data) {
        this.scanList = data != null ? data : new ArrayList<>();
        notifyDataSetChanged();
    }
}
