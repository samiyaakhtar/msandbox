package com.uwmsa.msandbox.Adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.uwmsa.msandbox.Fragments.PrayerLocationDailyFragment;
import com.uwmsa.msandbox.Fragments.PrayerLocationJumuahFragment;

import io.karim.MaterialTabs;

/**
 * Created by dx179 on 3/25/15.
 */
public class PrayerLocationMainAdapter extends FragmentStatePagerAdapter {

    private String[] tabTitles = { "Daily", "Jumuah" };
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
                return  new PrayerLocationDailyFragment();
            case 1:
                return new PrayerLocationJumuahFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
