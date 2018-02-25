package com.example.taquio.trasearch6;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        final FirebaseRecyclerAdapter<Friends,AllUsersFragment.AdminViewHolder> friendRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, AllUsersFragment.AdminViewHolder>(
                Friends.class,
                R.layout.all_users,
                AllUsersFragment.AdminViewHolder.class,
                mFriendsDatabase
        ) {
            @Override
            protected void populateViewHolder(final AllUsersFragment.AdminViewHolder viewHolder, Friends model, int position) {
//                viewHolder.setDate(model.getDate());

                final String list_user_Id = getRef(position).getKey();
                mUsersDatabase.child(list_user_Id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child("Name").getValue().toString();
                        if(dataSnapshot.hasChild("Image_thumb"))
                        {
                            String user_thumb_img = dataSnapshot.child("Image_thumb").getValue().toString();
                            viewHolder.setProfileImage(user_thumb_img,getContext());
                        }

                        if(dataSnapshot.hasChild("online"))
                        {
                            String user_online = dataSnapshot.child("online").getValue().toString();
                            if(user_online.equals("online"))
                            {
                                viewHolder.setuserOnline(true);
                            }
                            else
                            {
                                viewHolder.setuserOnline(false);
                            }
                        }
                        viewHolder.setEmail(dataSnapshot.child("Email").getValue().toString());
                        viewHolder.setName(name);
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{"Open Profile","Send Message"};

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle(name+"\nSelect Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which==0)
                                        {
                                            startActivity(new Intent(getContext(), ViewProfile.class)
                                                    .putExtra("user_id",list_user_Id));
                                        }
                                        if(which==1)
                                        {
                                            startActivity(new Intent(getContext(), MessageActivity.class)
                                                    .putExtra("user_id",list_user_Id)
                                                    .putExtra("user_Name",name));

                                        }
                                    }
                                });
                                builder.show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        mFriendList.setAdapter(friendRecyclerAdapter);


    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public AdminViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setEmail(String email)
        {
            TextView emailField = mView.findViewById(R.id.allUsersEmail);
            emailField.setText(email);
        }

        public void isVerify(Boolean verify)
        {
            TextView userNameView  = mView.findViewById(R.id.allUsersEmail);
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
