package com.example.megamart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.megamart.explore.ExploreFragment;
import com.example.megamart.managefunds.ManageFundsActivity;
import com.example.megamart.myprofile.MyProfileFragment;
import com.example.megamart.plog.LalbindiFragment;
import com.example.megamart.plog.PloggingFragment;
import com.example.megamart.plog.SocialShelfFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    NavigationView navigationView;

    ExploreFragment exploreFragment = new ExploreFragment();
    SocialShelfFragment socialShelfFragment = new SocialShelfFragment();
    LalbindiFragment lalbindiFragment = new LalbindiFragment();
    PloggingFragment ploggingFragment = new PloggingFragment();
    MyProfileFragment myProfileFragment = new MyProfileFragment();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FCM", "Subscribed to topic");
                    }
                });


        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

//            if (itemId == R.id.menuNavAdministration) {
//                Toast.makeText(HomeActivity.this, "Administration", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.this, AdministrationActivity.class));
//            }
           if (itemId == R.id.menuNavOE) {
                Toast.makeText(HomeActivity.this, "Organize Event", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, OrganizeEventActivity.class));
            } else if (itemId == R.id.menuNavSA) {
                Toast.makeText(HomeActivity.this, "Send Alerts", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, SendAlertsActivity.class));
//            } else if (itemId == R.id.menuNavVI) {
//                Toast.makeText(HomeActivity.this, "View Insights", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(HomeActivity.this, ViewInsightsActivity.class));
            } else if (itemId == R.id.menuNavMF) {
                Toast.makeText(HomeActivity.this, "Manage Funds", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, ManageFundsActivity.class));
            }

            drawerLayout.closeDrawers();
            return true;
        });

        // Drawer Toggle (Hamburger icon)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeFrameLayout, ploggingFragment).commit();

        bottomNavigationView = findViewById(R.id.homeBottomNav);
        bottomNavigationView.setSelectedItemId(R.id.menuHomeNavPlogging);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_item_aboutus) {
            Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AboutUsActivity.class));
            return true;
        } else if (itemId == R.id.menu_item_settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (itemId == R.id.menu_item_help) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HelpActivity.class));
            return true;
        } else if (itemId == R.id.menu_item_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Logout", (dialog, which) -> {
                        // Clear login session
                        SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        // Go to LoginActivity and clear back stack
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Finish HomeActivity so user can't go back to it
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuHomeNavExplore) {
            replaceFragment(exploreFragment);
        } else if (itemId == R.id.menuHomeNavPlogging) {
            replaceFragment(ploggingFragment);
        } else if (itemId == R.id.menuHomeNavLalbindi) {
            replaceFragment(lalbindiFragment);
        } else if (itemId == R.id.menuHomeNavSocialShelf) {
            replaceFragment(socialShelfFragment);
        } else if (itemId == R.id.menuHomeNavMyprofile) {
            replaceFragment(myProfileFragment);
        }

        return true;
    }

    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.homeFrameLayout, fragment).commit();
    }
}
