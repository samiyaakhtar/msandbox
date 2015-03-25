package com.uwmsa.msandbox.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.PrayerRoomAdapter;
import com.uwmsa.msandbox.Models.PrayerRoomLocation;
import com.uwmsa.msandbox.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrayerLocationListFragment extends Fragment {

    private RecyclerView mPrayerRoomRecyclerView;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PrayerLocationListFragment newInstance(int sectionNumber) {
        PrayerLocationListFragment fragment = new PrayerLocationListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public PrayerLocationListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prayer_location_list, container, false);

        try {
            mPrayerRoomRecyclerView = (RecyclerView) view.findViewById(R.id.prayerRooms_recyclerView);
            mPrayerRoomRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mPrayerRoomRecyclerView.setLayoutManager(llm);

            getLocations();
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }

        return view;
    }

    private void getLocations() {
        ParseQuery<PrayerRoomLocation> query = ParseQuery.getQuery(PrayerRoomLocation.class);
        query.findInBackground(new FindCallback<PrayerRoomLocation>() {
            @Override
            public void done(List<PrayerRoomLocation> prayerRoomLocations, ParseException e) {
                if (e == null) {
                    PrayerRoomAdapter adapter = new PrayerRoomAdapter(prayerRoomLocations);
                    mPrayerRoomRecyclerView.setAdapter(adapter);
                } else {
                    // TODO: handle error
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
