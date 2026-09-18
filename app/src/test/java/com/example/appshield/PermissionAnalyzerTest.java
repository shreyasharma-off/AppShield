package com.example.appshield;

import com.example.appshield.analyzer.PermissionAnalyzer;
import com.example.appshield.utils.RiskConstants;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PermissionAnalyzerTest {

    private final PermissionAnalyzer analyzer = new PermissionAnalyzer();

    @Test
    public void cameraPermission_returnsCameraCategory() {

        List<String> permissions = Arrays.asList(
                "android.permission.CAMERA"
        );

        List<String> categories =
                analyzer.getSensitiveCategories(permissions);

        assertEquals(1, categories.size());
        assertTrue(categories.contains(RiskConstants.CAMERA));
    }

    @Test
    public void microphonePermission_returnsMicrophoneCategory() {

        List<String> permissions = Arrays.asList(
                "android.permission.RECORD_AUDIO"
        );

        List<String> categories =
                analyzer.getSensitiveCategories(permissions);

        assertEquals(1, categories.size());
        assertTrue(categories.contains(RiskConstants.MICROPHONE));
    }

    @Test
    public void locationPermissions_returnOnlyOneLocationCategory() {

        List<String> permissions = Arrays.asList(
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION"
        );

        List<String> categories =
                analyzer.getSensitiveCategories(permissions);

        assertEquals(1, categories.size());
        assertTrue(categories.contains(RiskConstants.LOCATION));
    }

    @Test
    public void multipleSensitivePermissions_returnCorrectCategories() {

        List<String> permissions = Arrays.asList(
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.ACCESS_FINE_LOCATION"
        );

        List<String> categories =
                analyzer.getSensitiveCategories(permissions);

        assertEquals(3, categories.size());
        assertTrue(categories.contains(RiskConstants.CAMERA));
        assertTrue(categories.contains(RiskConstants.MICROPHONE));
        assertTrue(categories.contains(RiskConstants.LOCATION));
    }

    @Test
    public void unknownPermission_isIgnored() {

        List<String> permissions = Arrays.asList(
                "android.permission.CAMERA",
                "android.permission.SOME_UNKNOWN_PERMISSION"
        );

        List<String> categories =
                analyzer.getSensitiveCategories(permissions);

        assertEquals(1, categories.size());
        assertTrue(categories.contains(RiskConstants.CAMERA));
    }

    @Test
    public void allSensitivePermissions_returnAllCategories() {

        List<String> permissions = Arrays.asList(
                "android.permission.CAMERA",
                "android.permission.READ_MEDIA_IMAGES",
                "android.permission.POST_NOTIFICATIONS",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_CONTACTS",
                "android.permission.RECORD_AUDIO",
                "android.permission.READ_SMS",
                "android.permission.READ_PHONE_STATE"
        );

        List<String> categories =
                analyzer.getSensitiveCategories(permissions);

        assertEquals(8, categories.size());

        assertTrue(categories.contains(RiskConstants.CAMERA));
        assertTrue(categories.contains(RiskConstants.STORAGE_MEDIA));
        assertTrue(categories.contains(RiskConstants.NOTIFICATIONS));
        assertTrue(categories.contains(RiskConstants.LOCATION));
        assertTrue(categories.contains(RiskConstants.CONTACTS));
        assertTrue(categories.contains(RiskConstants.MICROPHONE));
        assertTrue(categories.contains(RiskConstants.SMS));
        assertTrue(categories.contains(RiskConstants.PHONE));
    }
}