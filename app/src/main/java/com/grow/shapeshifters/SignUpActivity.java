package com.grow.shapeshifters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.grow.shapeshifters.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signupBtn.setOnClickListener(v -> {
            dbHelper = new DatabaseHelper(this);
            String email = binding.signupEmail.getText().toString();
            String firstname = binding.signupFirstname.getText().toString();
            String lastname = binding.signupLastname.getText().toString();
            String password = binding.signupPassword.getText().toString();
            boolean type = binding.signupTypeCheckBox.isChecked();

            if (dbHelper.signup(firstname, lastname, email, password, type)) {
                Toast.makeText(SignUpActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignUpActivity.this, "Registration Failed. Fill all fields.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.signupToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}
