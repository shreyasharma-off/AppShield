package com.example.appshield.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appshield.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView btnBack = findViewById(R.id.btn_back);
        RelativeLayout btnAboutInfo = findViewById(R.id.btn_about_info);
        RelativeLayout btnViewMethodology = findViewById(R.id.btn_view_methodology);

        btnBack.setOnClickListener(v -> finish());

        btnAboutInfo.setOnClickListener(v -> showAboutDialog());

        btnViewMethodology.setOnClickListener(v -> {
            startActivity(new Intent(AboutActivity.this, MethodologyActivity.class));
        });
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About AppShield")
                .setMessage("AppShield is a college MAD mini-project designed to analyze installed applications for potential security and privacy risks.\n\n" +
                        "Team Members:\n" +
                        "• Shreya: Permission Analysis & Risk Logic\n" +
                        "• Ankit: UI & Navigation Foundation\n" +
                        "• Prem: SQLite Database & Scan History")
                .setPositiveButton("OK", null)
                .show();
    }
}
