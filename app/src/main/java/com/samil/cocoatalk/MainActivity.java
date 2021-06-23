package com.samil.cocoatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSION_REQUEST_SMS = 1001;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    SmsManager smsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 SMS 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
            }
        }
        TextView textView = (TextView)findViewById(R.id.textView);
        EditText editPhoneNum = (EditText)findViewById(R.id.EditPhoneNum);
        Button SMSBtn = (Button)findViewById(R.id.SMSBtn);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        SMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = editPhoneNum.getText().toString();
                numGen();
                String msg = "코코아톡의 인증번호는 [" + numGen() + "] 입니다.";
                try{
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNum, null, msg, null, null);
                    Toast.makeText(getBaseContext(), "인증번호 발송", Toast.LENGTH_SHORT).show();
                } catch ( Exception e){

                }
            }
        });


    }

//    private void sendSMS(String phoneNum){
//         SmsManager sms = SmsManager.getDefault();
//         sms.sendTextMessage(phoneNumber, null, message, null, null);
//        numGen();
//        String msg = "코코아톡의 인증번호는 [" + numGen() + "] 입니다.";
//
//        ArrayList<String> msgList = smsManager.divideMessage(msg);
//
//        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
//        PendingIntent recvPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
//
//        registerReceiver(mSentReceiver, new IntentFilter("SMS_SENT"));
//        registerReceiver(mRecvReceiver, new IntentFilter("SMS_DELIVERED"));
//    }

    public static String numGen(){
        Random random = new Random();
        String numString = "";

        for(int i=0; i<6; i++){
            String ran = Integer.toString(random.nextInt(10));
                numString += ran;
        }
        return numString;
    }

    BroadcastReceiver mSentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(getResultCode()){
                case RESULT_OK:
                    Toast.makeText(MainActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    BroadcastReceiver mRecvReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case RESULT_OK:
                    Toast.makeText(MainActivity.this, "인증번호 발송", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(requestCode){
            case MY_PERMISSION_REQUEST_SMS: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "승인 허가됨", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "승인 거부됨", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}