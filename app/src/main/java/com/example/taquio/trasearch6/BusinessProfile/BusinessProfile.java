package com.example.taquio.trasearch6.BusinessProfile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.taquio.trasearch6.BusinessMessages.BusinessInboxFragment;
import com.example.taquio.trasearch6.Messages.FriendsListFragment;
import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.SectionsPagerAdapter;
import com.example.taquio.trasearch6.Utils.BusinessBottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessProfile extends AppCompatActivity {

    private static final String TAG = "BusinessProfile";
    private Context mContext = BusinessProfile.this;
    private static final int ACTIVITY_NUM = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_activity_profile);

        setupBottomNavigationView();

        setupViewPager();

    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BusinessInboxFragment());
        adapter.addFragment(new FriendsListFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.businessProfileContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.busProfileTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Selling");
        tabLayout.getTabAt(1).setText("Buying");
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.businessBottomNavViewBar);
        BusinessBottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BusinessBottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
