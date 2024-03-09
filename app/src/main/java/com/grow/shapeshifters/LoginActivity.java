package com.grow.shapeshifters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.grow.shapeshifters.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding  binding;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dbHelper = new DatabaseHelper(this);

        // Handle user login.
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.loginEmail.getText().toString();
            String password = binding.loginPassword.getText().toString();

            if (dbHelper.login(email, password)) {
                saveLoginState(true);
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Navigate to the MainActivity.
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                // To prevent the user from returning to the login screen.
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Login Failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle forgot password click.
        binding.loginToNewPass.setOnClickListener(v -> {
            //TODO Navigate users to the forgotPasswordActivity once it is done.
        });

        // Handle sign up click.
        binding.loginToSignup.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Persists the user's login state within the application's shared preferences.
     * This method uses Android's SharedPreferences to save the user's login state, allowing
     * the application to maintain state across different activities or sessions.
     * By storing a boolean value under the key "IsLoggedIn", the application can quickly check
     * the user's login status at any point. The preferences are stored in a private mode,
     * meaning they are accessible only to this application.
     * This method is typically called after a user successfully logs in or logs out
     * to update their current login state.
     *
     * @param isLoggedIn A boolean value representing the user's login state;
     * true if the user is logged in, false otherwise.
     */
    public void saveLoginState(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsLoggedIn", isLoggedIn);
        editor.apply();
    }
}
