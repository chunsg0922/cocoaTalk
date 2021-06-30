package com.samil.cocoatalk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MemberListAdapter extends BaseAdapter {

    private Context context;
    private List<Member> memberList;

    public MemberListAdapter(Context context, List<Member> memberList){
        this.context = context;
        this.memberList = memberList;
    }

    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.friend, null);
        TextView memberName = (TextView)v.findViewById(R.id.memberName);
       // TextView memberPhone = (TextView)v.findViewById(R.id.memberPhone);
        ImageView memberImg = (ImageView)v.findViewById(R.id.memberImg);
        TextView memberMsg = (TextView)v.findViewById(R.id.memberMsg);

        memberName.setText(memberList.get(position).getMemberName());
//        memberPhone.setText(memberList.get(position).getMemberPhone());
      //  memberImg.setImageURI(memberList.get(position).getMemberImg());
        memberMsg.setText(memberList.get(position).getMemberMsg());

        v.setTag(memberList.get(position).getMemberID());
        return v;
    }
}