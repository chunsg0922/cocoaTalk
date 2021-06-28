package com.samil.cocoatalk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

public class SocialActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_READ = 1001;
    private final int MY_PERMISSION_REQUEST_WRITE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        // 컨텐트 프로바이더 (ContentProvider)
// 어플리케이션 내에서만 사용할 수 있는 데이터를 공유하기위한
// 방법으로 안드로이드의 4대 컴포넌트 중 하나이다

        // 폰에 저장되있는 전화번호부를 읽어보기(권한 필요)
        // AndroidManifest.xml

        TextView list = (TextView)findViewById(R.id.list);

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
            String phoneNumber = c.getString
                    (c.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.NUMBER));
            str += "이름 : " + name
                    +"폰번호 : " + phoneNumber + "\n";
        } while (c.moveToNext());//데이터가 없을 때까지반복
        list.setText(str);
    } // end of onCreate
} // end of class


