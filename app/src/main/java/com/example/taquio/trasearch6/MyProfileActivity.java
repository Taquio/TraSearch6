package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 4;
    private static final String TAG = "MyProfileActivity";
    private Context mContext = MyProfileActivity.this;
    private ProgressDialog progressDialog;

    private DatabaseReference mUserDatabase,mAllUsersDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private StorageReference mImageStorage;

    private Button logout2,myProfile_editBtn,chgn_image;
    private TextView txtname,txtemail;
    private EditText field_changeName;
    private CircleImageView ivprofilepic;
    private RecyclerView allUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        setupBottomNavigationView();
        refIDs();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Has data");

                txtname.setText(dataSnapshot.child("Name").getValue().toString());
                txtemail.setText(dataSnapshot.child("Email").getValue().toString());
                //setProfileImage
                Picasso.with(MyProfileActivity.this).load(dataSnapshot.child("Image").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.man)
                        .into(ivprofilepic, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(MyProfileActivity.this)
                                        .load(dataSnapshot
                                                .child("Image").getValue().toString())
                                        .placeholder(R.drawable.man)
                                        .into(ivprofilepic);
                            }
                        });
                myProfile_editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MyProfileActivity.this,EditProfileActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void refIDs()
    {
        txtemail = findViewById(R.id.myProfile_email);
        myProfile_editBtn = findViewById(R.id.myProfile_editBtn);
        ivprofilepic = findViewById(R.id.myProfile_image);
        txtname = findViewById(R.id.myProfile_name);

//        allUser = findViewById(R.id.allUsers);
//        allUser.setHasFixedSize(true);
//        allUser.setLayoutManager(new LinearLayoutManager(MyProfileActivity.this));
    }
}
