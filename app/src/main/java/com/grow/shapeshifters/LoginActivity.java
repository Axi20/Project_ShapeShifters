package com.grow.shapeshifters;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.login_email);
        EditText passwordEditText = findViewById(R.id.login_password);
        Button loginButton = findViewById(R.id.login_btn);
        TextView forgotPasswordTextView = findViewById(R.id.login_to_new_pass);
        TextView signUpTextView = findViewById(R.id.login_to_signup);


        loginButton.setOnClickListener(view -> {
            //TODO Implement login logic here
        });

        // Handle forgot password click
        forgotPasswordTextView.setOnClickListener(view -> {
            //TODO Navigate users to the forgotPasswordActivity once it is done.
        });

        // Handle sign up click
        signUpTextView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
    }
}
