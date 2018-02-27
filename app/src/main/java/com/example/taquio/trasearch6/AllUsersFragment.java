package com.example.taquio.trasearch6;


import android.content.Context;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment {
    private static final String TAG = "AllUsersFragment";
    private RecyclerView mFriendList;
    private FirebaseAuth mAuth;
    private DatabaseReference mFriendsDatabase
            ,mUsersDatabase;

    private String mCurrent_user_id;

    private View mMainView;

    public AllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentmMainView = inflater
        mMainView = inflater
                .inflate(R.layout.fragment_all_users,container,false);
        mFriendList = mMainView
                .findViewById(R.id.userList);
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

        final FirebaseRecyclerAdapter<AllUsers,AdminViewHolder> allUsersRecyclerAdapter = new FirebaseRecyclerAdapter<AllUsers,AdminViewHolder>(
                AllUsers.class,
                R.layout.all_users,
                AllUsersFragment.AdminViewHolder.class,
                mUsersDatabase.orderByChild("isVerify")
        ) {
            @Override
            protected void populateViewHolder(final AdminViewHolder viewHolder, final AllUsers model, int position) {
//                viewHolder.setDate(model.getDate());

                final String list_user_Id = getRef(position).getKey();

                Log.d(TAG, "populateViewHolder: UserID: "+list_user_Id);

                        mUsersDatabase.child(list_user_Id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("userType"))
                                {
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
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


            }
        };
        mFriendList.setAdapter(allUsersRecyclerAdapter);

    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public AdminViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setEmail(String Email)
        {
            TextView emailField = mView.findViewById(R.id.allUsersEmail);
            emailField.setText(Email);
        }

        public void isVerify(Boolean verify)
        {
            ImageView userNameView  = mView.findViewById(R.id.isVerify);

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
            TextView nameView = mView.findViewById(R.id.allUsersName);
            nameView.setText(Name);
        }
        public void setProfileImage (String ImageURL, Context cTHis)
        {
            CircleImageView mImageHolder = mView.findViewById(R.id.allUsersImg);
            Picasso.with(cTHis).load(ImageURL)
                    .into(mImageHolder);
        }
        public void setuserOnline (Boolean online_status)
        {
            CircleImageView userOnlineView = mView.findViewById(R.id.stat_icon);

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
