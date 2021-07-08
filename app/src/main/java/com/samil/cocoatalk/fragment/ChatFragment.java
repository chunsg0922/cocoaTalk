package com.samil.cocoatalk.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samil.cocoatalk.R;
import com.samil.cocoatalk.chat.MessageActivity;
import com.samil.cocoatalk.model.ChatModel;
import com.samil.cocoatalk.model.UserModel;
import com.samil.cocoatalk.tool.RecyclerViewEmptySupport;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.annotation.Nullable;

public class ChatFragment extends Fragment {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) view.findViewById(R.id.chatfragment_recyclerview);

        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setEmptyView(view.findViewById(R.id.empty_view));
        return view;
    }

    // 채팅목록을 리스트 형태로 띄우기 위한 Adapter 클래스
    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<ChatModel> chatModels = new ArrayList<>(); // 채팅 데이터를 담을 수 있는 ChatModel타입의 List 객체 생성
        private String uid;
        private ArrayList<String> user_others = new ArrayList<>(); // 상대방 정보를 저장하는 ArrayList 객체 생성
        private List<String> keys = new ArrayList<>();

        public ChatRecyclerViewAdapter(){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 접속한 계정의 uid를 받아온다. (사용자 uid)

            // chatRoom DB에서 자신의 계정이 포함된 채팅방 uid 중 활성화(true)되어있는 채팅방의 데이터를 가져온다.
            FirebaseDatabase.getInstance().getReference().child("chatRoom").orderByChild("users/" +uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    chatModels.clear();
                    for(DataSnapshot item :snapshot.getChildren()){ // 받아온 결과값동안 반복 수행
                        chatModels.add(item.getValue(ChatModel.class)); // chatModel의 리스트형태로 데이터를 추가해준다.
                    }
                    notifyDataSetChanged(); // 추가된 데이터 갱신하여 출력
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
            return new CustomViewHolder(view);
        }

        // ViewHolder를 화면에 Bind해주기 위한 메소드
        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String uid_other = null;

            // 채팅방에 있는 유저를 체크하는 for문
            for(String user: chatModels.get(position).users.keySet()){
                if(!user.equals(uid)){
                    uid_other = user;
                    user_others.add(uid_other);
                }
            }
            FirebaseDatabase.getInstance().getReference().child("Users").child(uid_other).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class); // DB에서 조회한 결과값을 UserModel의 객체에 저장한다.
                    // 프로필 사진을 출력하는 메소드
                    Glide.with(customViewHolder.itemView.getContext())
                            .load(userModel.getProfile())
                            .apply(new RequestOptions().circleCrop())
                            .into(customViewHolder.imageView);

                    customViewHolder.textView_title.setText(userModel.getName()); // 이름을 출력하기 위한 메소드
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            // 메시지를 내림차순으로 정렬 후, 마지막 메시지의 키값을 가져온다.
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder()); // TreeMap형태로 채팅목록을 선언한다.
            commentMap.putAll(chatModels.get(position).comments); // 채팅방에 저장된 comments를 모두 commentMap에 넣는다.

            // commentMap에 데이터가 존재하는 경우
            if(commentMap.keySet().toArray().length > 0) {
                String lastMessageKey = (String) commentMap.keySet().toArray()[0]; // 가져온 commentMap의 마지막 메시지 키값을 String 변수에 대입
                customViewHolder.textView_lastMessage.setText(chatModels.get(position).comments.get(lastMessageKey).message); // 뷰홀더에 마지막 메시지 출력

                // 채팅방 목록마다 가장 최근 메시지의 수/발신 시간을 나타내는 timestamp (시간값 데이터 변환)
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                long unixTime = (long) chatModels.get(position).comments.get(lastMessageKey).timestamp;
                Date date = new Date(unixTime);
                customViewHolder.textView_timestamp.setText(simpleDateFormat.format(date)); // 뷰홀더에 타임스탬프를 출력

            }
                // 뷰홀더에 아이템 클릭 리스너를 부여해준다.
                customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;

                        // 채팅방에 참여하는 회원의 수가 2명보다 클 경우(단체 채팅인 경우)
                        if(chatModels.get(position).users.size() > 2) {
                            intent = new Intent(v.getContext(), MessageActivity.class);
                            intent.putExtra("destinationRoom", keys.get(position));
                        } else{ // 단독 대화인 경우
                            intent = new Intent(v.getContext(), MessageActivity.class);
                            intent.putExtra("uid_other", user_others.get(position)); // 인텐트에 상대방의 uid
                        }
                        ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.fromright,R.anim.toleft);
                        startActivity(intent, activityOptions.toBundle());
                    }

                });

        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{

            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_lastMessage;
            public TextView textView_timestamp;

            public CustomViewHolder(View view){
                super(view);

                imageView = (ImageView)view.findViewById(R.id.chatitem_imageview);
                textView_title = (TextView)view.findViewById(R.id.chatitem_textview_title);
                textView_lastMessage = (TextView)view.findViewById(R.id.chatitem_textview_lastMessage);
                textView_timestamp = (TextView)view.findViewById(R.id.chatitem_textview_timestamp);
            }
        }
    }



}
