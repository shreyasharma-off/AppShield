package com.example.appshield.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.appshield.R;
import com.example.appshield.fragments.DashboardFragment;
import com.example.appshield.fragments.InstalledAppsFragment;
import com.example.appshield.fragments.ScanHistoryFragment;
import com.example.appshield.activities.AboutActivity;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_overview) {
                loadFragment(new DashboardFragment(), false);
                return true;
            } else if (id == R.id.nav_apps) {
                loadFragment(new InstalledAppsFragment(), false);
                return true;
            } else if (id == R.id.nav_history) {
                loadFragment(new ScanHistoryFragment(), false);
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, AboutActivity.class));
                return false; // Don't select the item if opening a new activity
            }
            return false;
        });

        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment(), false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        var transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment);
        
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
    
    public void setSelectedNavItem(int itemId) {
        bottomNavigation.setSelectedItemId(itemId);
    }
}
