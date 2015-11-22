package com.uwmsa.msandbox.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Models.PrayerRoomLocation;
import com.uwmsa.msandbox.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.karim.MaterialTabs;

/**
 * Created by dx179 on 3/27/15.
 */
public class PrayerLocationMapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private MaterialTabs tabs;
    boolean constructorCalled;
    private Context context;
    private GoogleMap mMap;

    public PrayerLocationMapFragment() {
        constructorCalled = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_prayer_location_map, null, false);

        try {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return homeView;
    }

    private void getLocations() {
        String[] prayerTypes = {"Daily", "Jumuah"};
        ParseQuery<PrayerRoomLocation> query = ParseQuery.getQuery(PrayerRoomLocation.class);
        query.whereContainedIn(PrayerRoomLocation.TYPE, Arrays.asList(prayerTypes));
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<PrayerRoomLocation>() {
            @Override
            public void done(List<PrayerRoomLocation> prayerRoomLocations, ParseException e) {
                if (e == null) {
                    System.out.println("Got locations");

                    for (int i = 0; i < prayerRoomLocations.size(); i++) {
                        ParseGeoPoint position = prayerRoomLocations.get(i).getLocation();
                        System.out.println(position);
                        if (position != null) {
                            LatLng point = new LatLng(position.getLatitude(), position.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(point).title(prayerRoomLocations.get(i).getRoomnumber()));
                            System.out.println("Added marker");
                        }
                    }
//
//                    PrayerLocationListAdapter adapter = new PrayerLocationListAdapter(orderedPrayerRoomLocations, mSwipeRefreshLayout.isRefreshing() || constructorCalled);
//                    constructorCalled = false;
//
//                    //This is the code to provide a sectioned list
//                    List<PrayerSectionedRecyclerViewAdapter.Section> sections =
//                            new ArrayList<PrayerSectionedRecyclerViewAdapter.Section>();
//
//                    //Sections
//                    sections.add(new PrayerSectionedRecyclerViewAdapter.Section(0, "Daily Prayers"));
//                    sections.add(new PrayerSectionedRecyclerViewAdapter.Section(dailyPrayerRoomLocations.size(), "Jumuah Prayers"));
//
//                    //Add your adapter to the sectionAdapter
//                    PrayerSectionedRecyclerViewAdapter.Section[] dummy = new PrayerSectionedRecyclerViewAdapter.Section[sections.size()];
//                    PrayerSectionedRecyclerViewAdapter mSectionedAdapter = new
//                            PrayerSectionedRecyclerViewAdapter(context, R.layout.prayer_section_title, R.id.section_text, adapter);
//                    mSectionedAdapter.setSections(sections.toArray(dummy));
//
//                    mPrayerRoomRecyclerView.setAdapter(mSectionedAdapter);
//                    if (mSwipeRefreshLayout.isRefreshing())
//                        mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    // TODO: handle error
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng uw = new LatLng(43.4722893, -80.5470463);
        mMap.addMarker(new MarkerOptions().position(uw).title("UW"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uw));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

        getLocations();
    }
}
