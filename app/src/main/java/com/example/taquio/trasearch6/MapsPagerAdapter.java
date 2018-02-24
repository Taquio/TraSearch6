package com.example.taquio.trasearch6;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ARVN on 2/24/2018.
 */

public class MapsPagerAdapter extends FragmentPagerAdapter
{
    int noTabs;

    public MapsPagerAdapter(FragmentManager fm, int tabNo) {
        super(fm);
        this.noTabs = tabNo;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0: //recycling center tab
                return new MapsFragmentRecycling();
            case 1: //junkyard tab
                return new MapsFragmentJunkyard();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return noTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0: //recycling center tab
                return "Recycling Center";
            case 1: //junkyard tab
                return "Junkyard";
            default:
                return null;
        }
    }
}
