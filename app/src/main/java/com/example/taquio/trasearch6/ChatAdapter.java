package com.example.taquio.trasearch6;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by taquio on 2/16/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<Chats> mChatList;
    private FirebaseAuth mAuth;


    public ChatAdapter(List<Chats> mChatList) {
        this.mChatList = mChatList;
    }





    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_layout,parent,false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chats c = mChatList.get(position);
        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        String from_user = c.getFrom();

        if(from_user.equals(currentUserID))
        {
            holder.chatSingleText.setBackgroundColor(Color.WHITE);
            holder.chatSingleText.setTextColor(Color.BLACK);
        }
        else
        {
            holder.chatSingleText.setBackgroundResource(R.drawable.chat_text_background);
            holder.chatSingleText.setTextColor(Color.WHITE);

        }
        holder.chatSingleText.setText(c.getMessage());

//        holder.chatSingleTime.setText(c.getTime());

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {
        public TextView chatSingleText;
        public TextView chatSingleTime;
        public CircleImageView chatSingleImage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            chatSingleText = itemView.findViewById(R.id.chatSingleText);
            chatSingleTime = itemView.findViewById(R.id.chatSingleTime);
            chatSingleImage = itemView.findViewById(R.id.chatSingleImage);

        }
    }
}
