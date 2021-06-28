package com.samil.cocoatalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterFailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fail);

        Button continueBtn = (Button)findViewById(R.id.continueBtn);
        Button otherBtn = (Button)findViewById(R.id.otherBtn);

        // '계속' 클릭 시 실행되는 리스너
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFailActivity.this, RegisterActivity.class);
                RegisterFailActivity.this.startActivity(intent);
            }
        });

        // '다른 전화번호로 인증하기' 클릭 시 실행되는 리스너
        otherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterFailActivity.this, RegisterPhoneInputActivity.class);
                RegisterFailActivity.this.startActivity(intent);
            }
        });
    }
}