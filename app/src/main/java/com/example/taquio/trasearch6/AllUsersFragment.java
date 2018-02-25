package com.example.taquio.trasearch6;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
                .findViewById(R.id.friendsList);
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


    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder
    {
        View mView;


        public AdminViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setStatus(String Status)
        {
            TextView userNameView  = mView.findViewById(R.id.allUsersEmail);
            userNameView.setText(Status);
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
