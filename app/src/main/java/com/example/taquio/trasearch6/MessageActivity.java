package com.example.taquio.trasearch6;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class  MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    private  static final int TOTAL_ITEMS_TO_LOAD = 10;
    private final List<Chats> mChatList = new ArrayList<>();
    private Context mContext = MessageActivity.this;
    private String user_id;
    private DatabaseReference mRootRef,mMessageDatabase;
    private FirebaseAuth mAuth;
    private Toolbar mChatToolBar;
    private TextView chatUserName,chatUserLastSeen;
    private CircleImageView chatUserImage;
    private String currentUserID;
    private ImageButton ChatUser_addBtn,ChatUser_sendBtn;
    private EditText ChatUser_txtFld;
    private RecyclerView chatList;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayout;
    private ChatAdapter mAdapter;
    private int mCurrentPage = 1;

    private int itemPos = 0;
    private String mLastKey="",mPrevKey="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        refIDs();

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

        chatUserImage = findViewById(R.id.chatUserImage);
        chatUserLastSeen = findViewById(R.id.chatUserLastSeen);
        chatUserName = findViewById(R.id.chatUserName);
        mRefreshLayout = findViewById(R.id.swipeUpdate_swipeLayout);
        mAdapter = new ChatAdapter(mChatList);

        chatList.setHasFixedSize(true);
        chatList.setLayoutManager(mLinearLayout);
        chatList.setAdapter(mAdapter);




        user_id = getIntent().getStringExtra("user_id");
        String name = getIntent().getStringExtra("user_Name");
        currentUserID = mAuth.getCurrentUser().getUid();
        loadMessages();


        Toast.makeText(MessageActivity.this,user_id+name,Toast.LENGTH_SHORT).show();
        chatUserName.setText(name);

        mRootRef.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").getValue().toString();
                String image = dataSnapshot.child("Image_thumb").getValue().toString();
                if(online.equals("online"))
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

        mRootRef.child("Chat").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id))
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
                                Log.d(TAG, "onComplete: Error On creating Chat DB");
                            }
                            else
                            {
                                Log.d(TAG, "onComplete: Success Error On creating Chat DB");
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ChatUser_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;
                itemPos = 0;
                loadMOreMessages();
            }
        });

    }

    private void loadMOreMessages() {
        final DatabaseReference messageRef = mRootRef.child("Messages").child(currentUserID).child(user_id);
        Query mMessageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        mMessageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chats chats = dataSnapshot.getValue(Chats.class);

                String messageKey = dataSnapshot.getKey();

                if (itemPos == 1){
                    mLastKey =messageKey;
                }
                if(!mPrevKey.equals(messageKey))
                {
                    mChatList.add(itemPos++,chats);
                }else
                {
                    mPrevKey = mLastKey;
                }





                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
                mLinearLayout.scrollToPositionWithOffset(10,0 );
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadMessages() {

        final DatabaseReference messageRef = mRootRef.child("Messages").child(currentUserID).child(user_id);
        Query mMessageQuery = messageRef.limitToLast(mCurrentPage*TOTAL_ITEMS_TO_LOAD);

        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chats chats = dataSnapshot.getValue(Chats.class);
                itemPos++;
                String messageKey = dataSnapshot.getKey();

                if (itemPos == 1){
                    mLastKey =messageKey;
                    mPrevKey = messageKey;
                }

                mChatList.add(chats);
                mAdapter.notifyDataSetChanged();
                chatList.scrollToPosition(mChatList.size()-1);
                mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage() {
        String message = ChatUser_txtFld.getText().toString();

        if(!TextUtils.isEmpty(message))
        {
            String currentUserRef = "Messages/"+currentUserID+"/"+user_id;
            String chatUserRef = "Messages/"+user_id+"/"+currentUserID;

            DatabaseReference userMessagePust = mRootRef.child("Messages").child(currentUserID).child(user_id).push();
            String pushID = userMessagePust.getKey();

            Map messageMap = new HashMap();

            messageMap.put("message",message);
            messageMap.put("seened",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",currentUserID);

            Map messageUserMap = new HashMap();

            messageUserMap.put(currentUserRef+"/"+pushID,messageMap);
            messageUserMap.put(chatUserRef+"/"+pushID,messageMap);
            ChatUser_txtFld.setText("");

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!=null)
                    {
                        Toast.makeText(MessageActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void refIDs()
    {
        ChatUser_addBtn = findViewById(R.id.ChatUser_addBtn);
        ChatUser_sendBtn = findViewById(R.id.ChatUser_sendBtn);
        ChatUser_txtFld = findViewById(R.id.ChatUser_txtFld);
        chatList = findViewById(R.id.chatList);
        mLinearLayout = new LinearLayoutManager(MessageActivity.this);

    }
}