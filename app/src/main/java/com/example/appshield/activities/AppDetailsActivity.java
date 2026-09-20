package com.example.appshield.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appshield.R;
import com.example.appshield.models.AppInfo;
import com.google.android.material.card.MaterialCardView;

public class AppDetailsActivity extends AppCompatActivity {

    public static AppInfo selectedApp; // Bridge to avoid serialization issues with Drawable
    private AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        // Prefer the static bridge for this college project to preserve Drawable
        if (selectedApp != null) {
            appInfo = selectedApp;
        } else {
            // Fallback for intent if possible (though AppInfo currently isn't Serializable)
            appInfo = (AppInfo) getIntent().getSerializableExtra("app_info");
        }

        if (appInfo == null) {
            Toast.makeText(this, "App information not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupUI();
    }

    private void setupUI() {
        ImageView imgIcon = findViewById(R.id.img_details_icon);
        TextView txtAppName = findViewById(R.id.txt_details_app_name);
        TextView txtPackage = findViewById(R.id.txt_details_package);
        
        TextView txtRiskLevel = findViewById(R.id.txt_details_risk_level);
        TextView txtRiskScore = findViewById(R.id.txt_details_risk_score);
        TextView txtRiskDesc = findViewById(R.id.txt_details_risk_desc);
        
        LinearLayout layoutCategories = findViewById(R.id.layout_sensitive_categories);
        TextView txtReasons = findViewById(R.id.txt_details_reasons);
        TextView txtPermissions = findViewById(R.id.txt_details_permissions);
        
        MaterialCardView cardResult = findViewById(R.id.card_risk_result);
        Button btnOpenSettings = findViewById(R.id.btn_open_settings);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        // App Identity
        if (appInfo.getAppIcon() != null) {
            imgIcon.setImageDrawable(appInfo.getAppIcon());
        } else {
            imgIcon.setImageResource(android.R.drawable.sym_def_app_icon);
        }
        txtAppName.setText(appInfo.getAppName());
        txtPackage.setText(appInfo.getPackageName());

        // Risk Result
        txtRiskLevel.setText(appInfo.getRiskLevel().toUpperCase() + " RISK");
        txtRiskScore.setText(appInfo.getRiskScore() + " points");
        
        // Color coding
        int riskColor = getResources().getColor(R.color.risk_low);
        int riskBg = getResources().getColor(R.color.low_bg);
        if ("High".equalsIgnoreCase(appInfo.getRiskLevel())) {
            riskColor = getResources().getColor(R.color.risk_high);
            riskBg = getResources().getColor(R.color.high_bg);
            txtRiskDesc.setText("This application requests multiple highly sensitive permissions that could impact your privacy.");
        } else if ("Medium".equalsIgnoreCase(appInfo.getRiskLevel())) {
            riskColor = getResources().getColor(R.color.risk_medium);
            riskBg = getResources().getColor(R.color.medium_bg);
            txtRiskDesc.setText("This application requests some sensitive permissions. Review the reasons below.");
        } else {
            txtRiskDesc.setText("This application requests minimal sensitive permissions and is considered low risk.");
        }
        
        txtRiskLevel.setTextColor(riskColor);
        cardResult.setCardBackgroundColor(riskBg);

        // Categories
        layoutCategories.removeAllViews();
        if (appInfo.getSensitiveCategories() != null) {
            for (String category : appInfo.getSensitiveCategories()) {
                addCategoryCard(layoutCategories, category);
            }
        }

        // Reasons
        StringBuilder reasonsText = new StringBuilder();
        if (appInfo.getRiskReasons() != null) {
            for (String reason : appInfo.getRiskReasons()) {
                reasonsText.append("• ").append(reason).append("\n");
            }
        }
        txtReasons.setText(reasonsText.length() > 0 ? reasonsText.toString() : "No specific risks identified.");

        // Permissions
        StringBuilder permissionsText = new StringBuilder();
        if (appInfo.getPermissions() != null) {
            for (String perm : appInfo.getPermissions()) {
                permissionsText.append("• ").append(perm).append("\n");
            }
        }
        txtPermissions.setText(permissionsText.length() > 0 ? permissionsText.toString() : "No permissions requested.");

        btnOpenSettings.setOnClickListener(v -> openAppSettings(appInfo.getPackageName()));
    }

    private void addCategoryCard(LinearLayout parent, String category) {
        View view = getLayoutInflater().inflate(R.layout.item_category_badge, parent, false);
        TextView txt = view.findViewById(R.id.txt_category_name);
        txt.setText(category);
        parent.addView(view);
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
