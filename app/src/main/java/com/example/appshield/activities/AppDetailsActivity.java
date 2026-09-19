package com.example.appshield.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appshield.R;
import com.example.appshield.models.AppInfo;

public class AppDetailsActivity extends AppCompatActivity {

    private AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        appInfo = (AppInfo) getIntent().getSerializableExtra("app_info");

        if (appInfo == null) {
            Toast.makeText(this, "App information not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
    }

    private void setupUI() {
        TextView txtAppName = findViewById(R.id.txt_details_app_name);
        TextView txtPackage = findViewById(R.id.txt_details_package);
        TextView txtRiskScore = findViewById(R.id.txt_details_risk_score);
        TextView txtRiskLevel = findViewById(R.id.txt_details_risk_level);
        TextView txtExplanation = findViewById(R.id.txt_details_explanation);
        TextView txtPermissions = findViewById(R.id.txt_details_permissions);
        Button btnOpenSettings = findViewById(R.id.btn_open_settings);

        txtAppName.setText(appInfo.getAppName());
        txtPackage.setText(appInfo.getPackageName());
        txtRiskScore.setText("Risk Score: " + appInfo.getRiskScore());
        txtRiskLevel.setText("Risk Level: " + appInfo.getRiskLevel());
        txtExplanation.setText(appInfo.getRiskExplanation() != null ? appInfo.getRiskExplanation() : "No explanation available.");
        
        StringBuilder permissionsText = new StringBuilder();
        if (appInfo.getAllPermissions() != null && !appInfo.getAllPermissions().isEmpty()) {
            for (String perm : appInfo.getAllPermissions()) {
                permissionsText.append("• ").append(perm).append("\n");
            }
        } else {
            permissionsText.append("No permissions found.");
        }
        txtPermissions.setText(permissionsText.toString());

        btnOpenSettings.setOnClickListener(v -> openAppSettings(appInfo.getPackageName()));
    }

    private void openAppSettings(String packageName) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Could not open settings for this app", Toast.LENGTH_SHORT).show();
        }
    }
}
