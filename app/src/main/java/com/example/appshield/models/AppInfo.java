package com.example.appshield.models;

import android.graphics.drawable.Drawable;

import java.util.List;

public class AppInfo {

    private String appName;
    private String packageName;
    private Drawable appIcon;

    private List<String> permissions;
    private List<String> grantedPermissions;
    private List<String> sensitiveCategories;

    private int riskScore;
    private String riskLevel;
    private List<String> riskReasons;

    // Privacy exposure based on currently granted sensitive permissions
    private int privacyExposureScore;
    private String privacyExposureLevel;

    // Security posture information
    private int targetSdkVersion;
    private boolean debuggable;

    public AppInfo(
            String appName,
            String packageName,
            Drawable appIcon,
            List<String> permissions,
            List<String> grantedPermissions,
            List<String> sensitiveCategories,
            int riskScore,
            String riskLevel,
            List<String> riskReasons,
            int privacyExposureScore,
            String privacyExposureLevel,
            int targetSdkVersion,
            boolean debuggable
    ) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.permissions = permissions;
        this.grantedPermissions = grantedPermissions;
        this.sensitiveCategories = sensitiveCategories;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.riskReasons = riskReasons;
        this.privacyExposureScore = privacyExposureScore;
        this.privacyExposureLevel = privacyExposureLevel;
        this.targetSdkVersion = targetSdkVersion;
        this.debuggable = debuggable;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getGrantedPermissions() {
        return grantedPermissions;
    }

    public List<String> getSensitiveCategories() {
        return sensitiveCategories;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public List<String> getRiskReasons() {
        return riskReasons;
    }

    public int getPrivacyExposureScore() {
        return privacyExposureScore;
    }

    public String getPrivacyExposureLevel() {
        return privacyExposureLevel;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public boolean isDebuggable() {
        return debuggable;
    }
}