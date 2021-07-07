package com.samil.cocoatalk.register;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.samil.cocoatalk.LoginActivity;
import com.samil.cocoatalk.R;

public class RegisterSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        Button loginMoveBtn = (Button)findViewById(R.id.loginMoveBtn);

        // '로그인페이지로 이동' 클릭 시 실행되는 리스너
        loginMoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSuccessActivity.this, LoginActivity.class);
                RegisterSuccessActivity.this.startActivity(intent);
                finish();
            }
        });
    }
}