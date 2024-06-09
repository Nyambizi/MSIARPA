package com.example.msiarpa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    // declaration
    TextView visitRegister, forgetpassword;
    RelativeLayout progressbar;

    MaterialButton login;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgetpassword = findViewById(R.id.forgotpassword);
        visitRegister = findViewById(R.id.register);
        progressbar = findViewById(R.id.progress_bar);
        login = findViewById(R.id.login);
        EditText emailEditText = findViewById(R.id.LoginEmail);
        EditText passwordEditText = findViewById(R.id.LoginPassword);
        RadioGroup radioGroup = findViewById(R.id.genderRadioGroup);

        // Set up click listener for the login button
        login.setOnClickListener(v -> {
            String email = String.valueOf(emailEditText.getText()).trim();
            String password = String.valueOf(passwordEditText.getText()).trim();
            int radioButtonId = radioGroup.getCheckedRadioButtonId();

            if (radioButtonId == -1) {
                Toast.makeText(this, "Please select your role (Student/Instructor)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.isEmpty() && !password.isEmpty()) {
                signInWithEmailAndPassword(email, password, radioButtonId);
            } else {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
            }
        });

        visitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    progressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Start Registration", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                }, 150);
            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    progressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Welcome to change password", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, forgetpassword.class);
                    startActivity(intent);
                }, 500);
            }
        });
    }

    private void signInWithEmailAndPassword(String email, String password, int radioButtonId) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful, navigate to the appropriate activity
                        if (radioButtonId == R.id.students) {
                            Toast.makeText(this, "Welcome to Student Dashboard", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                            finish();
                        } else if (radioButtonId == R.id.Teachers) {
                            Toast.makeText(this, "Welcome to Supervisor", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, supervisorDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Login failed. Please check your email and password", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}