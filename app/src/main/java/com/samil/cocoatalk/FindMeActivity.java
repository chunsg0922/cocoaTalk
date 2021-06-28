package com.samil.cocoatalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.internal.InternalTokenProvider;

import org.w3c.dom.Text;

public class FindMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_me);

        TextView findIDText = (TextView)findViewById(R.id.findIDText);
        TextView findPWText = (TextView)findViewById(R.id.findPWText);

        // '코코아계정 찾기' 클릭 시 실행되는 리스너
        findIDText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindMeActivity.this, FindIdActivity.class);
                FindMeActivity.this.startActivity(intent);
            }
        });

        // '비밀번호 재설정' 클릭 시 실행되는 리스너
        findPWText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindMeActivity.this, FindPwActivity.class);
                FindMeActivity.this.startActivity(intent);
            }
        });
    }
}