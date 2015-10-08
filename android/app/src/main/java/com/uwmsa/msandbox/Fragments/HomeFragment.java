package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.PrayerLocationMainAdapter;
import com.uwmsa.msandbox.R;

import io.karim.MaterialTabs;

public class HomeFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView fajrTime;
    private TextView dhuhrTime;
    private TextView asrTime;
    private TextView maghribTime;
    private TextView ishaTime;

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        fajrTime = (TextView) homeView.findViewById(R.id.fajrTime);
        dhuhrTime = (TextView) homeView.findViewById(R.id.dhuhrTime);
        asrTime = (TextView) homeView.findViewById(R.id.asrTime);
        maghribTime = (TextView) homeView.findViewById(R.id.maghribTime);
        ishaTime = (TextView) homeView.findViewById(R.id.ishaTime);

        // ParseQuery query = new ParseQuery();

        fajrTime.setText("6:03");
        dhuhrTime.setText("8:03");
        asrTime.setText("10:03");
        maghribTime.setText("2:03");
        ishaTime.setText("8:03");

        return homeView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
