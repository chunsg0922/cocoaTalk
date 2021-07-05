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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import static android.content.ContentValues.TAG;

public class SplashActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_READ = 1001;
    private final int MY_PERMISSION_REQUEST_WRITE = 1001;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        // 연락처 읽기를 위한 권한 설정
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 연락처 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_REQUEST_READ);
                    }
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_REQUEST_READ);
            }
        }

        // 연락처 쓰기를 위한 권한 설정
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)){
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 연락처 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[] {Manifest.permission.WRITE_CONTACTS}, MY_PERMISSION_REQUEST_WRITE);
                    }
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSION_REQUEST_WRITE);
            }
        }

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