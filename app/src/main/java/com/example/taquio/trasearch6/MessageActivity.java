package com.example.taquio.trasearch6;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class  MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    private Context mContext = MessageActivity.this;

    private String user_id;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    private Toolbar mChatToolBar;

    private TextView chatUserName,chatUserLastSeen;
    private CircleImageView chatUserImage;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mChatToolBar =  findViewById(R.id.messageAppbar);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(mChatToolBar);
        ActionBar actionBar =  getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_chatappbar,null);
        actionBar.setCustomView(action_bar_view);
        refIDs();

        user_id = getIntent().getStringExtra("user_id");
        String name = getIntent().getStringExtra("user_Name");


        Toast.makeText(MessageActivity.this,user_id+name,Toast.LENGTH_SHORT).show();
        chatUserName.setText(name);



        mRootRef.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").getValue().toString();
                String image = dataSnapshot.child("Image_thumb").getValue().toString();
                if(online.equals("true"))
                {
                    chatUserLastSeen.setText("online");
                }
                else
                {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTIme = Long.parseLong(online);
                    String lastSeen = GetTimeAgo.getTimeAgo(lastTIme,MessageActivity.this);
                    chatUserLastSeen.setText(lastSeen);
                }
                Picasso.with(MessageActivity.this).load(dataSnapshot.child("Image_thumb").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.man)
                        .into(chatUserImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(MessageActivity.this)
                                        .load(dataSnapshot
                                                .child("Image_thumb").getValue().toString())
                                        .placeholder(R.drawable.man)
                                        .into(chatUserImage);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        currentUserID = mAuth.getCurrentUser().getUid();
        mRootRef.child("Chat").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    Map chatAddMap = new HashMap<>();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map userMap = new HashMap<>();
                    userMap.put("Chat/"+currentUserID+"/"+user_id,chatAddMap);
                    userMap.put("Chat/"+user_id+"/"+currentUserID,chatAddMap);

                    mRootRef.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null)
                            {
                                Log.d(TAG, "onComplete: ");
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void refIDs()
    {
        chatUserImage = findViewById(R.id.chatUserImage);
        chatUserLastSeen = findViewById(R.id.chatUserLastSeen);
        chatUserName = findViewById(R.id.chatUserName);


    }
}
