package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.PrayerLocationDailyAdapter;
import com.uwmsa.msandbox.Models.PrayerRoomLocation;
import com.uwmsa.msandbox.R;

import java.util.List;

import io.karim.MaterialTabs;

/**
 * Created by dx179 on 3/27/15.
 */
public class PrayerLocationDailyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mPrayerRoomRecyclerView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MaterialTabs tabs;
    boolean constructorCalled;

    public PrayerLocationDailyFragment() {
        constructorCalled = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_prayer_location_daily, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshDaily);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        try {
            mPrayerRoomRecyclerView = (RecyclerView) rootView.findViewById(R.id.prayerRooms_recyclerView);
            mPrayerRoomRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mPrayerRoomRecyclerView.setLayoutManager(llm);

            getLocations();

        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }

        return rootView;
    }

    private void getLocations() {
        ParseQuery<PrayerRoomLocation> query = ParseQuery.getQuery(PrayerRoomLocation.class);
        query.whereEqualTo(PrayerRoomLocation.TYPE, "Daily");
        query.findInBackground(new FindCallback<PrayerRoomLocation>() {
            @Override
            public void done(List<PrayerRoomLocation> prayerRoomLocations, ParseException e) {
                if (e == null) {
                    PrayerLocationDailyAdapter adapter = new PrayerLocationDailyAdapter(prayerRoomLocations, mSwipeRefreshLayout.isRefreshing() || constructorCalled);
                    constructorCalled = false;
                    mPrayerRoomRecyclerView.setAdapter(adapter);
                    if(mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    // TODO: handle error
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getLocations();
    }
}
