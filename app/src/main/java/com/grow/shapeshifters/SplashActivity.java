package com.grow.shapeshifters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler to start the Menu-Activity and close this Splash-Screen after some seconds.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start app main activity.
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);

                // Close this activity.
                finish();
            }
        }, 4000);
    }
}