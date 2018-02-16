package com.example.taquio.trasearch6.BusinessMessages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.taquio.trasearch6.Messages.FriendsListFragment;
import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.SectionsPagerAdapter;
import com.example.taquio.trasearch6.Utils.BusinessBottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessMessages extends AppCompatActivity {

    private static final String TAG = "BusinessMessages";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = BusinessMessages.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_activity_messages);

        setupBottomNavigationView();
        setupViewPager();
    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BusinessInboxFragment());
        adapter.addFragment(new FriendsListFragment());
        ViewPager viewPager = findViewById(R.id.businessMessagesContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.busMessagesTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Inbox");
        tabLayout.getTabAt(1).setText("Friends List");
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.businessBottomNavViewBar);
        BusinessBottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BusinessBottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
