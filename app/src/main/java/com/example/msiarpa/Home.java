package com.example.msiarpa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.msiarpa.HomeFragment;
import com.example.msiarpa.Login;
import com.example.msiarpa.R;
import com.example.msiarpa.SearchFragment;
import com.example.msiarpa.notificationFragment;
import com.example.msiarpa.profileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Home extends AppCompatActivity {

    private MeowBottomNavigation meowBottomNavigation;
    private TextView userEmailTextView;
    private LinearLayout logoutLayout;

    protected final static int home =1;
    protected final static int notification =3;
    protected final static int search =2;
    protected final static int setting=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userEmailTextView = findViewById(R.id.user_email);
        logoutLayout = findViewById(R.id.linear_logout);

        meowBottomNavigation =findViewById(R.id.meowBottomNavigation);

        meowBottomNavigation.add(new MeowBottomNavigation.Model(home,R.drawable.home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(search,R.drawable.search));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(notification,R.drawable.notification));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(setting,R.drawable.settings));

        //set current selected fragment
        meowBottomNavigation.show(home, true);

        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                return null;
            }
        });

        meowBottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                Fragment fragment=null;
                if (model.getId()==1){
                    fragment = new HomeFragment();
                } else if (model.getId()==2) {

                    fragment=new SearchFragment();

                } else if (model.getId()==3) {
                    fragment=new notificationFragment();
                }else fragment=new profileFragment();

                //loading fragment
                LoadAndRelodFragment(fragment);
                return null;
            }
        });

        // Set user email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmailTextView.setText(user.getEmail());
        }

        // Set logout button click listener
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout logic here
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void LoadAndRelodFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment,null)
                .commit();
    }
}