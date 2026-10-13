
package com.example.appshield.analyzer;

import com.example.appshield.utils.RiskConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RiskEngine {

    public int calculateScore(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return 0;
        }

        int score = 0;
        Set<String> uniqueCategories = new HashSet<>(categories);

        for (String category : uniqueCategories) {
            score += getPointsForCategory(category);
        }

        return score;
    }

    public String getRiskLevel(int score) {
        if (score <= 3) {
            return RiskConstants.LOW;
        } else if (score <= 7) {
            return RiskConstants.MEDIUM;
        } else {
            return RiskConstants.HIGH;
        }
    }

    public List<String> getRiskReasons(List<String> categories) {
        List<String> reasons = new ArrayList<>();

        if (categories == null || categories.isEmpty()) {
            return reasons;
        }

        Set<String> uniqueCategories = new LinkedHashSet<>(categories);

        for (String category : uniqueCategories) {
            int points = getPointsForCategory(category);

            if (points > 0) {
                reasons.add(category + " (+" + points + ")");
            }
        }

        return reasons;
    }

    private int getPointsForCategory(String category) {
        if (category == null) {
            return 0;
        }

        switch (category) {
            case RiskConstants.CAMERA:
                return RiskConstants.CAMERA_POINTS;

            case RiskConstants.STORAGE_MEDIA:
                return RiskConstants.STORAGE_MEDIA_POINTS;

            case RiskConstants.NOTIFICATIONS:
                return RiskConstants.NOTIFICATIONS_POINTS;

            case RiskConstants.LOCATION:
                return RiskConstants.LOCATION_POINTS;

            case RiskConstants.CONTACTS:
                return RiskConstants.CONTACTS_POINTS;

            case RiskConstants.MICROPHONE:
                return RiskConstants.MICROPHONE_POINTS;

            case RiskConstants.SMS:
                return RiskConstants.SMS_POINTS;

            case RiskConstants.PHONE:
                return RiskConstants.PHONE_POINTS;

            default:
                return 0;
        }
    }
}
