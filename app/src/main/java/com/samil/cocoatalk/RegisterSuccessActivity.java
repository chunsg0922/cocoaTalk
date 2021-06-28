package com.samil.cocoatalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);

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