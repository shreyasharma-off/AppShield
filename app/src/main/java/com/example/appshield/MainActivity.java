package com.example.appshield;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appshield.analyzer.AppScanner;
import com.example.appshield.models.AppInfo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Temporary developer test screen
        TextView title = new TextView(this);
        title.setText("AppShield\nScanning installed apps...");
        title.setTextSize(22);
        title.setPadding(32, 32, 32, 32);

        ScrollView scrollView = new ScrollView(this);

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(24, 24, 24, 24);

        scrollView.addView(container);
        setContentView(scrollView);

        container.addView(title);

        executor.execute(() -> {

            AppScanner scanner = new AppScanner(this);
            List<AppInfo> apps = scanner.scanApps();

            runOnUiThread(() -> {

                title.setText(
                        "AppShield\n" +
                                "Scan complete\n" +
                                "Apps found: " + apps.size()
                );

                for (AppInfo app : apps) {

                    TextView appView = new TextView(this);

                    StringBuilder text = new StringBuilder();

                    text.append("\n");
                    text.append(app.getAppName()).append("\n");
                    text.append(app.getPackageName()).append("\n");
                    text.append("Risk Score: ")
                            .append(app.getRiskScore())
                            .append("\n");
                    text.append("Risk Level: ")
                            .append(app.getRiskLevel())
                            .append("\n");

                    if (!app.getSensitiveCategories().isEmpty()) {
                        text.append("Sensitive Categories: ")
                                .append(app.getSensitiveCategories())
                                .append("\n");
                    }

                    if (!app.getRiskReasons().isEmpty()) {
                        text.append("Reasons: ")
                                .append(app.getRiskReasons())
                                .append("\n");
                    }

                    appView.setText(text.toString());
                    appView.setTextSize(16);
                    appView.setPadding(16, 16, 16, 16);

                    container.addView(appView);
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}