package com.example.taquio.trasearch6;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "MessagesActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = AdminActivity.this;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupViewPager();



    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UnverifiedUsers());
        adapter.addFragment(new AllUsersFragment());
        ViewPager viewPager = findViewById(R.id.adminContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.adminTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Users (Not Yet Verified)");
        tabLayout.getTabAt(1).setText("All Users");
    }




    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
}
