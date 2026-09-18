package com.example.appshield.utils;

public class RiskConstants {

    // Permission Categories
    public static final String CAMERA = "Camera";
    public static final String STORAGE_MEDIA = "Storage/Media";
    public static final String NOTIFICATIONS = "Notifications";
    public static final String LOCATION = "Location";
    public static final String CONTACTS = "Contacts";
    public static final String MICROPHONE = "Microphone";
    public static final String SMS = "SMS";
    public static final String PHONE = "Phone";

    // Risk Points
    public static final int CAMERA_POINTS = 1;
    public static final int STORAGE_MEDIA_POINTS = 1;
    public static final int NOTIFICATIONS_POINTS = 1;
    public static final int LOCATION_POINTS = 2;
    public static final int CONTACTS_POINTS = 2;
    public static final int MICROPHONE_POINTS = 2;
    public static final int SMS_POINTS = 3;
    public static final int PHONE_POINTS = 3;

    // Risk Levels
    public static final String LOW = "Low";
    public static final String MEDIUM = "Medium";
    public static final String HIGH = "High";

    private RiskConstants() {
        // Prevent object creation
    }
}