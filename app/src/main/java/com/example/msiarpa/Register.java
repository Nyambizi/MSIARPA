package com.example.msiarpa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class Register extends AppCompatActivity {

    TextInputLayout fullname, email, phonenumber, password, comfirmpassword;
    MaterialButton register_to_DB;
    String gender, completePhoneNumber;



    CountryCodePicker countryCodePicker;
    TextView login;
    RelativeLayout progressbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        fullname = findViewById(R.id.Fullname);
        email = findViewById(R.id.Registeremail);
        phonenumber = findViewById(R.id.phone);
        password = findViewById(R.id.RegisterPassword);
        comfirmpassword = findViewById(R.id.Comfirm_password);
        register_to_DB = findViewById(R.id.register);

        progressbar = findViewById(R.id.progress_bar);
        login = findViewById(R.id.GoToLogin);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> {

                    progressbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Register.this, "Start Log in", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                }, 500);
            }
        });
        //passing value to otp
        register_to_DB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatingFullname() | !validatingPhoneNumber() | !validatingEmail() | !validateGender() | !validatingPassword() | !validatingConfirmPassword()) {
                    return;
                }

                // Store the captured values in variables
                String name = fullname.getEditText().getText().toString().trim();
                String emailAddress = email.getEditText().getText().toString().trim();
                String Gender = gender;
                String phoneNumber = completePhoneNumber;
                String passwordText = password.getEditText().getText().toString().trim();

                // Pass the captured values to the next activity (otp.class) as extras
                Intent intent = new Intent(Register.this, otp.class);
                intent.putExtra("name", name);
                intent.putExtra("email", emailAddress);
                intent.putExtra("gender", Gender);
                intent.putExtra("phone", phoneNumber);
                intent.putExtra("password", passwordText);

                // Start the otp activity
                startActivity(intent);
            }
        });



    }

    private boolean validatingFullname() {
        String name = fullname.getEditText().getText().toString().trim();
        String SpaceCheck = "\\A\\w{4,20}\\z";

        if (name.isEmpty()) {
            fullname.setError("name cannot be empty");
            return false;


        } else if (name.matches(".*\\d+.*")) {
            fullname.setError("your name should not contain numbers");
            return false;

        } else if (name.length() > 25) {
            fullname.setError("Your name is too large");
            return false;

        } else if (!name.matches(SpaceCheck)) {
            fullname.setError("white space is not allowed");
            return false;

        } else {
            fullname.setError(null);
            fullname.setEnabled(false);
            return true;
        }

    }


    private boolean validatingEmail() {
        String mail = email.getEditText().getText().toString().trim();
        String emailCheck = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (mail.isEmpty()) {
            email.setError("name cannot be empty");
            return false;


        } else if (!mail.matches(emailCheck)) {
            email.setError("Invalid Email ");
            return false;


        } else {
            email.setError(null);
            email.setEnabled(false);
            return true;
        }

    }



    private boolean validatingPhoneNumber() {
        String phoneNumber = phonenumber.getEditText().getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            phonenumber.setError("Phone number cannot be empty");
            return false;
        }  else {
            phonenumber.setError(null);
            phonenumber.setEnabled(false);
        }

        // Get the selected country code
        CountryCodePicker countryCodePicker = findViewById(R.id.countryCodePicker);
        String countryCode = countryCodePicker.getSelectedCountryCode();

        // Modify the phone number
        phoneNumber = phoneNumber.replaceAll("^0+", ""); // Remove any zero from the start
        if (phoneNumber.startsWith("+")) {
            phoneNumber = phoneNumber.substring(1); // Remove the plus sign
            if (phoneNumber.length() >= 12) {
                phoneNumber = phoneNumber.substring(0, 3); // Remove the country code
            }
        }

        // Check if the remaining numbers equal to 9
        if (phoneNumber.length() != 9) {
            phonenumber.setError("Invalid phone number format. Should have 9 digits.");
            return false;
        }

        // Generate a complete valid phone number
        completePhoneNumber = "+" + countryCode + phoneNumber;

        // Display the complete valid phone number
        Toast.makeText(this, completePhoneNumber, Toast.LENGTH_SHORT).show();

        return true;
    }

    private boolean validatingPassword() {
        String pass = password.getEditText().getText().toString().trim();
        String passCheck = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";

        if (pass.isEmpty()) {
            password.setError("Your password must include characters, letters, numbers,Capital letter");
            return false;

        } else {
            password.setError(null);
            password.setEnabled(false);
            return true;
        }

    }

    private boolean validatingConfirmPassword() {
        String confirmPass = comfirmpassword.getEditText().getText().toString().trim();
        String passwordText = password.getEditText().getText().toString().trim();

        if (confirmPass.isEmpty()) {
            comfirmpassword.setError("Confirm password cannot be empty");
            return false;
        } else if (!confirmPass.equals(passwordText)) {
            comfirmpassword.setError("Passwords do not match");
            return false;
        } else {
            comfirmpassword.setError(null);
            comfirmpassword.setEnabled(false);
            return true;
        }
    }

    private boolean validateGender() {
        // Get the selected gender
         gender = "";
        RadioGroup radioGroupGender;
        radioGroupGender = findViewById(R.id.genderRadioGroup);
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == -1) {
            // Set error message for the first radio button in the group

            RadioButton radioButton = radioGroupGender.findViewById(R.id.maleRadioButton);
            radioButton.setError("Please select a gender");
            return false;
        } else {
            if (selectedId == R.id.maleRadioButton) {
                gender = "Male";
            } else if (selectedId == R.id.femaleRadioButton) {
                gender = "Female";
            } else if (selectedId == R.id.otherRadioButton) {
                gender = "Other";
            }

            // Reset the error message for the first radio button in the group

            RadioButton radioButton = radioGroupGender.findViewById(R.id.maleRadioButton);
            radioButton.setError(null);

        }

        return true;

    }
}