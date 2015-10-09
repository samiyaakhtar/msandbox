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
import java.text.DateFormat;
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

    private TextView fajrIqamahTime;
    private TextView dhuhrIqamahTime;
    private TextView asrIqamahTime;
    private TextView maghribIqamahTime;
    private TextView ishaIqamahTime;
    private TextView fajrStartTime;
    private TextView dhuhrStartTime;
    private TextView asrStartTime;
    private TextView maghribStartTime;
    private TextView ishaStartTime;
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

        fajrIqamahTime = (TextView) homeView.findViewById(R.id.fajrIqamahTime);
        dhuhrIqamahTime = (TextView) homeView.findViewById(R.id.dhuhrIqamahTime);
        asrIqamahTime = (TextView) homeView.findViewById(R.id.asrIqamahTime);
        maghribIqamahTime = (TextView) homeView.findViewById(R.id.maghribIqamahTime);
        ishaIqamahTime = (TextView) homeView.findViewById(R.id.ishaIqamahTime);
        fajrStartTime = (TextView) homeView.findViewById(R.id.fajrStartTime);
        dhuhrStartTime = (TextView) homeView.findViewById(R.id.dhuhrStartTime);
        asrStartTime = (TextView) homeView.findViewById(R.id.asrStartTime);
        maghribStartTime = (TextView) homeView.findViewById(R.id.maghribStartTime);
        ishaStartTime = (TextView) homeView.findViewById(R.id.ishaStartTime);

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
        // c.set(Calendar.DAY_OF_YEAR, 400); // For testing bugs
        List<Date> days = new ArrayList<>();
        final List<String> formattedDays = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat sf = new SimpleDateFormat("MM/dd/yyyy");

        Date today = c.getTime();
        final String todayFormattedDate = df.format(today);

        String lastSuccessfulQueryDate = prefs.getString("mostRecentNewPrayerDataDate", "None");

        Boolean gotLastDate = false;
        Date lastDate;
        Date lastDateModified = addDaystoDate(today, 1); // Guarantees times won't be retrieved from SharedPreferences if lastDateModified is not set properly

        if (!lastSuccessfulQueryDate.equals("None")) {
            try {
                lastDate = sf.parse(lastSuccessfulQueryDate);
                lastDateModified = addDaystoDate(lastDate, 7); // Checking if at least one week has passed since last update
                gotLastDate = true;
            } catch (java.text.ParseException parseEx) {
                Log.e("Last query date error: ", parseEx.getMessage());
            }
        }

        if (gotLastDate && !(today.after(lastDateModified))) {
            System.out.println("Last date: " + lastDateModified);
            System.out.println("Today: " + today);
            System.out.println("Times have already been retrieved in the last week.");
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

            queryPrayerTimes(todayFormattedDate, formattedDays, false);
        }
    }

    public JSONObject processParsePrayerTimeToJSON(ParseObject prayerTime) {
        JSONObject storeAsJson = new JSONObject();
        JSONObject eqamaTime = prayerTime.getJSONObject("eqama");
        JSONObject startTime = prayerTime.getJSONObject("beginning");
        try {
            storeAsJson.put("date", prayerTime.getString("date"));
            storeAsJson.put("fajrStart", startTime.getString("fajr"));
            storeAsJson.put("dhuhrStart", startTime.getString("dhuhr"));
            storeAsJson.put("asrStart", startTime.getString("asr"));
            storeAsJson.put("maghribStart", startTime.getString("maghrib"));
            storeAsJson.put("ishaStart", startTime.getString("isha"));
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
            fajrIqamahTime.setText(data.getString("fajrIqamah"));
            dhuhrIqamahTime.setText(data.getString("dhuhrIqamah"));
            asrIqamahTime.setText(data.getString("asrIqamah"));
            maghribIqamahTime.setText(data.getString("maghribIqamah"));
            ishaIqamahTime.setText(data.getString("ishaIqamah"));
            fajrStartTime.setText(data.getString("fajrStart"));
            dhuhrStartTime.setText(data.getString("dhuhrStart"));
            asrStartTime.setText(data.getString("asrStart"));
            maghribStartTime.setText(data.getString("maghribStart"));
            ishaStartTime.setText(data.getString("ishaStart"));
        } catch (JSONException jEx) {
            Log.e("JSON exception: ", jEx.getMessage());
        }
    }

    public Date addDaystoDate(Date inDate, int addition) {
        Calendar dateManipulationCal = Calendar.getInstance();
        dateManipulationCal.setTime(inDate);
        dateManipulationCal.add(Calendar.DATE, addition);
        Date outDate = dateManipulationCal.getTime();
        return outDate;
    }

    public void queryPrayerTimes(final String todayFormattedDate, final List<String> formattedDays, Boolean useDatastore) {
        System.out.println("Running query");
        ParseQuery query = new ParseQuery("PrayerTimes");
        query.setLimit(365);
        query.whereContainedIn("date", formattedDays);
        if (useDatastore) {
            query.fromLocalDatastore();
        }
        query.findInBackground(new FindCallback() {
            @Override
            public void done(final List list, ParseException e) {
                if (e != null) {
                    // There was an error or the network wasn't available.
                    Log.e("No network: ", e.getMessage());
                    queryPrayerTimes(todayFormattedDate, formattedDays, true);
                    return;
                }

                if (list.size() > 0) {
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
            }
        });
    }
}
