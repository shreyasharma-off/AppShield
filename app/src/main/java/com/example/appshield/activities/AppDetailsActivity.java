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

import java.util.List;
import com.example.appshield.analyzer.PermissionAnalyzer;

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
            // Fallback for intent if possible
            appInfo = (AppInfo) getIntent().getSerializableExtra("app_info");
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

        ImageView imgIcon = findViewById(R.id.img_details_icon);
        TextView txtAppName = findViewById(R.id.txt_details_app_name);
        TextView txtPackage = findViewById(R.id.txt_details_package);

        TextView txtRiskLevel = findViewById(R.id.txt_details_risk_level);
        TextView txtRiskScore = findViewById(R.id.txt_details_risk_score);
        TextView txtRiskDesc = findViewById(R.id.txt_details_risk_desc);

        LinearLayout layoutCategories =
                findViewById(R.id.layout_sensitive_categories);

        TextView txtReasons =
                findViewById(R.id.txt_details_reasons);

        TextView txtPermissions =
                findViewById(R.id.txt_details_permissions);

        MaterialCardView cardResult =
                findViewById(R.id.card_risk_result);

        Button btnOpenSettings =
                findViewById(R.id.btn_open_settings);

        ImageView btnBack =
                findViewById(R.id.btn_back);

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
        // Risk Result
        // ---------------------------------------------------------

        txtRiskLevel.setText(
                appInfo.getRiskLevel().toUpperCase() + " RISK"
        );

        txtRiskScore.setText(
                appInfo.getRiskScore() + " points"
        );

        int riskColor = getResources().getColor(R.color.risk_low);
        int riskBg = getResources().getColor(R.color.low_bg);

        if ("High".equalsIgnoreCase(appInfo.getRiskLevel())) {

            riskColor = getResources().getColor(R.color.risk_high);
            riskBg = getResources().getColor(R.color.high_bg);

        } else if ("Medium".equalsIgnoreCase(appInfo.getRiskLevel())) {

            riskColor = getResources().getColor(R.color.risk_medium);
            riskBg = getResources().getColor(R.color.medium_bg);
        }

// Build security risk description.
        String securityDescription;

        if ("High".equalsIgnoreCase(appInfo.getRiskLevel())) {

            securityDescription =
                    "This application requests multiple highly sensitive permissions that could impact your privacy.";

        } else if ("Medium".equalsIgnoreCase(appInfo.getRiskLevel())) {

            securityDescription =
                    "This application requests some sensitive permissions. Review the reasons below.";

        } else {

            securityDescription =
                    "This application requests minimal sensitive permissions and is considered low risk.";
        }

// Add Privacy Exposure information.
        String privacyExposureText =
                "\n\nPrivacy Exposure: "
                        + appInfo.getPrivacyExposureLevel().toUpperCase()
                        + " ("
                        + appInfo.getPrivacyExposureScore()
                        + " points)"
                        + "\nBased on currently granted sensitive permissions.";

        txtRiskDesc.setText(
                securityDescription + privacyExposureText
        );

        txtRiskLevel.setTextColor(riskColor);
        cardResult.setCardBackgroundColor(riskBg);

        // ---------------------------------------------------------
        // Sensitive Categories
        // ---------------------------------------------------------

        layoutCategories.removeAllViews();

        if (appInfo.getSensitiveCategories() != null) {

            for (String category : appInfo.getSensitiveCategories()) {
                addCategoryCard(
                        layoutCategories,
                        category
                );
            }
        }

        // ---------------------------------------------------------
        // Risk Reasons
        // ---------------------------------------------------------

        StringBuilder reasonsText = new StringBuilder();

        if (appInfo.getRiskReasons() != null) {

            for (String reason : appInfo.getRiskReasons()) {

                reasonsText
                        .append("• ")
                        .append(reason)
                        .append("\n");
            }
        }

        txtReasons.setText(
                reasonsText.length() > 0
                        ? reasonsText.toString()
                        : "No specific risks identified."
        );

        // ---------------------------------------------------------
// Sensitive Permissions: Requested vs Granted
// ---------------------------------------------------------

        List<String> requestedPermissions =
                appInfo.getPermissions();

        List<String> grantedPermissions =
                appInfo.getGrantedPermissions();

        PermissionAnalyzer permissionAnalyzer =
                new PermissionAnalyzer();

        List<String> requestedSensitiveCategories =
                appInfo.getSensitiveCategories();

        List<String> grantedSensitiveCategories =
                permissionAnalyzer.getGrantedSensitiveCategories(
                        grantedPermissions
                );

        StringBuilder permissionsText =
                new StringBuilder();

        int requestedSensitiveCount =
                requestedSensitiveCategories != null
                        ? requestedSensitiveCategories.size()
                        : 0;

        int grantedSensitiveCount =
                grantedSensitiveCategories != null
                        ? grantedSensitiveCategories.size()
                        : 0;

        permissionsText.append("Sensitive permissions\n\n");

        permissionsText
                .append("Requested: ")
                .append(requestedSensitiveCount)
                .append("\n");

        permissionsText
                .append("Granted: ")
                .append(grantedSensitiveCount)
                .append("\n\n");

        if (requestedSensitiveCategories == null
                || requestedSensitiveCategories.isEmpty()) {

            permissionsText.append(
                    "No sensitive permissions detected."
            );

        } else {

            for (String category : requestedSensitiveCategories) {

                boolean isGranted =
                        grantedSensitiveCategories != null
                                && grantedSensitiveCategories.contains(category);

                permissionsText
                        .append("• ")
                        .append(category)
                        .append(" — ");

                if (isGranted) {
                    permissionsText.append("Granted");
                } else {
                    permissionsText.append("Not Granted");
                }

                permissionsText.append("\n");
            }
        }

// ---------------------------------------------------------
// Full Android Permission List
// ---------------------------------------------------------

        permissionsText.append("\n");
        permissionsText.append("All requested permissions\n\n");

        if (requestedPermissions == null
                || requestedPermissions.isEmpty()) {

            permissionsText.append(
                    "No permissions requested."
            );

        } else {

            for (String permission : requestedPermissions) {

                String permissionName = permission;

                if (permissionName.startsWith(
                        "android.permission."
                )) {

                    permissionName =
                            permissionName.substring(
                                    "android.permission.".length()
                            );
                }

                permissionName =
                        permissionName.replace("_", " ");

                boolean isGranted =
                        grantedPermissions != null
                                && grantedPermissions.contains(permission);

                permissionsText
                        .append("• ")
                        .append(permissionName)
                        .append(" — ");

                if (isGranted) {
                    permissionsText.append("Granted");
                } else {
                    permissionsText.append("Not Granted");
                }

                permissionsText.append("\n");
            }
        }

        txtPermissions.setText(
                permissionsText.toString()
        );

        // ---------------------------------------------------------
        // Open Android App Settings
        // ---------------------------------------------------------

        btnOpenSettings.setOnClickListener(
                v -> openAppSettings(
                        appInfo.getPackageName()
                )
        );
    }

    private void addCategoryCard(
            LinearLayout parent,
            String category
    ) {

        View view = getLayoutInflater().inflate(
                R.layout.item_category_badge,
                parent,
                false
        );

        TextView txt =
                view.findViewById(
                        R.id.txt_category_name
                );

        txt.setText(category);

        parent.addView(view);
    }

    private void openAppSettings(
            String packageName
    ) {

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