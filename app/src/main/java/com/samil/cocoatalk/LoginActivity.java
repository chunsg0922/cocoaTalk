package com.samil.cocoatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samil.cocoatalk.model.UserModel;
import com.samil.cocoatalk.register.RegisterAgreementActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class LoginActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_READ = 1001;
    private final int MY_PERMISSION_REQUEST_WRITE = 1001;

    String all = "";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    UserModel userModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 연락처 읽기를 위한 권한 설정
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 연락처 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_REQUEST_READ);
                    }
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_REQUEST_READ);
            }
        }

        // 연락처 쓰기를 위한 권한 설정
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)){
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 연락처 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[] {Manifest.permission.WRITE_CONTACTS}, MY_PERMISSION_REQUEST_WRITE);
                    }
                });
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSION_REQUEST_WRITE);
            }
        }

        // 액션바(타이틀/네비게이션) 숨기기
        ActionBar bar = getSupportActionBar();
        bar.hide();

        ////////////////////////////////////////////////////////////
            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds
                            .Phone.CONTENT_URI,  // 조회할 컬럼명
                    null, // 조회할 컬럼명
                    null, // 조건 절
                    null, // 조건절의 파라미터
                    null);// 정렬 방향


        if( c != null && c.getCount()>0) {
                c.moveToFirst(); // 커서를 처음위치로 이동시킴
                do {
                    // map = new HashMap<String, String>();

                    String phone = c.getString
                            (c.getColumnIndex(ContactsContract
                                    .CommonDataKinds.Phone.NUMBER));

                    all += "%" + phone + "%";
                } while (c.moveToNext()); //데이터가 없을 때까지반복
                Log.e("연락처 : " , all);
        };
/////////////////////////////////////////////////////////////////
        mAuth = FirebaseAuth.getInstance();

        EditText editID = (EditText)findViewById(R.id.editID);
        EditText editPasswd = (EditText)findViewById(R.id.editPasswd);
        TextView findMeText = (TextView)findViewById(R.id.findMeText);
        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);

        // '로그인' 클릭 시 실행되는 리스너
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String memberID = editID.getText().toString().trim();
                String memberPassword = editPasswd.getText().toString().trim();

                // '아이디'와 '비밀번호' 입력칸이 비어있는 경우
                if (memberID.equals("") || memberPassword.equals("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.myTheme).setTitle("알림").setMessage("아이디와 비밀번호를 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                // '아이디'와 '비밀번호' 입력이 되어있는 경우
                else {
                    mAuth.signInWithEmailAndPassword(memberID, memberPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Log.d(TAG, "signInWithCustomToken:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String id = user.getEmail();
                                        String uid = user.getUid();
                                        Log.e("로그인 이메일 정보 : " , id + ", UID: " + uid);
                                        userModel.setId(id);
                                        userModel.setUid(uid);

                                        // SharedPreferences 객체에 로그인 한 아이디를 저장한다.
                                        SharedPreferences sharedPref = getSharedPreferences("save", Context.MODE_PRIVATE); // SharedPreference 객체 생성해준다.
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("id", memberID);
                                        editor.putString("uid", uid);
                                        editor.apply();

                                        SharedPreferences sharedContact = getSharedPreferences("contact", MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sharedContact.edit();
                                        edit.putString("con", all);
                                        edit.apply();

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("id", userModel.getId());
                                        intent.putExtra("uid", userModel.getUid());
                                        LoginActivity.this.startActivity(intent);
                                        finish();
                                        //updateUI(user);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.myTheme).setTitle("로그인 실패").
                                                setMessage("회원 정보가 일치하지 않습니다. \n 계정과 비밀번호를 다시 입력해주세요.")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        editID.setText("");
                                                        editPasswd.setText("");
                                                    }
                                                });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                                       // updatUI(null);
                                    }
                                }
                            });

                }
            }

        });

        // '코코아계정 또는 비밀번호 찾기' 클릭 시 실행되는 리스너
        findMeText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, FindMeActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });

        // '코코아계정 만들기' 클릭 시 실행되는 리스너
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterAgreementActivity.class);
                //Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }

    // 화면 중지
    @Override
    protected void onStop(){
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

    }
}