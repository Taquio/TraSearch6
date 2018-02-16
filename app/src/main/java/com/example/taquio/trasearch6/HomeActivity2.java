package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity2 extends AppCompatActivity {
    private static final String TAG = "HomeActivity2";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity2.this;

    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Log.d(TAG, "onCreate: Starting");

        mAuth = FirebaseAuth.getInstance();

        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        setupBottomNavigationView();
        setupViewPager();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Log.d(TAG, "onStart: Calling back to start method");
            sendToStart();
        }
        else
        {
            Log.d(TAG, "onStart: User Online");
            mUserRef.child("online").setValue("true");
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop: Started");
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if(currentUser!=null) {
//            Log.d(TAG, "onStop: User Offline");
//            mUserRef.child("online").setValue(false);
//        }
//    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: OnPause Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            Log.d(TAG, "onPause: User Offline");
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }

    private void sendToStart()
    {
        Log.d(TAG, "sendToStart: Back to login page");
        startActivity(new Intent(HomeActivity2.this,ActivityLogin.class));
        finish();
    }

    private void setupViewPager() {
        Log.d(TAG, "setupViewPager: HomeActivity2 setupeViewPager started");
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new VideosFragment());
        adapter.addFragment(new ArticlesFragment());
        adapter.addFragment(new ItemsFragment());
        adapter.addFragment(new JunkShopsFragment());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Videos");
        tabLayout.getTabAt(1).setText("Articles");
        tabLayout.getTabAt(2).setText("Items");
        tabLayout.getTabAt(3).setText("Shops");

    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: HomeActivity2 setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
