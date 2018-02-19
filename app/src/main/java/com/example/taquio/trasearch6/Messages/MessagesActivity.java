package com.example.taquio.trasearch6.Messages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.taquio.trasearch6.FriendsFragment;
import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.SectionsPagerAdapter;
import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = "MessagesActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = MessagesActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        setupBottomNavigationView();
        setupViewPager();

    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InboxFragment());
        adapter.addFragment(new FriendsFragment());
        ViewPager viewPager = findViewById(R.id.messageContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.messageTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Inbox");
        tabLayout.getTabAt(1).setText("Friends List");
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
