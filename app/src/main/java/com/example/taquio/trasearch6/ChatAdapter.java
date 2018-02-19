package com.example.taquio.trasearch6;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by taquio on 2/16/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<Chats> mChatList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    public ChatAdapter(List<Chats> mChatList) {
        this.mChatList = mChatList;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_single_layout,parent,false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        Chats c = mChatList.get(position);
        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        String from_user = c.getFrom();
        String message_type = c.getType();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String image = dataSnapshot.child("Image_thumb").getValue().toString();

//                Picasso.with(holder.chatSingleConvImage.getContext())
//                        .load(dataSnapshot.child("Image_thumb").getValue().toString())
//                        .networkPolicy(NetworkPolicy.OFFLINE)
//                        .placeholder(R.drawable.man)
//                        .into(holder.chatSingleConvImage, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso.with(holder.chatSingleConvImage.getContext())
//                                        .load(dataSnapshot
//                                                .child("Image_thumb").getValue().toString())
//                                        .placeholder(R.drawable.man)
//                                        .into(holder.chatSingleConvImage);
//                            }
//                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text"))
        {
            holder.chatSingleText.setText(c.getMessage());
            holder.chatSingleTime.setText(getDate(c.getTime(),"hh:mm:ss aa"));
//            holder.chatSingleConvImage.setVisibility(View.INVISIBLE);
        }
//        else
//        {
//            holder.chatSingleText.setVisibility(View.INVISIBLE);
//            holder.chatSingleConvImage.setVisibility(View.VISIBLE);
//
//            Picasso.with(holder.chatSingleConvImage.getContext()).load(c.getMessage())
//                    .placeholder(R.drawable.man).into(holder.chatSingleConvImage);
//
//        }



        if(from_user.equals(currentUserID))
        {
            holder.chatSingleText.setBackgroundColor(Color.WHITE);
            holder.chatSingleText.setTextColor(Color.BLACK);
            holder.chatSingleImage.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.chatSingleText.setBackgroundResource(R.drawable.chat_text_background);
            holder.chatSingleText.setTextColor(Color.WHITE);

        }


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
        public ImageView chatSingleConvImage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            chatSingleText = itemView.findViewById(R.id.chatSingleText);
            chatSingleTime = itemView.findViewById(R.id.chatSingleTime);
            chatSingleImage = itemView.findViewById(R.id.chatSingleImage);
//            chatSingleConvImage = itemView.findViewById(R.id.chatSingleConvImage);


        }
    }
}
