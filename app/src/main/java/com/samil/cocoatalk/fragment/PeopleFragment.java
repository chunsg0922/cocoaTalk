package com.samil.cocoatalk.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_people, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());

        return view;
    }

    // Fragment에 친구 목록을 띄워주기 위한 Adapter
    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<UserModel> userModels;

        public PeopleFragmentRecyclerViewAdapter () {
            userModels = new ArrayList<>();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // 접속한 계정의 uid를 저장
            FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){
                        userModels.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UserModel userModel = snapshot.getValue(UserModel.class);
                            if(userModel.getUid().equals(uid)){
                                continue;
                            }
                            userModels.add(userModel);
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

            //if(userModels.get(position).getProfile().equals("")){
            //    ((CustomViewHolder)holder).imageView.setImageDrawable(drawable);
            //} else {
                Glide.with
                        (holder.itemView.getContext())
                        .load(userModels.get(position).getProfile())
                        .apply(new RequestOptions().circleCrop())
                        .into(((CustomViewHolder) holder).imageView);

           // }
            ((CustomViewHolder) holder).nameTextView.setText(userModels.get(position).getName());
            ((CustomViewHolder) holder).msgTextView.setText(userModels.get(position).getMsg());

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("uid", userModels.get(position).getUid());
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(),R.anim.fromright, R.anim.toleft ); // 액티비티 전환 간 애니메이션 적용
                    startActivity(intent, activityOptions.toBundle()); // 애니메이션 적용한 activityOption을 intent와 함께 보낸다.
                }
            });
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView nameTextView, msgTextView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.frienditem_imageview);
                nameTextView = (TextView) view.findViewById(R.id.frienditem_name);
                msgTextView = (TextView)view.findViewById(R.id.frienditem_msg);
            }
        }
    }
}
