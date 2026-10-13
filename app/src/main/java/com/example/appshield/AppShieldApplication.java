
package com.example.appshield;

import android.app.Application;

public class AppShieldApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        getSharedPreferences("appshield_session", MODE_PRIVATE)
                .edit()
                .putBoolean("scan_completed", false)
                .apply();
    }
}
