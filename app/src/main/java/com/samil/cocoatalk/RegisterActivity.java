package com.samil.cocoatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button okRegisterBtn = (Button)findViewById(R.id.okRegisterBtn);
        EditText inputID = (EditText)findViewById(R.id.inputID);
        EditText inputPW = (EditText)findViewById(R.id.inputPW);
        EditText inputPW2 = (EditText)findViewById(R.id.inputPW2);
        EditText inputName = (EditText)findViewById(R.id.inputName);
        TextView asLoginText = (TextView)findViewById(R.id.asLoginText);

        okRegisterBtn.setEnabled(false);

//        if(!inputID.getText().toString().matches("") && !inputPW.getText().toString().matches("") &&
//                !inputPW2.getText().toString().matches("") && !inputName.getText().toString().matches("")){
//            okRegisterBtn.setEnabled(true);
//        }

        // '확인' 클릭 시 실행되는 리스너
        okRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent phoneIntent = getIntent();
                String Phone = phoneIntent.getStringExtra("memberPhone");
                String memberPhone = makePhoneNum(Phone);
                String memberID = inputID.getText().toString();
                String memberPassword = inputPW.getText().toString();
                String memberName = inputName.getText().toString();

                Log.i("okBtn : onClick", "데이터 씌우기" + memberPhone );

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                Intent intent1 = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                                RegisterActivity.this.startActivity(intent1);
                                finish();
                            } else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNeutralButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(memberID, memberPassword, memberName, memberPhone,  responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

        // '아이디' 텍스트 입력에 따른 리스너
        inputID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0 ){
                     count++;
                    if(count >= 4){
                        okRegisterBtn.setEnabled(true);
                    }
                }
            }
        });

        // '비밀번호' 텍스트 입력에 따른 리스너
        inputPW.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    count++;
                    if(count >= 4){
                        okRegisterBtn.setEnabled(true);
                    }
                }
            }
        });

        // '비밀번호 확인' 텍스트 입력에 따른 리스너
        inputPW2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    count++;
                    if(count >= 4){
                        okRegisterBtn.setEnabled(true);
                    }
                }
            }
        });

        // '이름' 텍스트 입력에 따른 리스너
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    count++;
                    if(count >= 4){
                        okRegisterBtn.setEnabled(true);
                    }
                }
            }
        });

    }

    public static String makePhoneNum(String phoneNum){
        String regEx = "(\\d{3})(\\d{4})(\\d{4})";
        if(!Pattern.matches(regEx, phoneNum)) return null;
        return phoneNum.replaceAll(regEx, "$1-$2-$3");
    }

}