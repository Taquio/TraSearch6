package com.example.taquio.trasearch6.BusinessHome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.taquio.trasearch6.BusinessMessages.BusinessInboxFragment;
import com.example.taquio.trasearch6.Messages.FriendsListFragment;
import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.SectionsPagerAdapter;
import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch6.Utils.BusinessBottomNavigationViewHelper;
import com.example.taquio.trasearch6.Utils.Donate;
import com.google.android.gms.ads.MobileAds;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessHome extends AppCompatActivity {

    private static final String TAG = "BusinessHome";
    private Context mContext = BusinessHome.this;
    private static final int ACTIVITY_NUM = 0;
    FloatingActionButton floating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_activity_home);

        MobileAds.initialize(BusinessHome.this, "ca-app-pub-3940256099942544~3347511713");
        setupBottomNavigationView();
        setupViewPager();
        floating = (FloatingActionButton) findViewById(R.id.floatingButton);

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessHome.this, Donate.class);
                startActivity(i);
            }
        });

    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BusinessVideoFragment());
        adapter.addFragment(new BusinessArticleFragment());
        adapter.addFragment(new BusinessItemsFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.businessHomeContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.busHomeTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Videos");
        tabLayout.getTabAt(1).setText("Articles");
        tabLayout.getTabAt(2).setText("Items");
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
