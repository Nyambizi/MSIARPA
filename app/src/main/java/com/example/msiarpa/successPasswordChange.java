package com.example.msiarpa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class successPasswordChange extends AppCompatActivity {
Button login;
RelativeLayout progressbar;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_password_change);

        login = findViewById(R.id.Back_ToLogin);
        progressbar=findViewById(R.id.progress_bar);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(()->{

                    progressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(successPasswordChange.this, "Start Log in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(successPasswordChange.this, Login.class);
                    startActivity(intent);
                }, 500);
            }
        });
    }
}