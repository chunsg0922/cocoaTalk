package com.samil.cocoatalk.register;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.firebase.auth.PhoneAuthOptions;
import com.samil.cocoatalk.LoginActivity;
import com.samil.cocoatalk.R;

import java.util.Random;

public class RegisterPhoneInputActivity extends AppCompatActivity {

    private final int MY_PERMISSION_REQUEST_SMS = 1001;
    SmsManager smsManager;
    public String smsNum;
    PhoneAuthOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone_input);

        ActionBar bar = getSupportActionBar();
        bar.hide();

        EditText editPhoneNum = (EditText)findViewById(R.id.EditPhoneNum);
        Button SMSBtn = (Button)findViewById(R.id.smsBtn);
        TextView goBackText = (TextView)findViewById(R.id.goBackText);

        EditText inputNum = (EditText)findViewById(R.id.inputNum);
        Button okBtn = (Button)findViewById(R.id.okBtn);
        inputNum.setVisibility(View.GONE);
        okBtn.setVisibility(View.GONE);

        // 앱 최초 실행 시 메시지 수신을 위해 Activity에서 자체적으로 Permission을 받는 과정
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 SMS 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(RegisterPhoneInputActivity.this, new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
            }
        }

        // '인증번호 발송' 클릭 시 실행되는 리스너
        SMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = editPhoneNum.getText().toString();
                numGen();
                String msg = "코코아톡의 인증번호는 [" + smsNum + "] 입니다.";
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum, null, msg, null, null);
                    Toast.makeText(getBaseContext(), "인증번호 발송", Toast.LENGTH_SHORT).show();
                    inputNum.setVisibility(View.VISIBLE);
                    okBtn.setVisibility(View.VISIBLE);
                    okBtn.setEnabled(false);
                } catch ( Exception e){
                }
            }
        });

        // '인증번호 6자리' 입력 시 실행되는 리스너
        inputNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 6){
                    okBtn.setEnabled(true);
                } else {
                    okBtn.setEnabled(false);
                }
            }
        });

        // '처음으로 돌아가기' 클릭 시 실행되는 리스너
        goBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPhoneInputActivity.this, LoginActivity.class);
                RegisterPhoneInputActivity.this.startActivity(intent);
                finish();
            }
        });

        // '확인' 클릭 시 실행되는 리스너
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = inputNum.getText().toString();
                String memberPhone = editPhoneNum.getText().toString();

                // 입력한 인증번호와 전송받은 인증번호가 일치하는 경우
                if(num.equals(smsNum)){
                    Intent intent = new Intent(RegisterPhoneInputActivity.this, RegisterActivity.class);
                    intent.putExtra("memberPhone", memberPhone);
                    startActivity(intent);
                    finish();
                    // 휴대폰 번호 중복 검사를 진행하기 위해 서버 DB에 등록된 휴대폰 번호 조회 결과를 불러오는 과정

                }
                // 인증번호가 일치하지 않는 경우
                else {
                    Toast.makeText(RegisterPhoneInputActivity.this, "인증번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // 6자리 난수 생성 메소드
    public String numGen(){
        Random random = new Random();
        String numString = "";

        for(int i=0; i<6; i++){
            String ran = Integer.toString(random.nextInt(10));
            numString += ran;
        }
        smsNum = numString;
        return numString;
    }
}
















//    BroadcastReceiver mSentReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            switch(getResultCode()){
//                case RESULT_OK:
//                    Toast.makeText(RegisterActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };
//
//    BroadcastReceiver mRecvReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            switch (getResultCode()){
//                case RESULT_OK:
//                    Toast.makeText(RegisterActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };
//
//    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
//        switch(requestCode){
//            case MY_PERMISSION_REQUEST_SMS: {
//                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(this, "승인 허가됨", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "승인 거부됨", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }