package com.example.msiarpa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profileFragment extends Fragment {

    private TextView userNameTextView, phoneNumberTextView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userNameTextView = view.findViewById(R.id.user_name);
        phoneNumberTextView = view.findViewById(R.id.phone_number);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the current user's UID
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // Handle the case where the user is not authenticated
            Log.e("ProfileFragment", "User is not authenticated");
            return view;
        }

        String uid = user.getUid();

        // Fetch the user's data from Firestore
        db.collection("students").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document!= null) {
                        String userName = document.getString("Name");
                        String phoneNumber = document.getString("phoneNumber");

                        if (userName!= null && phoneNumber!= null) {
                            userNameTextView.setText(userName);
                            phoneNumberTextView.setText(phoneNumber);
                        } else {
                            // Handle the case where the data is null
                            Log.e("ProfileFragment", "User data is null");
                        }
                    } else {
                        // Handle the case where the document is null
                        Log.e("ProfileFragment", "Document is null");
                    }
                } else {
                    // Handle the case where the task is not successful
                    Log.e("ProfileFragment", "Task is not successful", task.getException());
                }
            }
        });

        // Set up the logout button
        RelativeLayout logoutNow = view.findViewById(R.id.logout_now);
        logoutNow.setOnClickListener(v -> {
            // Log out the user
            mAuth.signOut();
            // Navigate to the LoginActivity
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            getActivity().finish();
        });

        // Set up the Edit profile button
        AppCompatButton editProfileButton = view.findViewById(R.id.Edit_profile);
        editProfileButton.setOnClickListener(v -> {
            // Navigate to the EditProfileActivity
            Intent intent = new Intent(getActivity(), Edit_profile.class);
            startActivity(intent);
            Toast.makeText(getActivity(), "Edit Profile", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}