package com.example.appshield.models;

import java.io.Serializable;
import java.util.List;

public class AppInfo implements Serializable {
    private String appName;
    private String packageName;
    private int riskScore;
    private String riskLevel; // Low, Medium, High
    private List<String> sensitivePermissions;
    private List<String> allPermissions;
    private String riskExplanation;

    public AppInfo(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
        this.riskScore = 0;
        this.riskLevel = "Low";
    }

    // Getters and Setters
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public int getRiskScore() { return riskScore; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public List<String> getSensitivePermissions() { return sensitivePermissions; }
    public void setSensitivePermissions(List<String> sensitivePermissions) { this.sensitivePermissions = sensitivePermissions; }

    public List<String> getAllPermissions() { return allPermissions; }
    public void setAllPermissions(List<String> allPermissions) { this.allPermissions = allPermissions; }

    public String getRiskExplanation() { return riskExplanation; }
    public void setRiskExplanation(String riskExplanation) { this.riskExplanation = riskExplanation; }
}
