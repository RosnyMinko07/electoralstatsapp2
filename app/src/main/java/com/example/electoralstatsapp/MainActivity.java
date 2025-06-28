package com.example.electoralstatsapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.electoralstatsapp.fragments.FragmentNotifications;
import com.example.electoralstatsapp.fragments.Fragment_resultats_des_elections;
import com.example.electoralstatsapp.fragments.WelcomeFragment;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View progressBar = findViewById(R.id.progress_global_loading);
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.checkAndAddInitialUsers();

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new WelcomeFragment())
                    .commit();
        }

        // Masquer le loader après un court délai (ou après le vrai chargement si besoin)
        if (progressBar != null) {
            progressBar.postDelayed(() -> progressBar.setVisibility(View.GONE), 900);
        }
    }

    public void showBottomNav() {
        bottomNav.setVisibility(View.VISIBLE);
    }

    public void hideBottomNav() {
        bottomNav.setVisibility(View.GONE);
    }

    private final NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_home) {
                        selectedFragment = new Fragment_resultats_des_elections();
                    } else if (itemId == R.id.nav_mail) {
                        selectedFragment = new FragmentNotifications();
                    } else {
                        return false;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
            };
}
