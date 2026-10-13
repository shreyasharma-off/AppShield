package com.example.appshield.analyzer;

import com.example.appshield.utils.RiskConstants;

import java.util.ArrayList;
import java.util.List;

public class PermissionAnalyzer {

    public List<String> getSensitiveCategories(List<String> permissions) {
        List<String> categories = new ArrayList<>();

        if (permissions == null || permissions.isEmpty()) {
            return categories;
        }

        for (String permission : permissions) {

            if (permission == null) {
                continue;
            }

            if (permission.equals("android.permission.CAMERA")) {
                addCategory(categories, RiskConstants.CAMERA);
            } else if (
                    permission.equals("android.permission.READ_EXTERNAL_STORAGE")
                            || permission.equals("android.permission.WRITE_EXTERNAL_STORAGE")
                            || permission.equals("android.permission.READ_MEDIA_IMAGES")
                            || permission.equals("android.permission.READ_MEDIA_VIDEO")
                            || permission.equals("android.permission.READ_MEDIA_AUDIO")) {
                addCategory(categories, RiskConstants.STORAGE_MEDIA);
            } else if (
                    permission.equals("android.permission.POST_NOTIFICATIONS")) {
                addCategory(categories, RiskConstants.NOTIFICATIONS);
            } else if (
                    permission.equals("android.permission.ACCESS_FINE_LOCATION")
                            || permission.equals("android.permission.ACCESS_COARSE_LOCATION")) {
                addCategory(categories, RiskConstants.LOCATION);
            } else if (
                    permission.equals("android.permission.READ_CONTACTS")
                            || permission.equals("android.permission.WRITE_CONTACTS")) {
                addCategory(categories, RiskConstants.CONTACTS);
            } else if (
                    permission.equals("android.permission.RECORD_AUDIO")) {
                addCategory(categories, RiskConstants.MICROPHONE);
            } else if (
                    permission.equals("android.permission.READ_SMS")
                            || permission.equals("android.permission.SEND_SMS")
                            || permission.equals("android.permission.RECEIVE_SMS")) {
                addCategory(categories, RiskConstants.SMS);
            } else if (
                    permission.equals("android.permission.READ_PHONE_STATE")
                            || permission.equals("android.permission.CALL_PHONE")
                            || permission.equals("android.permission.READ_CALL_LOG")) {
                addCategory(categories, RiskConstants.PHONE);
            }
        }

        return categories;
    }

    private void addCategory(List<String> categories, String category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public List<String> getGrantedSensitiveCategories(
            List<String> grantedPermissions) {
        return getSensitiveCategories(grantedPermissions);
    }
}