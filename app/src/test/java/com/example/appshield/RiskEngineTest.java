package com.example.appshield;

import com.example.appshield.analyzer.RiskEngine;
import com.example.appshield.utils.RiskConstants;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RiskEngineTest {

    private final RiskEngine riskEngine = new RiskEngine();

    @Test
    public void noSensitivePermissions_returnsLowRisk() {

        List<String> categories = Collections.emptyList();

        int score = riskEngine.calculateScore(categories);
        String level = riskEngine.getRiskLevel(score);

        assertEquals(0, score);
        assertEquals(RiskConstants.LOW, level);
    }

    @Test
    public void cameraAndMicrophone_returnsLowRisk() {

        List<String> categories = Arrays.asList(
                RiskConstants.CAMERA,
                RiskConstants.MICROPHONE
        );

        int score = riskEngine.calculateScore(categories);
        String level = riskEngine.getRiskLevel(score);

        assertEquals(3, score);
        assertEquals(RiskConstants.LOW, level);
    }

    @Test
    public void cameraMicrophoneLocation_returnsMediumRisk() {

        List<String> categories = Arrays.asList(
                RiskConstants.CAMERA,
                RiskConstants.MICROPHONE,
                RiskConstants.LOCATION
        );

        int score = riskEngine.calculateScore(categories);
        String level = riskEngine.getRiskLevel(score);

        assertEquals(5, score);
        assertEquals(RiskConstants.MEDIUM, level);
    }

    @Test
    public void smsPhoneLocation_returnsHighRisk() {

        List<String> categories = Arrays.asList(
                RiskConstants.SMS,
                RiskConstants.PHONE,
                RiskConstants.LOCATION
        );

        int score = riskEngine.calculateScore(categories);
        String level = riskEngine.getRiskLevel(score);

        assertEquals(8, score);
        assertEquals(RiskConstants.HIGH, level);
    }

    @Test
    public void contactsAndLocation_returnsMediumRisk() {

        List<String> categories = Arrays.asList(
                RiskConstants.CONTACTS,
                RiskConstants.LOCATION
        );

        int score = riskEngine.calculateScore(categories);
        String level = riskEngine.getRiskLevel(score);

        assertEquals(4, score);
        assertEquals(RiskConstants.MEDIUM, level);
    }
}