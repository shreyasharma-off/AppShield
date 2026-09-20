package com.example.appshield;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This root MainActivity now redirects to the one in the activities package.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, com.example.appshield.activities.MainActivity.class);
        startActivity(intent);
        finish();
    }
}
