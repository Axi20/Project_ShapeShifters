package com.grow.shapeshifters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler to start the Menu-Activity and close this Splash-Screen after some seconds.
        new Handler().postDelayed(() -> {
            if (isUserLoggedIn()) {
                // User is logged in, redirect to main activity.
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                // Show login screen.
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 4000);
    }

    /**
     * Checks if the user is currently logged in by retrieving their login state from
     * the application's shared preferences.
     * By default, if the "IsLoggedIn" key does not exist, the method assumes
     * the user is not logged in.
     *
     * @return true if the shared preferences contain "IsLoggedIn" with a value of true,
     * indicating the user is logged in; false otherwise, indicating the user is not logged in
     * or the status is not saved.
     */
    public boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.getBoolean("IsLoggedIn", false);
    }
}