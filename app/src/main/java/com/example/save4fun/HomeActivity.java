package com.example.save4fun;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.save4fun.fragment.FavouriteFragment;
import com.example.save4fun.fragment.HomeFragment;
import com.example.save4fun.fragment.ListFragment;
import com.example.save4fun.fragment.ProductFragment;
import com.example.save4fun.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigation;
    Toolbar toolbar;
    FloatingActionButton fabHome;

    private static final int HOME_FRAGMENT = 0;
    private static final int PROFILE_FRAGMENT = 1;
    private int currentFragment = HOME_FRAGMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Handling toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Handling navigation drawer
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.navHome);
        }

        // Handling bottom navigation
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setBackground(null);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navList) {
                    replaceFragment(new ListFragment());
                    return true;
                } else if (id == R.id.navProduct) {
                    replaceFragment(new ProductFragment());
                    return true;
                } else if (id == R.id.navFavourite) {
                    replaceFragment(new FavouriteFragment());
                    return true;
                }
                return false;
            }
        });

        // Handling fab button
        fabHome = findViewById(R.id.fabHome);
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HomeFragment());
            }
        });

        // Handling back pressed
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finishAffinity();
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navHome) {
            if (currentFragment != HOME_FRAGMENT) {
                replaceFragment(new HomeFragment());
                currentFragment = HOME_FRAGMENT;
            }
        } else if (id == R.id.navProfile) {
            if (currentFragment != PROFILE_FRAGMENT) {
                replaceFragment(new ProfileFragment());
                currentFragment = PROFILE_FRAGMENT;
            }
        } else if (id == R.id.navLogout) {
            // To remove SharedPreferences

        }

        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}