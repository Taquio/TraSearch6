package com.example.taquio.trasearch6;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ARVN on 2/21/2018.
 */

public class MapPagerAdapter extends FragmentStatePagerAdapter {

    int mNoTabs;

    public MapPagerAdapter(FragmentManager fm, int noTabs) {
        super(fm);
        this.mNoTabs = noTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
            {
                MapFragmentRecyclingCenter mfRecyclingCenter = new MapFragmentRecyclingCenter();
                return mfRecyclingCenter;
            }
            case 1:
            {
                MapFragmentJunkyard mfJunkyard = new MapFragmentJunkyard();
                return mfJunkyard;
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoTabs;
    }
}
