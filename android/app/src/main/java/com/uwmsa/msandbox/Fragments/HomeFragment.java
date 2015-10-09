package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.PrayerLocationMainAdapter;
import com.uwmsa.msandbox.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.karim.MaterialTabs;

public class HomeFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PREF_FILE_NAME = "userprefs";
    private final String PRAYER_TIMES_LABEL = "PrayerTimes";

    private TextView fajrTime;
    private TextView dhuhrTime;
    private TextView asrTime;
    private TextView maghribTime;
    private TextView ishaTime;
    private ParseObject prayerTime;

    private Context context;
    private SharedPreferences prefs;

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

        context = getActivity();
        prefs = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);

        setPrayerTimes();

        return homeView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void setPrayerTimes() {
        Calendar c = Calendar.getInstance();
        // c.set(Calendar.DAY_OF_YEAR, 1); // For testing bugs
        List<Date> days = new ArrayList<>();
        final List<String> formattedDays = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        Date today = c.getTime();
        final String todayFormattedDate = df.format(today);

        String lastSuccessfulQueryDate = prefs.getString("mostRecentNewPrayerDataDate", "None");
        if (lastSuccessfulQueryDate.equals(todayFormattedDate)) {
            System.out.println("Times have already been retrieved today");
            String lastSuccessfulQuery = prefs.getString("mostRecentNewPrayerData", "None");
            if (!lastSuccessfulQuery.equals("None")) {
                try {
                    JSONObject jsonObject = new JSONObject(lastSuccessfulQuery);
                    updatePrayerTimeUI(jsonObject);
                } catch (JSONException jEx) {
                    Log.e("JSON exception: ", jEx.getMessage());
                }
            }
        } else {
            days.add(today);
            formattedDays.add(todayFormattedDate);

            for (int i = 0; i < 364; i++) { // 364 because 0-363 is 364 and today has already been added, making 365 days (approx. 1 year)
                c.add(Calendar.DAY_OF_YEAR, 1);
                Date newDate = c.getTime();
                days.add(newDate);
            }

            for (int i = 1; i < 365; i++) {
                String formattedDate = df.format(days.get(i));
                formattedDays.add(formattedDate);
            }

            ParseQuery query = new ParseQuery("PrayerTimes");
            query.setLimit(365);
            query.whereContainedIn("date", formattedDays);
            query.fromLocalDatastore();
            query.findInBackground(new FindCallback() {
                @Override
                public void done(final List list, ParseException e) {
                    if (e != null) {
                        // There was an error or the network wasn't available.
                        return;
                    }

                    // Release any objects previously pinned for this query.
                    ParseObject.unpinAllInBackground(PRAYER_TIMES_LABEL, list, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                // There was some error.
                                Log.e("Failed in unpin: ", e.getMessage());
                                return;
                            }

                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(PRAYER_TIMES_LABEL, list);

                            JSONObject parseToJSON = new JSONObject();
                            for (int i = 0; i < list.size(); i++) {
                                prayerTime = (ParseObject) list.get(i);
                                if (prayerTime.getString("date").equals(todayFormattedDate)) {
                                    parseToJSON = processParsePrayerTimeToJSON(prayerTime);
                                    updatePrayerTimeUI(parseToJSON);
                                }
                            }

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("mostRecentNewPrayerDataDate", todayFormattedDate);
                            editor.putString("mostRecentNewPrayerData", parseToJSON.toString());
                            editor.apply();
                        }
                    });
                }
            });
        }
    }

    public JSONObject processParsePrayerTimeToJSON(ParseObject prayerTime) {
        JSONObject storeAsJson = new JSONObject();
        JSONObject eqamaTime = prayerTime.getJSONObject("eqama");
        try {
            storeAsJson.put("date", prayerTime.getString("date"));
            storeAsJson.put("fajrIqamah", eqamaTime.getString("fajr"));
            List<String> dhuhrList = Arrays.asList(eqamaTime.getString("dhuhr").split(","));
            storeAsJson.put("dhuhrIqamah", dhuhrList.get(0));
            storeAsJson.put("asrIqamah", eqamaTime.getString("asr"));
            storeAsJson.put("maghribIqamah", eqamaTime.getString("maghrib"));
            storeAsJson.put("ishaIqamah", eqamaTime.getString("isha"));
        } catch (JSONException jEx) {
            Log.e("JSON exception: ", jEx.getMessage());
        }

        return storeAsJson;
    }

    public void updatePrayerTimeUI(JSONObject data) {
        try {
            fajrTime.setText(data.getString("fajrIqamah"));
            dhuhrTime.setText(data.getString("dhuhrIqamah"));
            asrTime.setText(data.getString("asrIqamah"));
            maghribTime.setText(data.getString("maghribIqamah"));
            ishaTime.setText(data.getString("ishaIqamah"));
        } catch (JSONException jEx) {
            Log.e("JSON exception: ", jEx.getMessage());
        }
    }
}
