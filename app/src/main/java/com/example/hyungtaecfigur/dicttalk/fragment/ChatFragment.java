package com.example.hyungtaecfigur.dicttalk.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hyungtaecfigur.dicttalk.R;
import com.example.hyungtaecfigur.dicttalk.chat.GroupMessageActivity;
import com.example.hyungtaecfigur.dicttalk.chat.MessageActivity;
import com.example.hyungtaecfigur.dicttalk.model.ChatModel;
import com.example.hyungtaecfigur.dicttalk.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatFragment extends Fragment {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.chatFragment_recyclerView);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));

        return view;
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ChatModel> chatModels = new ArrayList<>();
        private String uid;
        private ArrayList<String> destinationUsers = new ArrayList<>();
        private List<String> keys = new ArrayList<>();


        public ChatRecyclerViewAdapter() {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chatModels.clear();

                    for (DataSnapshot item: dataSnapshot.getChildren()) {
                        chatModels.add(item.getValue(ChatModel.class));
                        keys.add(item.getKey());
                    }

                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destinationUid = null;


            //루프를 돌 필요가있나??
            for (String user: chatModels.get(position).users.keySet()) {
                if (!user.equals(uid)) {
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }
            }

            if (destinationUid != null) {
                FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        Glide.with(customViewHolder.imageView.getContext())
                                .load(userModel.profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(customViewHolder.imageView);

                        customViewHolder.textView_title.setText(userModel.userName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            //체팅방이 여러 개 있을 때 선택한 체팅방의 코멘트를 전부 가져온후 데이터를 reverse해준 후 젤 첫번째 값을 가져온다. O(log n) 걸림
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);

            if (commentMap.keySet().size() > 0) {


                String lastMessageKey = (String)commentMap.keySet().toArray()[0];
                customViewHolder.textView_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);

                //시간 처리
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                long unixTime = (long) chatModels.get(position).comments.get(lastMessageKey).timeStamp;
                Date date = new Date(unixTime);
                customViewHolder.textView_timeStamp.setText(simpleDateFormat.format(date));
            }

            //itemView는 single row 가리킴
            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (chatModels.get(position).users.size() >= 3) {
                        intent = new Intent(v.getContext(), GroupMessageActivity.class);
                        intent.putExtra("destinationRoom", keys.get(position));
                    } else {
                        intent = new Intent(v.getContext(), MessageActivity.class);
                        intent.putExtra("destinationUid", destinationUsers.get(position));
                    }
                    ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(v.getContext(), R.anim.from_right, R.anim.to_left);
                    startActivity(intent, activityOptions.toBundle());
                }
            });

        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_last_message;
            public TextView textView_timeStamp;

            public CustomViewHolder(View view) {
                super(view);

                textView_last_message = view.findViewById(R.id.chatItem_textView_listItem);
                textView_title = view.findViewById(R.id.chatItem_textView_title);
                textView_timeStamp = view.findViewById(R.id.chatItem_textView_timeStamp);

                imageView = view.findViewById(R.id.chatItem_imageView);
            }
        }
    }
}
