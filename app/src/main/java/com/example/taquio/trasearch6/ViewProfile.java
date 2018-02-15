package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfile extends AppCompatActivity {
    private static final String TAG = "ViewProfile";
    TextView name,email,phoneNo;
    CircleImageView image;
    ProgressDialog mProgressDialog;
    Button sendRequest,declineRequest;
    FirebaseUser mCurrent_user;
    Integer mfriend_status;
    private DatabaseReference mUsersDatabase
            ,mFriendRequestDatabase
            ,mFriendDatabase
            ,mNotificatioonDatabase
            ,mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Log.d(TAG, "onCreate: View Profile started");
        //mFriend_status cheat codes:
        // 0 = not friends
        // 1 = req send
        // 2 = req recv
        // 3 = friends

        mfriend_status = 0;
        mProgressDialog = new ProgressDialog(ViewProfile.this);
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait while we load User Data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        refIDs();
        final String user_id = getIntent().getStringExtra("user_id");

        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("friend_request");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mNotificatioonDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Check Database");
                name.setText(dataSnapshot.child("Name").getValue().toString());
                email.setText(dataSnapshot.child("Email").getValue().toString());
                phoneNo.setText(dataSnapshot.child("PhoneNumber").getValue().toString());
                Picasso.with(ViewProfile.this).load(dataSnapshot.child("Image").getValue().toString())
                        .into(image);

                mFriendRequestDatabase
                        .child(mCurrent_user.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id))
                        {
                            Log.d(TAG, "onDataChange: user found in FriendRequest Database");
                            String req_type = dataSnapshot.
                                    child(user_id)
                                    .child("request_type")
                                    .getValue()
                                    .toString();

                            if(req_type.equals("received"))
                            {
                                Log.d(TAG, "onDataChange: Request type: Received");
                                mfriend_status = 2;
                                sendRequest.setText("Accept Friend Request");
                                declineRequest.setVisibility(View.VISIBLE);
                                declineRequest.setEnabled(true);
                            }
                            else if(req_type.equals("sent"))
                            {
                                Log.d(TAG, "onDataChange: Request type: Sent");
                                mfriend_status = 1;
                                sendRequest.setText("Cancel Friend Request");
                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                            }
                            mProgressDialog.dismiss();

                        }else{
                            mFriendDatabase.child(mCurrent_user.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id))
                                    {
                                        sendRequest.setEnabled(true);
                                        mfriend_status = 3;
                                        sendRequest.setText("Unfriend this Person");
                                        declineRequest.setVisibility(View.INVISIBLE);
                                        declineRequest.setEnabled(false);
                                    }
                                    mProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgressDialog.dismiss();

                                }
                            });
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest.setEnabled(false);
                //mFriend_status cheat codes:
                // 0 = not friends
                // 1 = req send
                // 2 = req recv
                // 3 = friends

                if(mfriend_status == 0)
                {
                    DatabaseReference newNotificationref = mRootRef.child("Notifications").child(user_id).push();
                    String newNotificationID = newNotificationref.getKey();

                    Map notificationData = new HashMap();
                    notificationData.put("from", mCurrent_user.getUid());
                    notificationData.put("type","request");

                    Map requestMap = new HashMap();
                    requestMap.put("friend_request/"+mCurrent_user.getUid()+"/"+user_id+"/request_type","sent");
                    requestMap.put("friend_request/"+user_id+"/"+mCurrent_user.getUid()+"/request_type","received");
                    requestMap.put("Notifications/"+user_id+"/"+newNotificationID,notificationData);

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null)
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(ViewProfile.this
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }

                            sendRequest.setEnabled(true);
                            mfriend_status = 1;
                            sendRequest.setText("Cancel Friend Request");
                        }
                    });
                }
                else if (mfriend_status==1)
                {
                    Log.d(TAG, "onClick: Cancel friend request Started");
                    Map cancelReqMap = new HashMap();

                    cancelReqMap.put("friend_request/"+mCurrent_user.getUid()+"/"+user_id,null);
                    cancelReqMap.put("friend_request/"+user_id+"/"+mCurrent_user.getUid(),null);

                    mRootRef.updateChildren(cancelReqMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                sendRequest.setText("Send Friend Request");
                                sendRequest.setEnabled(true);
                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                                mfriend_status = 0;
                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(ViewProfile.this
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });


                }
                else if(mfriend_status==2)
                {
                    final String current_date = DateFormat.getDateInstance().format(new Date());

                    Map friendMap = new HashMap();

                    friendMap.put("Friends/"+mCurrent_user.getUid()+"/"+user_id+"/date",current_date);
                    friendMap.put("Friends/"+user_id+"/"+mCurrent_user.getUid()+"/date",current_date);

                    friendMap.put("friend_request/"+mCurrent_user.getUid()+"/"+user_id,null);
                    friendMap.put("friend_request/"+user_id+"/"+mCurrent_user.getUid(),null);

                    mRootRef.updateChildren(friendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                sendRequest.setEnabled(true);
                                mfriend_status = 3;
                                sendRequest.setText("Unfriend this Person");

                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(ViewProfile.this
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
                else if(mfriend_status==3)
                {
                    Map unfriendMap = new HashMap();

                    unfriendMap.put("Friends/"+mCurrent_user.getUid()+"/"+user_id,null);
                    unfriendMap.put("Friends/"+user_id+"/"+mCurrent_user.getUid(),null);

                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                sendRequest.setEnabled(true);
                                mfriend_status = 0;
                                sendRequest.setText("Send Friend Request");

                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(ViewProfile.this
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
            }
        });

        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Decline Friend Request Clicked");

                Map declineMap = new HashMap();

                declineMap.put("friend_request/"+mCurrent_user.getUid()+user_id,null);
                declineMap.put("friend_request/"+user_id+mCurrent_user.getUid(),null);

                mRootRef.updateChildren(declineMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null)
                        {
                            sendRequest.setText("Send Friend Request");
                            declineRequest.setVisibility(View.INVISIBLE);
                            declineRequest.setEnabled(false);
                            mfriend_status = 0;
                        }
                        else
                        {
                            String error = databaseError.getMessage();
                            Toast.makeText(ViewProfile.this
                                    ,error
                                    ,Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

            }
        });

    }

    public void refIDs()
    {


        name = findViewById(R.id.vProfile_Name);
        email = findViewById(R.id.vProfile_email);
        phoneNo = findViewById(R.id.vProfile_number);
        image = findViewById(R.id.vProfile_image);
        sendRequest = findViewById(R.id.vProfile_sendFREquest_btn);
        declineRequest = findViewById(R.id.vProfile_declineFREquest_btn);
    }
}
