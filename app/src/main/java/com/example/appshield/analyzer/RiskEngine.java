package com.example.appshield.analyzer;

import com.example.appshield.utils.RiskConstants;

import java.util.ArrayList;
import java.util.List;

public class RiskEngine {

    public int calculateScore(List<String> categories) {

        int score = 0;

        for (String category : categories) {

            switch (category) {

                case RiskConstants.CAMERA:
                    score += RiskConstants.CAMERA_POINTS;
                    break;

                case RiskConstants.STORAGE_MEDIA:
                    score += RiskConstants.STORAGE_MEDIA_POINTS;
                    break;

                case RiskConstants.NOTIFICATIONS:
                    score += RiskConstants.NOTIFICATIONS_POINTS;
                    break;

                case RiskConstants.LOCATION:
                    score += RiskConstants.LOCATION_POINTS;
                    break;

                case RiskConstants.CONTACTS:
                    score += RiskConstants.CONTACTS_POINTS;
                    break;

                case RiskConstants.MICROPHONE:
                    score += RiskConstants.MICROPHONE_POINTS;
                    break;

                case RiskConstants.SMS:
                    score += RiskConstants.SMS_POINTS;
                    break;

                case RiskConstants.PHONE:
                    score += RiskConstants.PHONE_POINTS;
                    break;
            }
        }

        return score;
    }

    public String getRiskLevel(int score) {

        if (score <= 3) {
            return RiskConstants.LOW;
        }

        else if (score <= 7) {
            return RiskConstants.MEDIUM;
        }

        else {
            return RiskConstants.HIGH;
        }
    }

    public List<String> getRiskReasons(List<String> categories) {

        List<String> reasons = new ArrayList<>();

        for (String category : categories) {

            int points = getPointsForCategory(category);

            reasons.add(category + " (+" + points + ")");
        }

        return reasons;
    }

    private int getPointsForCategory(String category) {

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