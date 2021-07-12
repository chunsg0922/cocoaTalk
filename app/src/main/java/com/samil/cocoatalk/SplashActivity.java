package com.samil.cocoatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    String uid, id, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        SharedPreferences sharedPref = getSharedPreferences("save", MODE_PRIVATE);
        uid = sharedPref.getString("uid",uid);
        id = sharedPref.getString("id", id);
        password = sharedPref.getString("password", password);

      //  if(id.equals("") || password.equals("")){
            // Fire
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings); //

            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) { // 작업이 성공적으로 이루어졌다면
                                boolean updated = task.getResult();
                                Log.d(TAG, "Config params updated: " + updated); // 로그에 업데이트의 상태를 출력
                            } else {
                                Toast.makeText(SplashActivity.this, "Fetch failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            displayWelcomeMessage(); // 웰컴메시지 메소드 실행
                        }
                    });
//        } else{
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(id, password)
//                    .addOnCompleteListener(SplashActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
//
//                            FirebaseDatabase.getInstance().getReference().child("Users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        task.getResult().getValue();
//                                    }
//                                }
//                            });
//                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//
//                            startActivity(intent);
//                        }
//                    });
//        }

    }

    // 메시지를 띄워주는 메소드
    void displayWelcomeMessage(){
        String splash_background = mFirebaseRemoteConfig.getString("splash_background");
        boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = mFirebaseRemoteConfig.getString("splash_message");

        if(caps){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else{
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }
}