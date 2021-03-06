package com.samil.cocoatalk.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.samil.cocoatalk.R;
import com.samil.cocoatalk.model.ChatModel;
import com.samil.cocoatalk.model.NotificationModel;
import com.samil.cocoatalk.model.UserModel;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageActivity extends AppCompatActivity {

    private String uid; // ?????? ????????? uid
    private String uid_other; // ????????? ????????? uid
    private String chatRoom_uid; // ????????? uid
    private String name_other; // ????????? ????????? ??????
    private String profile_other; // ????????? ???????????????
    private UserModel userModel_other = new UserModel();
    private Button button;
    private EditText edit;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm"); // ?????? ??????
    private int position;
    int peopleCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // ???????????? ????????? uid??? ????????????.
        uid_other = getIntent().getStringExtra("uid_other"); // ?????? ????????? uid
        profile_other = getIntent().getStringExtra("profile"); // ?????? ????????? profile
        name_other = getIntent().getStringExtra("name"); // ?????? ????????? name
        getSupportActionBar().setTitle(name_other); // ???????????? ????????? ????????? ????????????.
        button = (Button)findViewById(R.id.messageActivity_button);
        edit = (EditText)findViewById(R.id.messageActivity_editText);
        recyclerView = (RecyclerView)findViewById(R.id.messageActivity_recyclerView);


        button.setEnabled(false); // ?????? ????????? ????????????.

        // ????????? ?????? ??? ?????? ?????????????????? ?????????
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

        // '??????' ????????? ????????? ??????
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel(); // ???????????? ????????? ???????????? ????????? ChatModel ????????? ?????? ??????
                chatModel.users.put(uid, true);
                chatModel.users.put(uid_other, true);

                // ???????????? ???????????? ?????? ?????? ??????
                if(chatRoom_uid == null){
                    //button.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatRoom").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {                    // ????????????????????? chatRoom????????? ????????? ?????????????????? ???????????????.
                        // push()??? ?????? ????????? ????????? ??????????????? value??? ???????????????.
                        @Override
                        public void onSuccess(Void unused) {

                            checkChatRoom(); // chatRoom??? ???????????? ??????????????? ????????? ??????, checkChatRoom()??? ????????????.
                        }
                    });

                }
                // ???????????? ???????????? ?????? ??????
                else{
                    ChatModel.Comment comment = new ChatModel.Comment(); // ChatModel??? Comment ????????? ????????????,
                    comment.uid = uid;
                    comment.message = edit.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    // ????????? ???????????? chatRoom ????????? ????????? comments?????? ?????? ???????????? ???????????? ???????????? ????????????.
                    // DB??? ????????? ????????? ???????????? ?????? ???????????? onComplete??? ????????????. (????????? ?????????)
                    FirebaseDatabase.getInstance().getReference().child("chatRoom").child(chatRoom_uid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            edit.setText("");
                            button.setEnabled(false);
                        }
                    });


                }
            }
        });
        checkChatRoom();
    }

//    void sendGcm(){
//        Gson gson = new Gson();
//
//        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//        NotificationModel notificationModel = new NotificationModel();
//        Log.e("push?????? ??? : ", userModel_other.pushToken);
//        notificationModel.to = userModel_other.pushToken;
//        notificationModel.notification.title = userName;
//        notificationModel.notification.text = edit.getText().toString();
//        notificationModel.data.title = userName;
//        notificationModel.data.text = edit.getText().toString();
//
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));
//
//        Request request = new Request.Builder()
//                .header("Content-Type", "application/json")
//                .addHeader("Authorization", "key=AIzaSyDNFs9vhpZQzQ3VeMXojsAJIId2Z7aj_Xk")
//                .url("https://gcm-http.googleapis.com/gcm/send")
//                .post(requestBody)
//                .build();
//        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });
//    }

    // Firebase(DB)??? ???????????? ???????????? ????????? ???????????? ?????????
    void checkChatRoom(){
        // chatRoom DB??? ????????? ???????????? uid?????? ???????????? ???????????? uid??? ?????????(true) ???????????? chatRoom??? ??????????????? ????????????.
        FirebaseDatabase.getInstance().getReference().child("chatRoom").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    ChatModel chatModel = item.getValue(ChatModel.class); // chatModel??? ????????? ???????????? ????????????.

                    // DB??? ????????? ????????? ????????? ?????? ??? ???????????? uid??? ????????????, ????????? ????????? ?????? ??? ?????? ??????
                    if(chatModel.users.containsKey(uid_other) && chatModel.users.size() == 2){
                        chatRoom_uid = item.getKey(); // chatRoom??? ????????? ???????????? uid??? chatRoom_uid??? ??????
                        // button.setEnabled(true); // '??????' ?????? ?????????
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

    // ?????? ???????????? ???????????? ?????? Adapter
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<ChatModel.Comment> comments; // ChatModel??? Comment ????????? ????????? ?????? ??????

        public RecyclerViewAdapter(){ // ?????????
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("users").child(uid_other).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                       // userModel_other = dataSnapshot.getValue(UserModel.class);
                       // Log.e("userModel_other : ", "???????????? ??? : " + userModel_other.getProfile() + ", ?????? : " + userModel_other.getName());
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }

        // ????????? ????????? ????????? ?????????
        void getMessageList(){

            // chatRoom ????????? ??? ???????????? uid??? ??????, ??? ?????? child ??? comments??? ???????????? ???????????? ?????? ????????????.
            databaseReference = FirebaseDatabase.getInstance().getReference().child("chatRoom").child(chatRoom_uid).child("comments");

            // chatRoom - comments DB??? ????????? ????????? ????????? ?????? ?????? ????????? ???????????? ????????????.
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    Map<String,Object> readUserMap = new HashMap<>();
                    for(DataSnapshot item : dataSnapshot.getChildren()){
                        String key = item.getKey();
                        ChatModel.Comment comment_origin = item.getValue(ChatModel.Comment.class);
                        ChatModel.Comment comment_motify = item.getValue(ChatModel.Comment.class);
                        comment_motify.readUsers.put(uid, true);

                        readUserMap.put(key,comment_motify);
                        comments.add(comment_origin);
                    }

                    if(comments.size() == 0){
                        return;
                    }
                    if(!comments.get(comments.size() -1).readUsers.containsKey(uid)) {

                    FirebaseDatabase.getInstance().getReference().child("chatRoom").child(chatRoom_uid).child("comments")
                            .updateChildren(readUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            notifyDataSetChanged();
                            recyclerView.scrollToPosition(comments.size() - 1);
                        }
                    }) ;
                }else{
                        notifyDataSetChanged();
                        recyclerView.scrollToPosition(comments.size() - 1);
                    }
                    // ????????? ??????
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

        // Message??? ????????? ?????? ??????????????????
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

            long unixTime = (long)comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);

            // ?????? ?????? ?????????
            if (comments.get(position).uid.equals(uid)) {
                messageViewHolder.textView_message.setText(comments.get(position).message); // ???????????? ???????????? ????????????.
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.textView_name.setText("");
                messageViewHolder.linearLayout.setVisibility(View.VISIBLE);
                messageViewHolder.imageView_profile.setVisibility(View.INVISIBLE);
                messageViewHolder.me_layout.setVisibility(View.VISIBLE);
                messageViewHolder.you_layout.setVisibility(View.GONE);
                messageViewHolder.textView_message.setTextSize(15);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                setReadCounter(position,messageViewHolder.textview_readCounter_left);

                messageViewHolder.textView_timestamp_left.setText(time);
            }
            // ???????????? ?????? ?????????
            else{
                messageViewHolder.textView_timestamp.setText(time);
                messageViewHolder.imageView_profile.setVisibility(View.VISIBLE);
                    Glide.with(holder.itemView.getContext())
                            .load(profile_other)
                            .centerCrop()
                            .apply(new RequestOptions().bitmapTransform(new RoundedCorners(15)))
                            .into(messageViewHolder.imageView_profile); // ???????????? ??????????????? ??????

                messageViewHolder.textView_name.setText(name_other); // ???????????? ?????? ??????
                messageViewHolder.linearLayout.setVisibility(View.VISIBLE);
                messageViewHolder.me_layout.setVisibility(View.GONE);
                messageViewHolder.you_layout.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setTextSize(15);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
                setReadCounter(position,messageViewHolder.textview_readCounter_right);

            }


        }

        // ????????? ?????? ????????? ?????? ?????????
        void setReadCounter(final int Position, final TextView textView){
            if(peopleCount ==0) {
                // ????????? DB??? ????????? ????????? uid??? ????????????.
                FirebaseDatabase.getInstance().getReference().child("chatRoom").child(chatRoom_uid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Boolean> users = (Map<String, Boolean>) dataSnapshot.getValue(); // users ????????? ?????? ??????(uid, true)?????? ????????????.
                        peopleCount = users.size(); // users????????? ???????????? ????????? ??????
                        int count = peopleCount-comments.get(position).readUsers.size(); //
                        if (count > 0) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(String.valueOf(count));

                        } else {
                            textView.setVisibility((View.INVISIBLE));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }else{
                int count = peopleCount-comments.get(position).readUsers.size();
                if (count > 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(String.valueOf(count));

                } else {
                    textView.setVisibility((View.INVISIBLE));
                }
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
            public LinearLayout linearLayout_main;
            public LinearLayout me_layout;
            public LinearLayout you_layout;
            public TextView textView_timestamp;
            public TextView textview_readCounter_right;
            public TextView textview_readCounter_left;
            public TextView textView_timestamp_left;

            public MessageViewHolder(View view){
                super(view);
                textView_message = (TextView)view.findViewById(R.id.messageItem_message);
                textView_name = (TextView)view.findViewById(R.id.messageItem_name);
                imageView_profile = (ImageView)view.findViewById(R.id.messageItem_profile);
                linearLayout = (LinearLayout)view.findViewById(R.id.messageItem_Linearlayout);
                linearLayout_main = (LinearLayout)view.findViewById(R.id.messageItem_Linearlayout_main);
                textView_timestamp = (TextView)view.findViewById(R.id.messageItem_timestamp);
                textView_timestamp_left = (TextView)view.findViewById(R.id.messageItem_timestamp_left);
                textview_readCounter_left = (TextView)view.findViewById(R.id.messageItem_textview_readCounter_left);
                textview_readCounter_right = (TextView)view.findViewById(R.id.messageItem_textview_readCounter_right);
                me_layout = (LinearLayout)view.findViewById(R.id.me_layout);
                you_layout = (LinearLayout)view.findViewById(R.id.you_layout);

            }
        }
    }

    @Override
    public void onBackPressed(){

        if(valueEventListener != null){
            databaseReference.removeEventListener(valueEventListener);
        }
        finish();
        overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }

}