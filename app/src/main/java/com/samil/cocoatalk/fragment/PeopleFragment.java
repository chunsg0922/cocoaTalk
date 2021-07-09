package com.samil.cocoatalk.fragment;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.samil.cocoatalk.model.UserModel;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {

    List<UserModel> userModels;
    String all;
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        SharedPreferences sharedContact = getActivity().getSharedPreferences("contact", Context.MODE_PRIVATE);
        all = sharedContact.getString("con", "");
        Log.e("피플프래그먼트 : " , "넘어온 값 : " + all);
        View view  = inflater.inflate(R.layout.fragment_people, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());


        return view;
    }

    // Fragment에 친구 목록을 띄워주기 위한 Adapter
    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        public PeopleFragmentRecyclerViewAdapter () {
            userModels = new ArrayList<>();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 접속한 계정의 uid를 저장
            // DB의 Users 테이블에 있는 데이터를 가져온다.
            FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        userModels.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            if(userModel.getUid().equals(uid)){ // userModel객체의 uid값이 접속한 계정의 uid와 같을 경우
                                continue; // 목록에 추가시키는 작업을 하지 않고 그냥 넘어간다.
                            }
                            if(all.contains(userModel.getPhone())) {
                                userModels.add(userModel);
                            }
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){

                    }
            });
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


                Glide.with
                        (holder.itemView.getContext())
                        .load(userModels.get(position).getProfile())
                        .apply(new RequestOptions().circleCrop())
                        .into(((CustomViewHolder) holder).imageView);

//            Picasso.get()
//                    .load(userModels.get(position).getProfile())
//                    .into(((CustomViewHolder) holder).imageView);

            ((CustomViewHolder) holder).nameTextView.setText(userModels.get(position).getName());
           // ((CustomViewHolder) holder).msgTextView.setText(userModels.get(position).getMsg());

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("uid_other", userModels.get(position).getUid());
                    intent.putExtra("profile", userModels.get(position).getProfile());
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(),R.anim.fromright, R.anim.toleft ); // 액티비티 전환 간 애니메이션 적용
                    startActivity(intent, activityOptions.toBundle()); // 애니메이션 적용한 activityOption을 intent와 함께 보낸다.
                }
            });
            if (userModels.get(position).comment != null) {

                ((CustomViewHolder) holder).textView_comment.setText(userModels.get(position).comment);
            }
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView nameTextView, msgTextView;
            public TextView textView_comment;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                nameTextView = (TextView) view.findViewById(R.id.frienditem_textview);
                //msgTextView = (TextView)view.findViewById(R.id.frienditem_msg);
                textView_comment = (TextView)view.findViewById(R.id.frienditem_textview_comment);
            }
        }
    }



}
