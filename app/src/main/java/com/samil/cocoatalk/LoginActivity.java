package com.samil.cocoatalk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

                // 서버측에 보낸 요청에 대한 응답을 받는 리스너 실행
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                Intent intent = new Intent(LoginActivity.this, SocialActivity.class);
                                LoginActivity.this.startActivity(intent);
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("회원 정보를 잘못 입력하셨습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create().show();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                // 서버 측에 보내는 Request객체 선언 및 실행
                LoginRequest loginRequest = new LoginRequest(memberID, memberPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);


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
    }
}