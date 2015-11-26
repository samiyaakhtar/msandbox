package com.uwmsa.msandbox.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.uwmsa.msandbox.Fragments.PrayerLocationListFragment;
import com.uwmsa.msandbox.Fragments.PrayerLocationMapFragment;

import io.karim.MaterialTabs;

/**
 * Created by dx179 on 3/25/15.
 */
public class PrayerLocationMainAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = { "List", "Map" };
    private MaterialTabs tabs;
    public PrayerLocationMainAdapter(FragmentManager fm, MaterialTabs tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PrayerLocationListFragment();
            case 1:
                return new PrayerLocationMapFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
