package com.uwmsa.msandbox.Fragments;


import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.PrayerLocationMainAdapter;
import com.uwmsa.msandbox.Fragments.PrayerLocationTabs.SlidingTabLayout;
import com.uwmsa.msandbox.R;

import io.karim.MaterialTabs;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrayerLocationMainFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ViewPager viewPager;
    private PrayerLocationMainAdapter mAdapter;
    private ActionBar actionBar;
    public MaterialTabs tabs;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prayer_location_main, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.prayerRoomLocation_viewPager);
        tabs = (MaterialTabs) view.findViewById(R.id.material_tabs);
        mAdapter = new PrayerLocationMainAdapter(getActivity().getSupportFragmentManager(), tabs);
        viewPager.setAdapter(mAdapter);
        tabs.setViewPager(viewPager);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
