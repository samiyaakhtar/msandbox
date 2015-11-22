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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
                    for (int i = 0; i < prayerRoomLocations.size(); i++) {
                        PrayerRoomLocation prayerRoom = prayerRoomLocations.get(i);
                        ParseGeoPoint position = prayerRoom.getLocation();
                        if (position != null) {
                            LatLng point = new LatLng(position.getLatitude(), position.getLongitude());
                            BitmapDescriptor iconColour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

                            if (prayerRoom.getType().equals("Daily")) {
                                iconColour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                            }

                            mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title(prayerRoom.getRoomnumber())
                                .icon(iconColour));
                        }
                    }
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
        LatLng uw = new LatLng(43.472230, -80.5440463);
//        mMap.addMarker(new MarkerOptions()
//                .position(uw)
//                .title("UW")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uw));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

        getLocations();
    }
}
