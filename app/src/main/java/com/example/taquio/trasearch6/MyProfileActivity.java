package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Models.User;
import com.example.taquio.trasearch6.Utils.ViewPostFragment;
import com.example.taquio.trasearch6.Utils.ViewProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Del Mar on 2/7/2018.
 */

public class MyProfileActivity extends AppCompatActivity implements
        ProfileFragment.OnGridImageSelectedListener ,
        ViewProfileFragment.OnGridImageSelectedListener{

    private static final String TAG = "ProfileActivity";
    private Context mContext = MyProfileActivity.this;
    private ProgressBar mProgressbar;
    private CircleImageView profilePhoto;
    private FirebaseDatabase mfirebaseDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();
        profilePhoto = findViewById(R.id.myProfile_image);


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
    private void init(){
        Log.d(TAG, "init: PAG INFLATE SA FRAGMENT NGA PROFILE " + getString(R.string.profile_fragment));

        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.calling_activity))){
            Log.d(TAG, "init: searching for user object attached as intent extra"  );
            if(intent.hasExtra(getString(R.string.intent_user))){
                User user = intent.getParcelableExtra(getString(R.string.intent_user));
                Log.d(TAG, "init: THIS IS A TEST FOR " +intent.getParcelableExtra(getString(R.string.intent_user)) );

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



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        mfirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
        mDatabase = mfirebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDatabase = mfirebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }

}
