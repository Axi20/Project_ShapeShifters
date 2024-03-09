package com.grow.shapeshifters;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import com.google.android.material.navigation.NavigationView;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.grow.shapeshifters.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.logoutBtn.setOnClickListener(view -> {
            // Directly use getSharedPreferences to update the login state.
            SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", false);
            editor.apply();

            // Create an intent to start the LoginActivity.
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // Clear the task and start a new task with LoginActivity.
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // Call finish to ensure this activity is closed.
            finish();
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Retrieve the header view at index 0 (the first header view if there are multiple)
        View headerView = navigationView.getHeaderView(0);
        // Find the TextViews for username and email within the header
        TextView userNameTextView = headerView.findViewById(R.id.nav_name);
        TextView userEmailTextView = headerView.findViewById(R.id.nav_email);

        // Retrieve the username and email from SharedPreferences
        String firstname = getFirstname();
        String lastname = getLastname();
        String email = getEmail();

        // Concatenate the firstname and lastname for the full name
        String fullName = firstname + " " + lastname;

        // Update the TextViews with the retrieved username and email
        userNameTextView.setText(fullName);
        userEmailTextView.setText(email);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    /**
     * Retrieves the firstname of the currently logged-in user.
     *
     * @return The firstname of the user, or an empty string if not found.
     */
    public String getFirstname() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("Firstname", "");
    }

    /**
     * Retrieves the lastname of the currently logged-in user.
     *
     * @return The lastname of the user, or an empty string if not found.
     */
    public String getLastname() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("Lastname", "");
    }

    /**
     * Retrieves the email of the currently logged-in user.
     *
     * @return The email of the user, or an empty string if not found.
     */
    public String getEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}