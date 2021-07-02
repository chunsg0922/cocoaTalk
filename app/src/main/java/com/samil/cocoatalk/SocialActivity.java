package com.samil.cocoatalk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;


public class SocialActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_READ = 1001;
    private final int MY_PERMISSION_REQUEST_WRITE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        // 연락처 읽기를 위한 권한 설정
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 연락처 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SocialActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_REQUEST_READ);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_REQUEST_READ);
            }
        }

        // 연락처 쓰기를 위한 권한 설정
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_CONTACTS)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("안내");
                builder.setMessage("이 앱은 연락처 사용 권한을 부여하지 않으면 제대로 작동하지 않습니다.");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SocialActivity.this, new String[] {Manifest.permission.WRITE_CONTACTS}, MY_PERMISSION_REQUEST_WRITE);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSION_REQUEST_WRITE);

            }

        }

        TextView list = (TextView)findViewById(R.id.list);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String uid = intent.getStringExtra("uid");
        Log.e("로그인 멤버 정보 넘기기 : " , "아이디: " + id + ", UID: " + uid );


        
        Cursor c =  getContentResolver().query(
                ContactsContract.CommonDataKinds
                        .Phone.CONTENT_URI,  // 조회할 컬럼명
                null, // 조회할 컬럼명
                null, // 조건 절
                null, // 조건절의 파라미터
                null);// 정렬 방향

        String str = ""; // 출력할 내용을 저장할 변수
        c.moveToFirst(); // 커서를 처음위치로 이동시킴
        do {
            String name = c.getString
                    (c.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.DISPLAY_NAME));
            String memberphoneNumber = c.getString
                    (c.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.NUMBER));
            str += "이름 : " + name
                    +"폰번호 : " + memberphoneNumber + "\n";

        } while (c.moveToNext()); //데이터가 없을 때까지반복
        list.setText(str);

    }


}


//    Response.Listener<String> responseListener = new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try{
//                        JSONObject jsonResponse = new JSONObject(response);
//                        boolean success = jsonResponse.getBoolean("success");
//
//                        if(success){
//
//                        } else{
//                            AlertDialog.Builder builder = new AlertDialog.Builder(SocialActivity.this);
//                            builder.setMessage("친구 목록 조회에 실패했습니다.")
//                                    .setNeutralButton("다시 시도", null)
//                                    .create()
//                                    .show();
//                        }
//                    } catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            };
//            ShowListRequest showListRequest = new ShowListRequest(memberphoneNumber,  responseListener);
//            RequestQueue queue = Volley.newRequestQueue(SocialActivity.this);
//            queue.add(showListRequest);