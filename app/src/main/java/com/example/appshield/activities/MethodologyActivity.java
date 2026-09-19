package com.example.appshield.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ⚠️  PLACEHOLDER — this is Shreya's screen.
 *
 * This file exists ONLY so the project compiles while Shreya's real
 * MethodologyActivity is not yet merged.
 *
 * When Shreya's branch is merged:
 *   1. Delete this file.
 *   2. Shreya's MethodologyActivity (same package, same class name) will take over.
 *   3. No changes are needed in AboutActivity or AndroidManifest.xml — they already
 *      reference this class name/path.
 *
 * DO NOT put real methodology content here.
 */
public class MethodologyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Minimal layout — just a placeholder message.
        TextView tv = new TextView(this);
        tv.setText("Risk Methodology — coming soon (Shreya's screen)");
        tv.setPadding(48, 48, 48, 48);
        setContentView(tv);

        // Enable the Up / Back button.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Risk Methodology");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
