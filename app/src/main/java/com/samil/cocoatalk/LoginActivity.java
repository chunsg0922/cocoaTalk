package com.samil.cocoatalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        EditText editID = (EditText)findViewById(R.id.editID);
        EditText editPasswd = (EditText)findViewById(R.id.editPasswd);
        TextView findMeText = (TextView)findViewById(R.id.findMeText);
        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);

        // '로그인' 클릭 시 실행되는 리스너
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String memberID = editID.getText().toString();
                String memberPassword = editPasswd.getText().toString();

                if(memberID.equals("") || memberPassword.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.myTheme).setTitle("알림").setMessage("아이디와 비밀번호를 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });

        // '코코아계정 또는 비밀번호 찾기' 클릭 시 실행되는 리스너
        findMeText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, FindMeActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        // '코코아계정 만들기' 클릭 시 실행되는 리스너
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterAgreementActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        });
    }

    // 화면 중지
    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

    }
}