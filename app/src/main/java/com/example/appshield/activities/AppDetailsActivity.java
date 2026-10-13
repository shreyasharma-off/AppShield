package com.example.appshield.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appshield.R;
import com.example.appshield.analyzer.PermissionAnalyzer;
import com.example.appshield.models.AppInfo;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AppDetailsActivity extends AppCompatActivity {

    public static AppInfo selectedApp;
    private AppInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        // Retrieve the selected app.
        // The static reference preserves the Drawable app icon.
        if (selectedApp != null) {
            appInfo = selectedApp;
        } else {
            appInfo = (AppInfo) getIntent()
                    .getSerializableExtra("app_info");
        }

        if (appInfo == null) {
            Toast.makeText(
                    this,
                    "App information not found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        setupUI();
    }

    private void setupUI() {

        // ---------------------------------------------------------
        // View references
        // ---------------------------------------------------------

        ImageView imgIcon =
                findViewById(R.id.img_details_icon);

        TextView txtAppName =
                findViewById(R.id.txt_details_app_name);

        TextView txtPackage =
                findViewById(R.id.txt_details_package);

        TextView txtRiskLevel =
                findViewById(R.id.txt_details_risk_level);

        TextView txtRiskScore =
                findViewById(R.id.txt_details_risk_score);

        TextView txtRiskDesc =
                findViewById(R.id.txt_details_risk_desc);

        FlexboxLayout sensitiveCategoriesLayout =
                findViewById(R.id.layout_sensitive_categories);

        TextView txtReasons =
                findViewById(R.id.txt_details_reasons);

        TextView txtPermissions =
                findViewById(R.id.txt_details_permissions);

        Button btnTogglePermissions =
                findViewById(R.id.btn_toggle_permissions);

        MaterialCardView cardResult =
                findViewById(R.id.card_risk_result);

        Button btnOpenSettings =
                findViewById(R.id.btn_open_settings);

        ImageView btnBack =
                findViewById(R.id.btn_back);

        // Separate Privacy Exposure card.
        TextView txtPrivacyLevel =
                findViewById(R.id.txt_privacy_exposure_level);

        TextView txtPrivacyScore =
                findViewById(R.id.txt_privacy_exposure_score);

        // Separate Security Posture card.
        TextView txtTargetSdk =
                findViewById(R.id.txt_target_sdk);

        TextView txtDebuggable =
                findViewById(R.id.txt_debuggable);

        // ---------------------------------------------------------
        // Navigation
        // ---------------------------------------------------------

        btnBack.setOnClickListener(v -> finish());

        // ---------------------------------------------------------
        // App Identity
        // ---------------------------------------------------------

        if (appInfo.getAppIcon() != null) {
            imgIcon.setImageDrawable(appInfo.getAppIcon());
        } else {
            imgIcon.setImageResource(
                    android.R.drawable.sym_def_app_icon
            );
        }

        txtAppName.setText(appInfo.getAppName());
        txtPackage.setText(appInfo.getPackageName());

        // ---------------------------------------------------------
        // Security Risk
        // Based on the requested sensitive permissions.
        // ---------------------------------------------------------

        String riskLevel = appInfo.getRiskLevel();

        txtRiskLevel.setText(
                riskLevel.toUpperCase() + " RISK"
        );

        txtRiskScore.setText(
                appInfo.getRiskScore() + " points"
        );

        int riskColor =
                getResources().getColor(R.color.risk_low);

        int riskBackground =
                getResources().getColor(R.color.low_bg);

        if ("High".equalsIgnoreCase(riskLevel)) {

            riskColor =
                    getResources().getColor(R.color.risk_high);

            riskBackground =
                    getResources().getColor(R.color.high_bg);

        } else if ("Medium".equalsIgnoreCase(riskLevel)) {

            riskColor =
                    getResources().getColor(R.color.risk_medium);

            riskBackground =
                    getResources().getColor(R.color.medium_bg);
        }

        txtRiskLevel.setTextColor(riskColor);
        cardResult.setCardBackgroundColor(riskBackground);

        String securityDescription;

        if ("High".equalsIgnoreCase(riskLevel)) {

            securityDescription =
                    "This application requests multiple highly " +
                            "sensitive permissions that could impact your privacy.";

        } else if ("Medium".equalsIgnoreCase(riskLevel)) {

            securityDescription =
                    "This application requests some sensitive " +
                            "permissions. Review the reasons below.";

        } else {

            securityDescription =
                    "This application requests minimal sensitive " +
                            "permissions and is considered low risk.";
        }

        // Only show the security risk description here.
        // Privacy Exposure and Security Posture have separate cards.
        txtRiskDesc.setText(securityDescription);

        // ---------------------------------------------------------
        // Privacy Exposure
        // Based on currently granted sensitive permissions.
        // ---------------------------------------------------------

        txtPrivacyLevel.setText(
                appInfo.getPrivacyExposureLevel().toUpperCase()
        );

        txtPrivacyScore.setText(
                appInfo.getPrivacyExposureScore() + " points"
        );

        // ---------------------------------------------------------
        // Security Posture
        // ---------------------------------------------------------

        txtTargetSdk.setText(
                String.valueOf(appInfo.getTargetSdkVersion())
        );

        txtDebuggable.setText(
                appInfo.isDebuggable() ? "Yes" : "No"
        );

        // ---------------------------------------------------------
        // Sensitive Category Chips
        // ---------------------------------------------------------

        sensitiveCategoriesLayout.removeAllViews();

        List<String> sensitiveCategories =
                appInfo.getSensitiveCategories();

        if (sensitiveCategories != null) {

            for (String category : sensitiveCategories) {
                addCategoryCard(
                        sensitiveCategoriesLayout,
                        category
                );
            }
        }

        // ---------------------------------------------------------
        // Risk Reasons
        // ---------------------------------------------------------

        StringBuilder reasonsText = new StringBuilder();

        List<String> riskReasons = appInfo.getRiskReasons();

        if (riskReasons != null) {

            for (String reason : riskReasons) {

                reasonsText
                        .append("\u2022 ")
                        .append(reason)
                        .append("\n");
            }
        }

        txtReasons.setText(
                reasonsText.length() > 0
                        ? reasonsText.toString().trim()
                        : "No specific risks identified."
        );


// ---------------------------------------------------------
// Permission Details: Compact by default, expandable
// ---------------------------------------------------------

        List<String> requestedPermissions = appInfo.getPermissions();
        List<String> grantedPermissions = appInfo.getGrantedPermissions();

        PermissionAnalyzer permissionAnalyzer = new PermissionAnalyzer();

        List<String> requestedSensitiveCategories =
                appInfo.getSensitiveCategories();

        List<String> grantedSensitiveCategories =
                permissionAnalyzer.getGrantedSensitiveCategories(
                        grantedPermissions
                );

// Build the compact sensitive-permission summary.
        StringBuilder summaryText = new StringBuilder();

        int requestedCount = requestedSensitiveCategories != null
                ? requestedSensitiveCategories.size() : 0;

        int grantedCount = grantedSensitiveCategories != null
                ? grantedSensitiveCategories.size() : 0;

        summaryText.append("Sensitive permissions\n")
                .append("Requested: ").append(requestedCount).append("\n")
                .append("Granted: ").append(grantedCount).append("\n\n");

        if (requestedSensitiveCategories == null
                || requestedSensitiveCategories.isEmpty()) {

            summaryText.append("No sensitive permissions detected.");

        } else {

            for (String category : requestedSensitiveCategories) {

                boolean isGranted = grantedSensitiveCategories != null
                        && grantedSensitiveCategories.contains(category);

                summaryText.append("\u2022 ")
                        .append(category)
                        .append(" \u2014 ")
                        .append(isGranted ? "Granted" : "Not granted")
                        .append("\n");
            }
        }

// Build the complete list separately.
        StringBuilder fullText = new StringBuilder(summaryText);

        fullText.append("\nAll requested permissions\n\n");

        if (requestedPermissions == null || requestedPermissions.isEmpty()) {

            fullText.append("No permissions requested.");

        } else {

            for (String permission : requestedPermissions) {

                String permissionName = permission;

                String prefix = "android.permission.";

                if (permissionName.startsWith(prefix)) {
                    permissionName = permissionName.substring(prefix.length());
                }

                permissionName = permissionName.replace("_", " ");

                boolean isGranted = grantedPermissions != null
                        && grantedPermissions.contains(permission);

                fullText.append("\u2022 ")
                        .append(permissionName)
                        .append(" \u2014 ")
                        .append(isGranted ? "Granted" : "Not granted")
                        .append("\n");
            }
        }

// Show only the summary when the screen opens.
        txtPermissions.setText(summaryText.toString().trim());
        btnTogglePermissions.setText("Show all");

        final boolean[] showingAllPermissions = {false};

        btnTogglePermissions.setOnClickListener(v -> {

            showingAllPermissions[0] = !showingAllPermissions[0];

            if (showingAllPermissions[0]) {
                txtPermissions.setText(fullText.toString().trim());
                btnTogglePermissions.setText("Show less");
            } else {
                txtPermissions.setText(summaryText.toString().trim());
                btnTogglePermissions.setText("Show all");
            }
        });


        // ---------------------------------------------------------
        // Open Android App Settings
        // ---------------------------------------------------------

        btnOpenSettings.setOnClickListener(
                v -> openAppSettings(
                        appInfo.getPackageName()
                )
        );
    }

    // -------------------------------------------------------------
    // Add a category chip to the FlexboxLayout.
    // -------------------------------------------------------------

    private void addCategoryCard(
            FlexboxLayout layout,
            String category
    ) {

        View view = getLayoutInflater().inflate(
                R.layout.item_category_badge,
                layout,
                false
        );

        TextView txtCategory =
                view.findViewById(R.id.txt_category_name);

        txtCategory.setText(category);

        layout.addView(view);
    }

    // -------------------------------------------------------------
    // Open the selected app's Android settings page.
    // -------------------------------------------------------------

    private void openAppSettings(String packageName) {

        try {

            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            );

            Uri uri = Uri.fromParts(
                    "package",
                    packageName,
                    null
            );

            intent.setData(uri);

            startActivity(intent);

        } catch (ActivityNotFoundException e) {

            Toast.makeText(
                    this,
                    "Could not open settings for this app",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
