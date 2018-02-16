package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Models.User;
import com.example.taquio.trasearch6.Utils.ViewCommentsFragment;
import com.example.taquio.trasearch6.Utils.ViewPostFragment;
import com.example.taquio.trasearch6.Utils.ViewProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileActivity extends AppCompatActivity  implements
        ProfileFragment.OnGridImageSelectedListener ,
        ViewPostFragment.OnCommentThreadSelectedListener,
        ViewProfileFragment.OnGridImageSelectedListener{
//    private static final int ACTIVITY_NUM = 4;
//    private static final String TAG = "MyProfileActivity";
//    private Context mContext = MyProfileActivity.this;
//    private ProgressDialog progressDialog;
//
//    private DatabaseReference mUserDatabase,mAllUsersDatabase;
//    private FirebaseUser mCurrentUser;
//    private FirebaseAuth mAuth;
//    private StorageReference mImageStorage;
//
//    private Button logout2,myProfile_editBtn,chgn_image;
//    private TextView txtname,txtemail;
//    private EditText field_changeName;
//    private CircleImageView ivprofilepic;
//    private RecyclerView allUser;

    private static final String TAG = "ProfileActivity";
    private Context mContext = MyProfileActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }
    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        Log.d(TAG, "onGridImageSelected: selected an image gridview: " + photo.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putInt(getString(R.string.activity_number), activityNumber);

        fragment.setArguments(args);

        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();

    }
    @Override
    public void onCommentThreadSelectedListener(Photo photo) {
        Log.d(TAG, "onCommentThreadSelectedListener:  selected a comment thread");

        ViewCommentsFragment fragment = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();
    }
    private void init(){
        Log.d(TAG, "init: PAG INFLATE SA FRAGMENT NGA PROFILE " + getString(R.string.profile_fragment));

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "init: searching for user object attached as intent extra");
            if(intent.hasExtra(getString(R.string.intent_user))){
                User user = intent.getParcelableExtra(getString(R.string.intent_user));

                /* IF DI EQUAL SA USER NGA GA GAMIT ANG ID
                    IT MEANS LAIN NGA USERS VIEW ANG E INFLATE
                    PARA SA VIEW PROFILE
                */
                if(!user.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Log.d(TAG, "init: inflating view profile");
                    ViewProfileFragment fragment = new ViewProfileFragment();
                    Bundle args = new Bundle();
                    args.putParcelable(getString(R.string.intent_user),
                            intent.getParcelableExtra(getString(R.string.intent_user)));
                    fragment.setArguments(args);

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(getString(R.string.view_profile_fragment));
                    transaction.commit();
                }else{

                     /* IF ANG CURRENT USER GAGAMIT
                     IT MEAN ANG E INFLATE NGA LAYOUT KAY
                     IYAHANG PROFILE VIEW
                    */
                    Log.d(TAG, "init: inflating Profile");
                    ProfileFragment fragment = new ProfileFragment();
                    FragmentTransaction transaction = MyProfileActivity.this.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack(getString(R.string.profile_fragment));
                    transaction.commit();
                }
            }else{
                Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            }

        }else{
            Log.d(TAG, "init: inflating Profile");
            ProfileFragment fragment = new ProfileFragment();
            FragmentTransaction transaction = MyProfileActivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(getString(R.string.profile_fragment));
            transaction.commit();
        }
    }

}
//        setupBottomNavigationView();
//        refIDs();
//
//        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = mCurrentUser.getUid();
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//        mUserDatabase.keepSynced(true);
//
//        mUserDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(final DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: Has data");
//
//                txtname.setText(dataSnapshot.child("Name").getValue().toString());
//                txtemail.setText(dataSnapshot.child("Email").getValue().toString());
//                Picasso.with(MyProfileActivity.this).load(dataSnapshot.child("Image").getValue().toString())
//                        .networkPolicy(NetworkPolicy.OFFLINE)
//                        .placeholder(R.drawable.man)
//                        .into(ivprofilepic, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso.with(MyProfileActivity.this)
//                                        .load(dataSnapshot
//                                                .child("Image").getValue().toString())
//                                        .placeholder(R.drawable.man)
//                                        .into(ivprofilepic);
//                            }
//                        });
//                myProfile_editBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(MyProfileActivity.this,EditProfileActivity.class));
//                        finish();
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//    }
//
//    private void setupBottomNavigationView() {
//        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
//        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }
//
//    private void refIDs()
//    {
//        txtemail = findViewById(R.id.myProfile_email);
//        myProfile_editBtn = findViewById(R.id.myProfile_editBtn);
//        ivprofilepic = findViewById(R.id.myProfile_image);
//        txtname = findViewById(R.id.myProfile_name);
//
////        allUser = findViewById(R.id.allUsers);
////        allUser.setHasFixedSize(true);
////        allUser.setLayoutManager(new LinearLayoutManager(MyProfileActivity.this));
//    }
//}
