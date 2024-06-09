package com.example.msiarpa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.msiarpa.Utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    Button BtnVerifyOTP, BtnResendOTP;
    MaterialTextView TxtViewVerifiedPhoneNo;
    TextView TxtViewPhoneNumber;
    PinView PinViewVerifyOTP;


    String name, email, password, gender, phone,VerificationCode, userId;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Long timeoutSeconds = 60L;
    PhoneAuthProvider.ForceResendingToken ResendingToken;



    private TextView resend;

    RelativeLayout progressbar;

    public otp() {
    }

    @SuppressLint({"WrongViewCast", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Get the data from the previous intent
        Intent intent = getIntent();
         name = intent.getStringExtra("name");
         email = intent.getStringExtra("email");
         gender = intent.getStringExtra("gender");
         phone = intent.getStringExtra("phone");
         password = intent.getStringExtra("password");


        TxtViewPhoneNumber = findViewById(R.id.phone);
        TxtViewPhoneNumber.setText("OTP sent to: " + phone);

        BtnVerifyOTP = findViewById(R.id.verifyOTP);
        PinViewVerifyOTP = findViewById(R.id.PinViewVerifyOTP);
        BtnResendOTP = findViewById(R.id.didintReceiveOtp);


        sendOTP(phone, false);

        BtnVerifyOTP.setOnClickListener(v -> {
            String enteredOTP = PinViewVerifyOTP.getText().toString();
            if (enteredOTP.length() == 6) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationCode, enteredOTP);
                signIn(credential);

                setInProgress(true);
            }


        });


        BtnResendOTP.setOnClickListener(v -> {

            sendOTP(phone, true);

        });
    }


    void sendOTP(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                signIn(phoneAuthCredential);
                                setInProgress(false);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");
                                setInProgress(false);

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                VerificationCode = s;
                                setInProgress(false);
                                ResendingToken = forceResendingToken;

                                AndroidUtil.showToast(getApplicationContext(), "OTP sent successfully");

                                // Automatically fill the OTP in the PinView
                                PinViewVerifyOTP.setText(s);
                            }
                        });

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }


    void setInProgress(boolean inProgress) {
        if (inProgress) {
            BtnVerifyOTP.setVisibility(View.INVISIBLE);
            BtnResendOTP.setVisibility(View.INVISIBLE);

        } else {
            BtnVerifyOTP.setVisibility(View.VISIBLE);
            BtnResendOTP.setVisibility(View.VISIBLE);

        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential) {
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a new user account with email and password

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                // The user account has been successfully created
                                Map<String, Object> UsersData = getUsersData();
                                db.collection("students").document(userId).set(UsersData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(otp.this, Login.class);

                                        intent.putExtra("UserID", userId);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AndroidUtil.showToast(getApplicationContext(), "Failed to upload data to Firestore");
                                    }
                                });
                            } else {
                                // The user account creation has failed
                                AndroidUtil.showToast(getApplicationContext(), "Failed to create user account");
                            }
                        }
                    });
                } else {
                    AndroidUtil.showToast(getApplicationContext(), "OTP verification failed");

                }
            }

            @NonNull
            private Map<String, Object> getUsersData() {
                Map<String, Object> UsersData = new HashMap<>();

                // Roles and permissions
                UsersData.put("Students", true);
                UsersData.put("Teacher", false);


                // Personal information
                UsersData.put("Gender", gender);
                UsersData.put("phoneNumber", phone);

                // Sensitive information should be handled carefully
                UsersData.put("Password", password);

                // Group related fields together for better readability
                UsersData.put("Email", email);
                UsersData.put("Name", name);

                return UsersData;
            }
        });
    }


    void startResendTimer() {
        BtnResendOTP.setEnabled(false);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                final String text = "Resend OTP in " + timeoutSeconds + " seconds";

                runOnUiThread(() -> {
                    BtnResendOTP.setText(text);

                    if (timeoutSeconds <= 0) {
                        timeoutSeconds = 60L;
                        timer.cancel();
                        BtnResendOTP.setEnabled(true);
                        BtnResendOTP.setText("Resend OTP");
                    }
                });
            }
        }, 0, 1000);
    }



}
