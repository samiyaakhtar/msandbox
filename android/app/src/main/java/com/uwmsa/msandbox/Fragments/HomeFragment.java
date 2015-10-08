package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.PrayerLocationMainAdapter;
import com.uwmsa.msandbox.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.karim.MaterialTabs;

public class HomeFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView fajrTime;
    private TextView dhuhrTime;
    private TextView asrTime;
    private TextView maghribTime;
    private TextView ishaTime;
    private ParseObject prayerTime;

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

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        final String formattedDate = df.format(c.getTime());

        ParseQuery query = new ParseQuery("PrayerTimes");
        query.whereEqualTo("date", formattedDate);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List list, ParseException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        prayerTime = (ParseObject) list.get(0);
                        HashMap eqamaTime = (HashMap) prayerTime.get("eqama");
                        fajrTime.setText(eqamaTime.get("fajr").toString());
                        List<String> dhuhrList = Arrays.asList(eqamaTime.get("dhuhr").toString().split(","));
                        dhuhrTime.setText(dhuhrList.get(0));
                        asrTime.setText(eqamaTime.get("asr").toString());
                        maghribTime.setText(eqamaTime.get("maghrib").toString());
                        ishaTime.setText(eqamaTime.get("isha").toString());
                    } else {
                        System.out.println("No appropriate results for " + formattedDate);
                    }
                } else {
                    Log.e("Prayer times failed: ", e.getMessage());
                }
            }
        });

        return homeView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
