package com.example.msiarpa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    MaterialButton start;
    RelativeLayout progressbar;
    TextView web;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.get_started);
        progressbar = findViewById(R.id.progress_bar);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedToInternet()) {
                    progressbar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        progressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Start Log in", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                    }, 100);
                } else {
                    Toast.makeText(MainActivity.this, "Please connect to the internet first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!= null && networkInfo.isConnected();
    }
}