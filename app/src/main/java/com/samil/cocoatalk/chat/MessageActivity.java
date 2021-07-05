package com.samil.cocoatalk.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.samil.cocoatalk.R;
import com.samil.cocoatalk.model.ChatModel;
import com.samil.cocoatalk.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private String uid; // 자신 계정의 uid
    private String uid_other; // 상대방 계정의 uid
    private String chatRoom_uid; // 채팅방 uid

    private Button button;
    private EditText edit;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 로그인한 계정의 uid를 받아온다.
        uid_other = getIntent().getStringExtra("uid"); // 채팅 대상의 uid
        button = (Button)findViewById(R.id.messageActivty_button);
        edit = (EditText)findViewById(R.id.messageActivity_editText);
        button.setEnabled(false);

        recyclerView = (RecyclerView)findViewById(R.id.messageActivity_recyclerView);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                button.setEnabled(true);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel(); // 채팅방에 필요한 데이터를 정의한 ChatModel 클래스 객체 생성
                chatModel.users.put(uid, true);
                chatModel.users.put(uid_other, true);

                // 채팅방이 생성되어 있지 않은 경우
                if(chatRoom_uid == null){
                    button.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatRoom").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {                    // 데이터베이스에 chatRoom이라는 문서에 채팅데이터를 추가해준다.
                        // push()를 통해 채팅방 식별이 가능하도록 value를 추가해준다.
                        @Override
                        public void onSuccess(Void unused) {
                            checkChatRoom();
                        }
                    });

                } else{ // 채팅방이 생성되어 있는 경우
                    ChatModel.Comment comment = new ChatModel.Comment(); // ChatModel 하위 클래스로 생성한 comment의 객체를 생성하고,
                    comment.uid = uid;
                    comment.message = edit.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatRoom").child(chatRoom_uid).child("comments").push().setValue(comment);
                    // 보내는 메시지를 chatRoom 테이블 아래에 comments라는 하위 테이블을 생성하여 데이터를 저장한다.
                }
            }
        });
    }

    // Firebase(DB)에 채팅방이 생성되어 있는지 확인하는 메소드
    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatRoom").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class); // DB에 해당 uid가 들어가있는지 확인하기 위해 chatModel 객체 생성
                    if(chatModel.users.containsKey(uid_other)){
                        chatRoom_uid = item.getKey(); // DB의 테이블 중 chatRoom에 생성된 데이터의 uid를 chatRoom_uid로 설정
                        button.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments; // ChatModel의 Comment 클래스 형태의 배열 선언
        UserModel userModel;
        public RecyclerViewAdapter(){
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child(uid_other).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    userModel = dataSnapshot.getValue(UserModel.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
        void getMessageList(){
            //
            FirebaseDatabase.getInstance().getReference().child("chatRooms").child(chatRoom_uid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        comments.add(item.getValue(ChatModel.Comment.class));
                    }
                    notifyDataSetChanged();
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);

            // 채팅 데이터의 uid가 사용자일 경우
            if (comments.get(position).uid.equals(uid)) {
                messageViewHolder.textView_message.setText(comments.get(position).message); // 메시지의 텍스트를 표시한다.
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.linearLayout.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setTextSize(20);
            }else{
                Glide.with(holder.itemView.getContext())
                        .load(userModel.getProfile())
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_profile);
                messageViewHolder.textView_name.setText(userModel.getName());
                messageViewHolder.linearLayout.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(20);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder{
            public TextView textView_message;
            public TextView textView_name;
            public ImageView imageView_profile;
            public LinearLayout linearLayout;

            public MessageViewHolder(View view){
                super(view);
                textView_message = (TextView)view.findViewById(R.id.messageItem_message);
                textView_name = (TextView)view.findViewById(R.id.messageItem_name);
                imageView_profile = (ImageView)view.findViewById(R.id.messageItem_profile);
                linearLayout = (LinearLayout)view.findViewById(R.id.messageItem_Linearlayout);
            }
        }
    }
}