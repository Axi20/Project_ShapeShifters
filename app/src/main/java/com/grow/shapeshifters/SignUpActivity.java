package com.grow.shapeshifters;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText emailEditText = findViewById(R.id.signup_email);
        EditText firstnameEditText = findViewById(R.id.signup_firstname);
        EditText lastnameEditText = findViewById(R.id.signup_lastname);
        EditText passwordEditText = findViewById(R.id.signup_password);
        TextView signupToLogin = findViewById(R.id.signup_to_login);
        CheckBox accountType = findViewById(R.id.signup_type_checkBox);
        Button signupBtn = findViewById(R.id.signup_btn);

        signupBtn.setOnClickListener(v -> {
            //TODO Implement Sign up logic here.
        });

        signupToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}
