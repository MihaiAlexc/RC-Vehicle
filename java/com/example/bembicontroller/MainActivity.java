package com.example.bembicontroller;

import android.Manifest; // IMPORT NOU
import android.content.pm.PackageManager; // IMPORT NOU
import android.os.Build; // IMPORT NOU
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat; // IMPORT NOU
import androidx.core.content.ContextCompat; // IMPORT NOU
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // --- ÎNCEPUT COD PERMISIUNI (Adăugat pentru Android 12+) ---
        // Verificăm dacă telefonul are Android 12 sau mai nou
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Verificăm dacă NU avem permisiunea de scanare acordată
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {

                // Cerem permisiunile (Scan și Connect)
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT
                        }, 100);
            }
        }
        // --- SFÂRȘIT COD PERMISIUNI ---

        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        // 1. Când deschidem aplicația, afișăm direct pagina HOME
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        // 2. Ascultăm click-urile pe meniul de jos
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                // Verificăm ce buton a fost apăsat și alegem fragmentul potrivit
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_gamepad) {
                    selectedFragment = new GamepadFragment();
                } else if (itemId == R.id.nav_bluetooth) {
                    selectedFragment = new BluetoothFragment();
                } else if (itemId == R.id.nav_settings) {
                    selectedFragment = new SettingsFragment();
                }

                // Schimbăm pagina pe ecran
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }

                return true;
            }
        });
    }
}