package com.example.msiarpa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class forgetpassword extends AppCompatActivity {
Button next;
RelativeLayout progressbar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        next = findViewById(R.id.nextPasswordForget);
        progressbar = findViewById(R.id.progress_bar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(()->{

                    progressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(forgetpassword.this, "Reset password", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(forgetpassword.this, otpEmail.class);
                    startActivity(intent);
                }, 500);
            }
        });


    }

  //--fun click(view: View){
        //val dialog = Dialog(this)
               //* dialod.setContentView(R.layout.dialog_loadng)
                       // dialog.cancelable(false)

                        //dialog.show()
   // }
}