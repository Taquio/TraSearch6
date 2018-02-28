package com.example.taquio.trasearch6.MapsDir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ARVN on 2/26/2018.
 */

public class MapsRetriveUserDetail extends AppCompatActivity{

    private static final String TAG = "MapsRetriveUserDetail";

    //database vars
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        //check if you are signed in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //get data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void showData(DataSnapshot dataSnapshot) {
//        for(DataSnapshot ds : dataSnapshot.getChildren()){
//            MapsDetailInformation uInfo = new MapsDetailInformation();
//            uInfo.setBusinessName(ds.child(userID).getValue(UserInformation.class).getName()); //set the name
//            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail()); //set the email
//            uInfo.setPhone_num(ds.child(userID).getValue(UserInformation.class).getPhone_num()); //set the phone_num
//
//            //display all the information
//            Log.d(TAG, "showData: name: " + uInfo.getName());
//            Log.d(TAG, "showData: email: " + uInfo.getEmail());
//            Log.d(TAG, "showData: phone_num: " + uInfo.getPhone_num());
//
//            ArrayList<String> array  = new ArrayList<>();
//            array.add(uInfo.getName());
//            array.add(uInfo.getEmail());
//            array.add(uInfo.getPhone_num());
//        }
//    }


}
