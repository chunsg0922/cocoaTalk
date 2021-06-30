package com.samil.cocoatalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.file.StandardWatchEventKinds;

public class RegisterAgreementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_agreement);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        Button agreeBtn = (Button)findViewById(R.id.agreeBtn);
        CheckBox allCheck = (CheckBox)findViewById(R.id.allCheckBox);
        CheckBox ageCheck = (CheckBox)findViewById(R.id.ageCheckBox);
        CheckBox assetCheck = (CheckBox)findViewById(R.id.assetCheckBox);
        CheckBox dataCheck = (CheckBox)findViewById(R.id.dataCheckBox);
        TextView goBackText = (TextView)findViewById(R.id.goBackText);
        agreeBtn.setEnabled(false);


        // '모두 동의' 체크박스 리스너
        allCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCheck.isChecked()){
                    ageCheck.setChecked(true);
                    assetCheck.setChecked(true);
                    dataCheck.setChecked(true);
                    agreeBtn.setEnabled(true);

                } else {
                    ageCheck.setChecked(false);
                    assetCheck.setChecked(false);
                    dataCheck.setChecked(false);
                    agreeBtn.setEnabled(false);
                }

            }
        });

        // '만 14세 이상 동의' 체크박스 리스너
        ageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCheck.isChecked()){
                    allCheck.setChecked(false);
                    agreeBtn.setEnabled(false);
                } else if(ageCheck.isChecked() && assetCheck.isChecked() && dataCheck.isChecked()){
                    allCheck.setChecked(true);
                    agreeBtn.setEnabled(true);
                }
            }
        });

        // '계정 약관 동의' 체크박스 리스너
        assetCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCheck.isChecked()){
                    allCheck.setChecked(false);
                    agreeBtn.setEnabled(false);
                } else if(ageCheck.isChecked() && assetCheck.isChecked() && dataCheck.isChecked()){
                    allCheck.setChecked(true);
                    agreeBtn.setEnabled(true);

                }
            }
        });

        // '개인정보 동의' 체크박스 리스너
        dataCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCheck.isChecked()){
                    allCheck.setChecked(false);
                    agreeBtn.setEnabled(false);
                } else if(ageCheck.isChecked() && assetCheck.isChecked() && dataCheck.isChecked()){
                    allCheck.setChecked(true);
                    agreeBtn.setEnabled(true);

                }
            }
        });

        // '동의하고 계속 진행합니다' 클릭 시 실행되는 리스너
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterAgreementActivity.this, RegisterPhoneInputActivity.class);
                RegisterAgreementActivity.this.startActivity(intent);
            }
        });

        // '처음으로 돌아가기' 클릭 시 실행되는 리스너
        goBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterAgreementActivity.this, LoginActivity.class);
                RegisterAgreementActivity.this.startActivity(intent);
                finish();
            }
        });
    }

}