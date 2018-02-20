package com.example.taquio.trasearch6;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by taquio on 2/16/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{

    private List<Chats> mMessageList;
    private DatabaseReference mUserDatabase;

    public ChatAdapter(List<Chats> mMessageList) {

        this.mMessageList = mMessageList;

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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_single_layout ,parent, false);

        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        Chats c = mMessageList.get(position);

        String from_user = c.getFrom();
        String message_type = c.getType();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("Image_thumb").getValue().toString();
                Picasso.with(holder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.man).into(holder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        if(message_type.equals("text")) {

            holder.messageText.setText(c.getMessage());
            holder.messageImage.setVisibility(View.INVISIBLE);
            holder.chatSingleTime.setText(getDate(c.getTime(),"hh:mm:ss aa"));


        } else {

            holder.messageText.setVisibility(View.INVISIBLE);
            holder.messageImage.setVisibility(View.VISIBLE);
            Picasso.with(holder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.man).into(holder.messageImage);
            holder.chatSingleTime.setText(getDate(c.getTime(),"hh:mm:ss aa"));

        }


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder
    {
        public TextView messageText,chatSingleTime;
        public CircleImageView profileImage;
        public ImageView messageImage;

        public ChatViewHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.chatSingleText);
            profileImage = view.findViewById(R.id.chatSingleImage);
            messageImage = view.findViewById(R.id.chatSingleConvImage);
            chatSingleTime = view.findViewById(R.id.chatSingleTime);

        }
    }
}
