package com.samil.cocoatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.samil.cocoatalk.fragment.ChatFragment;
import com.samil.cocoatalk.fragment.PeopleFragment;
import com.samil.cocoatalk.fragment.ProfileFragment;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity {

    PeopleFragment peopleFragment;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        peopleFragment = new PeopleFragment();
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();





    }
}

//
//    BottomNavigationView bottomNavigationView = findViewById(R.id.mainactivity_bottomnavigationview);
//    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//            R.id.navigation_friend, R.id.navigation_chat, R.id.navigation_profile)
//            .build();
//    NavController navController = Navigation.findNavController(this, R.id.mainactivity_framelayout);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//                NavigationUI.setupWithNavController(bottomNavigationView, navController);


//    BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.mainactivity_bottomnavigationview);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//@SuppressLint("NonConstantResourceId")
//@Override
//public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//
//        int id = item.getItemId();
//
//        if(id == R.id.navigation_friend){
//        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, peopleFragment).commit();
//        }
////                switch (item.getItemId()){
////                    case R.id.navigation_friend:
////                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, peopleFragment).commit();
////                        return true;
////                    case R.id.navigation_chat:
////                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new ChatFragment()).commit();
////                        return true;
////                    case R.id.navigation_profile:
////                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new ProfileFragment()).commit();
////                        return true;
////                }
//        return false;
//        }
//
//        });