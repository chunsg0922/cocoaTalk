package com.samil.cocoatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.samil.cocoatalk.databinding.ActivityMainBinding;


import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setT
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_friend, R.id.navigation_chat, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //passPushTokenToServer();


    }

    void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = null;

        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).updateChildren(map);


    }
}