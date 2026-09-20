package com.example.appshield.analyzer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.example.appshield.models.AppInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppScanner {

    private final PackageManager packageManager;
    private final PermissionAnalyzer permissionAnalyzer;
    private final RiskEngine riskEngine;

    public AppScanner(Context context) {
        packageManager = context.getPackageManager();
        permissionAnalyzer = new PermissionAnalyzer();
        riskEngine = new RiskEngine();
    }

    public List<AppInfo> scanApps() {

        List<AppInfo> scannedApps = new ArrayList<>();

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> launchableApps;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            launchableApps = packageManager.queryIntentActivities(
                    launcherIntent,
                    PackageManager.ResolveInfoFlags.of(0)
            );

        } else {

            launchableApps = packageManager.queryIntentActivities(
                    launcherIntent,
                    0
            );
        }

        // Prevent the same app from being scanned more than once.
        Set<String> scannedPackages = new HashSet<>();

        for (ResolveInfo resolveInfo : launchableApps) {

            ApplicationInfo applicationInfo =
                    resolveInfo.activityInfo.applicationInfo;

            String packageName = applicationInfo.packageName;

            if (scannedPackages.contains(packageName)) {
                continue;
            }

            scannedPackages.add(packageName);

            try {

                String appName = packageManager
                        .getApplicationLabel(applicationInfo)
                        .toString();

                Drawable appIcon =
                        packageManager.getApplicationIcon(applicationInfo);

                PackageInfo packageInfo;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    packageInfo = packageManager.getPackageInfo(
                            packageName,
                            PackageManager.PackageInfoFlags.of(
                                    PackageManager.GET_PERMISSIONS
                            )
                    );

                } else {

                    packageInfo = packageManager.getPackageInfo(
                            packageName,
                            PackageManager.GET_PERMISSIONS
                    );
                }

                // Get all permissions requested by the app.
                List<String> permissions;

                if (packageInfo.requestedPermissions != null) {

                    permissions = new ArrayList<>(
                            Arrays.asList(packageInfo.requestedPermissions)
                    );

                } else {

                    permissions = new ArrayList<>();
                }

                // Check which of the requested permissions are currently granted.
                List<String> grantedPermissions = new ArrayList<>();

                for (String permission : permissions) {

                    if (packageManager.checkPermission(
                            permission,
                            packageName
                    ) == PackageManager.PERMISSION_GRANTED) {

                        grantedPermissions.add(permission);
                    }
                }

                // Calculate privacy exposure from currently granted sensitive permissions.
                List<String> grantedSensitiveCategories =
                        permissionAnalyzer.getGrantedSensitiveCategories(
                                grantedPermissions
                        );

                int privacyExposureScore =
                        riskEngine.calculateScore(
                                grantedSensitiveCategories
                        );

                String privacyExposureLevel =
                        riskEngine.getRiskLevel(
                                privacyExposureScore
                        );

                // Map requested permissions to AppShield's sensitive categories.
                List<String> sensitiveCategories =
                        permissionAnalyzer.getSensitiveCategories(
                                permissions
                        );

                // Calculate risk score from requested permissions.
                int riskScore =
                        riskEngine.calculateScore(
                                sensitiveCategories
                        );

                String riskLevel =
                        riskEngine.getRiskLevel(riskScore);

                List<String> riskReasons =
                        riskEngine.getRiskReasons(
                                sensitiveCategories
                        );

                AppInfo appInfo = new AppInfo(
                        appName,
                        packageName,
                        appIcon,
                        permissions,
                        grantedPermissions,
                        sensitiveCategories,
                        riskScore,
                        riskLevel,
                        riskReasons,
                        privacyExposureScore,
                        privacyExposureLevel
                );

                scannedApps.add(appInfo);

            } catch (PackageManager.NameNotFoundException e) {
                // Skip apps whose package information is unavailable.
            }
        }

        return scannedApps;
    }
}