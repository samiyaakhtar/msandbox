package com.uwmsa.msandbox.Fragments;


import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.PrayerLocationMainAdapter;
import com.uwmsa.msandbox.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrayerLocationMainFragment extends Fragment implements ActionBar.TabListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ViewPager viewPager;
    private PrayerLocationMainAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Daily", "Jumuah" };

    public static PrayerLocationMainFragment newInstance(int sectionNumber) {
        PrayerLocationMainFragment fragment = new PrayerLocationMainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public PrayerLocationMainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prayer_location_main, container, false);


        viewPager = (ViewPager) view.findViewById(R.id.prayerRoomLocation_viewPager);
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        mAdapter = new PrayerLocationMainAdapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.removeAllTabs();
        for(String tab_name: tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
