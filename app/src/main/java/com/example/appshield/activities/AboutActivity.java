package com.example.appshield.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appshield.R;

/**
 * AboutActivity — Settings / About screen for AppShield.
 *
 * Displays:
 *   - App name and tagline
 *   - What AppShield does (About section)
 *   - Disclaimer (not an antivirus, heuristic score, etc.)
 *   - Project Information (team, tech stack)
 *   - "View Risk Methodology" button → opens MethodologyActivity
 *
 * Navigation:
 *   - Called from the Dashboard via: startActivity(new Intent(this, AboutActivity.class))
 *   - The Toolbar Up arrow and the system Back button both finish this activity.
 *
 * Note: The global theme is NoActionBar, so we inflate our own Toolbar and
 *       register it as the support action bar.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // ── Toolbar setup (NoActionBar theme requires manual registration) ──
        Toolbar toolbar = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbar);

        // Enable the Up / Back arrow in the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // ── View Risk Methodology button ─────────────────────────────────────
        Button btnViewMethodology = findViewById(R.id.btnViewMethodology);
        btnViewMethodology.setOnClickListener(v -> {
            // Explicit intent as required by the task spec.
            // MethodologyActivity is Shreya's screen.
            Intent intent = new Intent(this, MethodologyActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Handles the Toolbar Up button (back arrow).
     * Returning true tells Android we handled the event; the activity finishes.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // same as pressing the system Back button
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
