package com.samil.cocoatalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    int count = 0;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        ActionBar bar = getSupportActionBar();
        bar.hide();

        Button okRegisterBtn = (Button)findViewById(R.id.okRegisterBtn);
        EditText inputID = (EditText)findViewById(R.id.inputID);
        EditText inputPW = (EditText)findViewById(R.id.inputPW);
        EditText inputPW2 = (EditText)findViewById(R.id.inputPW2);
        EditText inputName = (EditText)findViewById(R.id.inputName);
        TextView asLoginText = (TextView)findViewById(R.id.asLoginText);

            Intent phoneIntent = getIntent();
                    String Phone = phoneIntent.getStringExtra("memberPhone");
                    String memberPhone = makePhoneNum(Phone);

                    Log.i("okBtn : onClick", "데이터 씌우기" + memberPhone);
        okRegisterBtn.setEnabled(false);

        // '확인' 클릭 시 실행되는 리스너
        okRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memberID = inputID.getText().toString().trim();
                String memberPassword = inputPW.getText().toString().trim();
                String pscheck = inputPW2.getText().toString().trim();
                String memberName = inputName.getText().toString().trim();

                // '비밀번호'가 8글자 이내일 경우
                if(memberPassword.length() < 8 || pscheck.length() < 8 ){
                    Toast.makeText(RegisterActivity.this, "비밀번호는 8~20글자 이내로 설정해주세요", Toast.LENGTH_SHORT).show();
                } else{

                    // 확인 비밀번호와 일치하는 경우
                    if (memberPassword.equals(pscheck)) {

                    // Firebase의 Authentication 수준에서의 회원가입 진행
                    firebaseAuth.createUserWithEmailAndPassword(memberID, memberPassword).addOnCompleteListener(RegisterActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    // 회원가입에 성공했을 경우
                                    if(task.isSuccessful()){
                                        FirebaseUser user = firebaseAuth.getCurrentUser(); // FirebaseUser 객체 선언
                                        String email = user.getEmail(); // user의 email,uid를 받아와서 변수에 저장해준다.
                                        String uid = user.getUid();
                                        String name = memberName;

                                        HashMap<Object, String> member = new HashMap<>();

                                        member.put("uid", uid );
                                        member.put("email", email);
                                        member.put("name",name);
                                        member.put("phone",memberPhone);
                                        member.put("img",null);
                                        member.put("msg",null);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("Users");
                                        reference.child(uid).setValue(member);
                                        db.collection("member")
                                                .add(member)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });
                                        Intent intent = new Intent(RegisterActivity.this, RegisterSuccessActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    // 회원가입에 실패했을 경우(같은 아이디 존재)
                                    else{
                                        Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });

//                    Map<String, Object> user = new HashMap<>();
//                    user.put("id", memberID);
//                    user.put("password", memberPassword);
//                    user.put("name", memberName);
//                    user.put("phone", memberPhone);
//                    user.put("img", null);
//                    user.put("msg", null);


                }
                    // 비밀번호 확인이 일치하지 않는 경우
                    else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.myTheme).setTitle("확인 비밀번호 불일치").
                            setMessage("비밀번호와 비밀번호 확인이 일치하지 않습니다 \n 비밀번호를 다시 입력해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    inputPW.setText("");
                                    inputPW2.setText("");
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
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


