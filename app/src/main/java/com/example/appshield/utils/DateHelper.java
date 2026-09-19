package com.example.appshield.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateHelper provides simple date/time utilities for AppShield.
 * Used by DatabaseHelper to timestamp each scan record automatically.
 */
public class DateHelper {

    // Prevent instantiation — all methods are static.
    private DateHelper() {}

    /**
     * Returns the current date and time as a formatted string.
     * Example output: "19 Sep 2026, 05:30 PM"
     *
     * Uses Locale.getDefault() so the format adapts to the device locale.
     */
    public static String now() {
        // Pattern: day (no leading zero) + abbreviated month + year, hour:minute AM/PM
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy, hh:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }
}
