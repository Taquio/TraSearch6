package com.example.taquio.trasearch6.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = ProfileActivity.this;
    private ProgressBar mProgressbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        mProgressbar = findViewById(R.id);
        setupBottomNavigationView();
        setupToolBar();
    }

    private void setupToolBar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        ImageView profMenu = findViewById(R.id.profileMenu);
        profMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AccountSettingsActivity.class));
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
}
