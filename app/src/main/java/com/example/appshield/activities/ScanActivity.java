package com.example.appshield.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appshield.R;
import com.example.appshield.analyzer.AppScanner;
import com.example.appshield.database.DatabaseHelper;
import com.example.appshield.models.AppInfo;
import com.example.appshield.models.ScanRecord;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanActivity extends AppCompatActivity {

    private LinearLayout layoutScanning, layoutComplete;
    private TextView txtScanStage, txtScanSummary, txtSummaryHigh, txtSummaryMedium, txtSummaryLow;
    private MaterialButton btnViewResults;
    private ProgressBar progressCircular;

    private List<AppInfo> scannedApps;
    public static List<AppInfo> lastScanResults; // Temporary storage for the fragment to read

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        layoutScanning = findViewById(R.id.layout_scanning);
        layoutComplete = findViewById(R.id.layout_complete);
        txtScanStage = findViewById(R.id.txt_scan_stage);
        txtScanSummary = findViewById(R.id.txt_scan_summary);
        txtSummaryHigh = findViewById(R.id.txt_summary_high);
        txtSummaryMedium = findViewById(R.id.txt_summary_medium);
        txtSummaryLow = findViewById(R.id.txt_summary_low);
        btnViewResults = findViewById(R.id.btn_view_results);
        progressCircular = findViewById(R.id.progress_circular);

        startScan();

        btnViewResults.setOnClickListener(v -> {
            // Navigate back to MainActivity and tell it to show InstalledAppsFragment
            // We use static storage for simplicity in this college project
            lastScanResults = scannedApps;
            finish();
        });
    }

    private void startScan() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Update UI stage
            handler.post(() -> txtScanStage.setText("Discovering applications..."));
            
            AppScanner scanner = new AppScanner(this);
            scannedApps = scanner.scanApps();

            handler.post(() -> txtScanStage.setText("Reading requested permissions..."));
            // Simulate reading time if scan was too fast for UI
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            handler.post(() -> txtScanStage.setText("Calculating risk..."));
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            int high = 0, medium = 0, low = 0;
            for (AppInfo app : scannedApps) {
                if ("High".equalsIgnoreCase(app.getRiskLevel())) high++;
                else if ("Medium".equalsIgnoreCase(app.getRiskLevel())) medium++;
                else low++;
            }


            // Save to database
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.insertScan(new ScanRecord(scannedApps.size(), high, medium, low));

            // Mark that a scan has completed successfully
            getSharedPreferences("appshield_session", MODE_PRIVATE)
                    .edit()
                    .putBoolean("scan_completed", true)
                    .apply();


            int finalHigh = high;
            int finalMedium = medium;
            int finalLow = low;
            handler.post(() -> {
                layoutScanning.setVisibility(View.GONE);
                layoutComplete.setVisibility(View.VISIBLE);
                txtScanSummary.setText(scannedApps.size() + " applications analyzed");
                txtSummaryHigh.setText("High: " + finalHigh);
                txtSummaryMedium.setText("Medium: " + finalMedium);
                txtSummaryLow.setText("Low: " + finalLow);
            });
        });
    }
}
