package com.example.taquio.trasearch6;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taquio.trasearch6.Models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnverifiedUsers extends Fragment {


    private static final String TAG = "UnverifiedUsers";
    private RecyclerView mFriendList;
    private FirebaseAuth mAuth;
    private DatabaseReference mFriendsDatabase
            ,mUsersDatabase;

    private String mCurrent_user_id;

    private View mMainView;
    private User muser;
    public UnverifiedUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentmMainView = inflater
        mMainView = inflater
                .inflate(R.layout.fragment_unverified_users,container,false);
        mFriendList = mMainView
                .findViewById(R.id.unverifiedList);
        mAuth = FirebaseAuth
                .getInstance();
        mCurrent_user_id = mAuth.
                getCurrentUser()
                .getUid();
        mUsersDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");
        mUsersDatabase.keepSynced(true);

        mFriendList
                .setHasFixedSize(true);
        mFriendList
                .setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<AllUsers,unverifiedUsersViewHolder> allUsersRecyclerAdapter = new FirebaseRecyclerAdapter<AllUsers,unverifiedUsersViewHolder>(
                AllUsers.class,
                R.layout.unverifiedusers,
                unverifiedUsersViewHolder.class,
                mUsersDatabase.orderByChild("isVerify").equalTo(false)) {

            @Override
            protected void populateViewHolder(final unverifiedUsersViewHolder viewHolder, final AllUsers model, int position) {
//                viewHolder.setDate(model.getDate());

                final String list_user_Id = getRef(position).getKey();

                Log.d(TAG, "populateViewHolder: UserID: "+list_user_Id);

                mUsersDatabase.child(list_user_Id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.hasChild("isVerify"))
//                        {
                            boolean isVerified = dataSnapshot.child("isVerify").getValue(Boolean.class);
                            if(!isVerified)
                            {
                                Log.d(TAG, "populateViewHolder: Unverified Users: "+list_user_Id);
                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence options[] = new CharSequence[]{"Open Profile","Verify"};

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Select Options");
                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if(which==0)
                                                {
//                                                    startActivity(new Intent(getContext(), ViewProfile.class)
//                                                            .putExtra("user_id",list_user_Id));
                                                    Intent i = new Intent(getContext(), MyProfileActivity.class);
                                                    i.putExtra("viewprofile", "UnverifiedUsers");
                                                    i.putExtra("intent_userid", list_user_Id);
                                                    getContext().startActivity(i);
                                                }
                                                if(which==1)
                                                {
                                                    startActivity(new Intent(getContext(), AdminVerification.class)
                                                            .putExtra("user_id",list_user_Id));

                                                }
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                                String userType = dataSnapshot.child("userType").getValue().toString();
                                Log.d(TAG, "onDataChange: UserType: "+userType);
                                if (userType.equals("admin"))
                                {
                                    String email = dataSnapshot.child("Email").getValue().toString();
                                    String Name = dataSnapshot.child("Name").getValue().toString();
                                    String profile_thuumb = dataSnapshot.child("Image_thumb").getValue().toString();
                                    String isOnline =  dataSnapshot.child("online").getValue().toString();
                                    boolean isVerify = dataSnapshot.child("isVerify").getValue(Boolean.class);
                                    viewHolder.setEmail(email);
                                    viewHolder.setName(Name);
                                    viewHolder.setProfileImage(profile_thuumb,getContext());

                                    viewHolder.isVerify(isVerify);

                                    if(isOnline.equals("online"))
                                    {
                                        viewHolder.setuserOnline(true);
                                    }else
                                    {
                                        viewHolder.setuserOnline(false);
                                    }

                                }
                                else if (userType.equals("business"))
                                {
                                    Log.d(TAG, "onDataChange: BusinessType");
                                    String email = dataSnapshot.child("bsnEmail").getValue().toString();
                                    String Name = dataSnapshot.child("bsnBusinessName").getValue().toString();
                                    String profile_thuumb = dataSnapshot.child("image").getValue().toString();
//                                        String isOnline =  dataSnapshot.child("online").getValue().toString();
                                        boolean isVerify = dataSnapshot.child("isVerify").getValue(Boolean.class);
                                    viewHolder.setEmail(email);
                                    viewHolder.setName(Name);
                                    viewHolder.setProfileImage(profile_thuumb,getContext());

                                        viewHolder.isVerify(isVerify);

//                                        if(isOnline.equals("online"))
//                                        {
//                                            viewHolder.setuserOnline(true);
//                                        }else
//                                        {
//                                            viewHolder.setuserOnline(false);
//                                        }
                                }
                                else if(userType.equals("free"))
                                {
                                    Log.d(TAG, "onDataChange: Free Type");
                                    String email = dataSnapshot.child("Email").getValue().toString();
                                    String Name = dataSnapshot.child("Name").getValue().toString();
                                    String profile_thuumb = dataSnapshot.child("Image_thumb").getValue().toString();
                                    String isOnline =  dataSnapshot.child("online").getValue().toString();
                                    boolean isVerify = dataSnapshot.child("isVerify").getValue(Boolean.class);
                                    viewHolder.setEmail(email);
                                    viewHolder.setName(Name);
                                    viewHolder.setProfileImage(profile_thuumb,getContext());

                                    viewHolder.isVerify(isVerify);

                                    if(isOnline.equals("online"))
                                    {
                                        viewHolder.setuserOnline(true);
                                    }else
                                    {
                                        viewHolder.setuserOnline(false);
                                    }
                                }
                                else {
                                    Log.d(TAG, "onDataChange: NoType");
                                }
                            }
//                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };
        mFriendList.setAdapter(allUsersRecyclerAdapter);

    }

    public static class unverifiedUsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public unverifiedUsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setEmail(String Email)
        {
            TextView emailField = mView.findViewById(R.id.unverifiedEmail);
            emailField.setText(Email);
        }

        public void isVerify(Boolean verify)
        {
            ImageView userNameView  = mView.findViewById(R.id.unverifiedisVerify);

            if(verify)
            {
                userNameView.setImageResource(R.drawable.verify);
            }else
            {
                userNameView.setImageResource(R.drawable.not_verify);
            }
        }
        public void setName (String Name)
        {
            TextView nameView = mView.findViewById(R.id.unverifiedName);
            nameView.setText(Name);
        }
        public void setProfileImage (String ImageURL, Context cTHis)
        {
            CircleImageView mImageHolder = mView.findViewById(R.id.unverifiedImg);
            Picasso.with(cTHis).load(ImageURL)
                    .into(mImageHolder);
        }
        public void setuserOnline (Boolean online_status)
        {
            CircleImageView userOnlineView = mView.findViewById(R.id.unverifiedstat_icon);

            if(online_status)
            {
                userOnlineView.setVisibility(View.VISIBLE);
            }
            else
            {
                userOnlineView.setVisibility(View.INVISIBLE);
            }
        }
    }

}
