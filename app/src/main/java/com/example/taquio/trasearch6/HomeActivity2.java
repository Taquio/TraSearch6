package com.example.taquio.trasearch6;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity2 extends AppCompatActivity {
    private static final String TAG = "HomeActivity2";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity2.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Log.d(TAG, "onCreate: Starting");

        setupBottomNavigationView();
        setupViewPager();

    }

    private void setupViewPager() {
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
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
