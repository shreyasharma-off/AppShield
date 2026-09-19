package com.example.appshield.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appshield.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button btnMethodology = findViewById(R.id.btn_risk_methodology);
        btnMethodology.setOnClickListener(v -> {
            startActivity(new Intent(AboutActivity.this, MethodologyActivity.class));
        });
    }
}
