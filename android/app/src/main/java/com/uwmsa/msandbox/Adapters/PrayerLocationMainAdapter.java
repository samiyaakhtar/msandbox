package com.uwmsa.msandbox.Adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.uwmsa.msandbox.Fragments.PrayerLocationDailyFragment;
import com.uwmsa.msandbox.Fragments.PrayerLocationJumuahFragment;

/**
 * Created by dx179 on 3/25/15.
 */
public class PrayerLocationMainAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = { "Daily", "Jumuah" };

    public PrayerLocationMainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PrayerLocationDailyFragment();
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
