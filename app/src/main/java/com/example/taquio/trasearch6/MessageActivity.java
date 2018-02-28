package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class  MessageActivity extends AppCompatActivity {

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private static final int GALLERY_PICK = 1;
    private static final String TAG = "MessageActivity";
    private final List<Chats> mChatList = new ArrayList<>();
    private String mChatUser;
    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef;
    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;
    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayout;
    private ChatAdapter mAdapter;
    private int mCurrentPage = 1;
    // Storage Firebase
    private StorageReference mImageStorage;
    private ProgressDialog progressDialog;
    //New Solution
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mLinearLayout = new LinearLayoutManager(MessageActivity.this);
        mChatToolbar = findViewById(R.id.messageAppbar);
        setSupportActionBar(mChatToolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        mChatUser = getIntent().getStringExtra("user_id");
        String userName = getIntent().getStringExtra("user_name");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_chatappbar, null);

        actionBar.setCustomView(action_bar_view);

        // ---- Custom Action bar Items ----
        refIDs();
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        mMessagesList.setAdapter(mAdapter);

        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mTitleView.setText(userName);

        mRootRef.child("Users").child(mChatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String online = dataSnapshot.child("online").getValue().toString();

                if(dataSnapshot.hasChild("Image_thumb"))
                {
                    final String image = dataSnapshot.child("Image_thumb").getValue().toString();
                    Picasso.with(MessageActivity.this).load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.man)
                            .into(mProfileImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(MessageActivity.this)
                                            .load(image)
                                            .placeholder(R.drawable.man)
                                            .into(mProfileImage);
                                }
                            });
                }


                if(online.equals("online")) {

                    mLastSeenView.setText("Online");

                } else {

                    GetTimeAgo getTimeAgo = new GetTimeAgo();

                    long lastTime = Long.parseLong(online);

                    String lastSeenTime = GetTimeAgo.getTimeAgo(lastTime, getApplicationContext());

                    mLastSeenView.setText(lastSeenTime);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(mChatUser)){

                    Log.d(TAG, "onDataChange: IF");
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + mChatUser, chatAddMap);
                    chatUserMap.put("Chat/" + mChatUser + "/" + mCurrentUserId, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Log.d("CHAT_LOG", databaseError.getMessage().toString());
                            }
                        }
                    });

                }
//                else if(dataSnapshot.child(mChatUser).child("seen").getValue().toString().equals("true"))
//                {
//                    Log.d(TAG, "onDataChange: Nothing to do");
//                }
//                else if(dataSnapshot.child(mChatUser).child("seen").getValue().toString().equals("false"))
//                {
//
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mSeen = mRootRef.child("Chat")
                .child(mCurrentUserId)
                .child(mChatUser)
                .child("seen");
        ValueEventListener valueEventListener = mSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: Else" + dataSnapshot.getValue().toString());
                if (!dataSnapshot.getValue(Boolean.class))
                {
                    Log.d(TAG, "onDataChange: Else");
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", true);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + mCurrentUserId + "/" + mChatUser, chatAddMap);
//                    chatUserMap.put("Chat/" + mChatUser + "/" + mCurrentUserId, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError != null) {

                                Log.d("CHAT_LOG", databaseError.getMessage());

                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();

            }
        });



        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setBorderCornerColor(Color.GREEN)
                        .setFixAspectRatio(false)
                        .start(MessageActivity.this);
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

        loadMessages();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            progressDialog = new ProgressDialog(MessageActivity.this);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.setMessage("Please wait while we upload your image");
            progressDialog.show();
            Uri imageUri = result.getUri();

            progressDialog.setCanceledOnTouchOutside(false);
            File image_path = new File(imageUri.getPath());
            Bitmap thumbBitmap;
            try {
                thumbBitmap = new Compressor(MessageActivity.this)
                        .setMaxWidth(400).setMaxHeight(400).setQuality(90)
                        .compressToBitmap(image_path);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteData = baos.toByteArray();

                final String current_user_ref = "Messages/" + mCurrentUserId + "/" + mChatUser;
                final String chat_user_ref = "Messages/" + mChatUser + "/" + mCurrentUserId;

                DatabaseReference user_message_push = mRootRef.child("Messages")
                        .child(mCurrentUserId).child(mChatUser).push();

                final String push_id = user_message_push.getKey();


                StorageReference filepath = mImageStorage.child("Photos").child(mCurrentUserId).child("ConvoImages").child( push_id + ".jpg");

                UploadTask uploadTask = filepath.putBytes(byteData);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@android.support.annotation.NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            String download_url = task.getResult().getDownloadUrl().toString();


                            Map messageMap = new HashMap();
                            messageMap.put("message", download_url);
                            messageMap.put("seen", false);
                            messageMap.put("type", "image");
                            messageMap.put("time", ServerValue.TIMESTAMP);
                            messageMap.put("from", mCurrentUserId);

                            Map messageUserMap = new HashMap();
                            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                            mChatMessageView.setText("");

                            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if(databaseError != null){

                                        Log.d("CHAT_LOG", databaseError.getMessage().toString());
                                        progressDialog.dismiss();
                                    }else{
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadMOreMessages() {
        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Chats message = dataSnapshot.getValue(Chats.class);
                String messageKey = dataSnapshot.getKey();

                if(!mPrevKey.equals(messageKey)){

                    mChatList.add(itemPos++, message);

                } else {

                    mPrevKey = mLastKey;

                }


                if(itemPos == 1) {

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                mAdapter.notifyDataSetChanged();

                mRefreshLayout.setRefreshing(false);

                mLinearLayout.scrollToPositionWithOffset(10, 0);

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

        DatabaseReference messageRef = mRootRef.child("Messages").child(mCurrentUserId).child(mChatUser);

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Chats message = dataSnapshot.getValue(Chats.class);

                itemPos++;

                if(itemPos == 1){

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                mChatList.add(message);
                mAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(mChatList.size() - 1);

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
        String message = mChatMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){

            String current_user_ref = "Messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "Messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("Messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            mChatMessageView.setText("");

            mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);
            mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);

            mRootRef.child("Chat").child(mChatUser).child(mCurrentUserId).child("seen").setValue(false);
            mRootRef.child("Chat").child(mChatUser).child(mCurrentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }

                }
            });

        }
    }


    public void refIDs()
    {
        mTitleView = findViewById(R.id.chatUserName);
        mLastSeenView = findViewById(R.id.chatUserLastSeen);
        mProfileImage = findViewById(R.id.chatUserImage);

        mChatAddBtn = findViewById(R.id.ChatUser_addBtn);
        mChatSendBtn = findViewById(R.id.ChatUser_sendBtn);
        mChatMessageView = findViewById(R.id.ChatUser_txtFld);

        mAdapter = new ChatAdapter(mChatList);

        mMessagesList = findViewById(R.id.chatList);
        mRefreshLayout = findViewById(R.id.swipeUpdate_swipeLayout);

    }
}