package com.samil.cocoatalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    private ListView memberListView;
    private MemberListAdapter adapter;
    private List<Member> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Intent intent = getIntent();
        memberListView = (ListView)findViewById(R.id.memberListView);
        memberList = new ArrayList<Member>();

        adapter = new MemberListAdapter(getApplicationContext(), memberList);
        memberListView.setAdapter(adapter);

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("memberList"));
            JSONArray jsonArray = jsonObject.getJSONArray("response");
            int count = 0;
            String memberID, memberPassword, memberName, memberPhone, memberImg, memberMsg;
            while(count < jsonArray.length()){
                JSONObject object = jsonArray.getJSONObject(count);
                memberID = object.getString("memberID");
                memberPassword = object.getString("memberPassword");
                memberName = object.getString("memberName");
                memberPhone = object.getString("memberPhone");
                memberImg = object.getString("memberImg");
                memberMsg = object.getString("memberMsg");
                Member member = new Member(memberID, memberPassword, memberName, memberPhone, memberImg, memberMsg);
                memberList.add(member);
                count++;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}