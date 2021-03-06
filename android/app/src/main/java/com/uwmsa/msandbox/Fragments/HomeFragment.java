package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.EventAdapter;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.Models.Hadith;
import com.uwmsa.msandbox.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PREF_FILE_NAME = "userprefs";
    private final String PRAYER_TIMES_LABEL = "PrayerTimes";

    private boolean hadithExpanded = false;

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

    private TextView englishHadith;
    private TextView arabicHadith;

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

        context = getActivity();
        prefs = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);

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

        englishHadith = (TextView) homeView.findViewById(R.id.englishHadith);
        arabicHadith = (TextView) homeView.findViewById(R.id.arabicHadith);

        final ImageButton showFullHadithButton = (ImageButton) homeView.findViewById(R.id.hadithExpandButton);

        showFullHadithButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hadithExpanded) {
                    englishHadith.setMaxLines(2);
                    arabicHadith.setMaxLines(2);
                    showFullHadithButton.setImageResource(R.drawable.ic_keyboard_arrow_down);
                } else {
                    englishHadith.setMaxLines(Integer.MAX_VALUE);
                    arabicHadith.setMaxLines(Integer.MAX_VALUE);
                    showFullHadithButton.setImageResource(R.drawable.ic_keyboard_arrow_up);
                }
                hadithExpanded = !hadithExpanded;
            }
        });

        setPrayerTimes();
        setDailyHadith();

        return homeView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void setDailyHadith() {
        Calendar c = Calendar.getInstance();
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        ParseQuery<Hadith> hadithParseQuery = ParseQuery.getQuery(Hadith.CLASSNAME);
        hadithParseQuery.whereEqualTo("number", dayOfMonth);
        hadithParseQuery.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        hadithParseQuery.findInBackground(new FindCallback<Hadith>() {
            @Override
            public void done(List<Hadith> ahadith, ParseException e) {
                if (e != null) {
                    try {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    } catch (NullPointerException ne) {
                        // User has switched fragments to something other than the event list while
                        // there was no network connection, so the toast has no context and throws an error
                        Log.e("Error", ne.getMessage());
                    }
                } else {
                    Hadith matchHadith = ahadith.get(0);
                    englishHadith.setText(matchHadith.getEnglishText());
                    arabicHadith.setText(matchHadith.getArabicText());
                }
            }
        });
    }

    private void setPrayerTimes() {
        Calendar c = Calendar.getInstance();
        // c.set(Calendar.DAY_OF_YEAR, 400); // For testing bugs
        List<Date> days = new ArrayList<>();
        final List<String> formattedDays = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat sf = new SimpleDateFormat("MM/dd/yyyy");

        Date today = c.getTime();
        final String todayFormattedDate = df.format(today);

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

        queryPrayerTimes(todayFormattedDate, formattedDays, true);
        queryPrayerTimes(todayFormattedDate, formattedDays, false);
    }

    private JSONObject processParsePrayerTimeToJSON(ParseObject prayerTime) {
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

    private void updatePrayerTimeUI(JSONObject data) {
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

    private void queryPrayerTimes(final String todayFormattedDate, final List<String> formattedDays, final Boolean useCache) {
        System.out.println("Running query");
        ParseQuery query = new ParseQuery("PrayerTimes");
        query.setLimit(365);
        query.whereContainedIn("date", formattedDays);
        if (useCache) {
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ONLY);
        } else {
            query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findInBackground(new FindCallback() {
            @Override
            public void done(final List list, ParseException e) {
                if (e != null) {
                    // There was an error or the network wasn't available.
                    Log.e("No network", e.getMessage());
                    return;
                }

                if (list.size() > 0) {
                    JSONObject parseToJSON;
                    for (int i = 0; i < list.size(); i++) {
                        prayerTime = (ParseObject) list.get(i);
                        if (prayerTime.getString("date").equals(todayFormattedDate)) {
                            parseToJSON = processParsePrayerTimeToJSON(prayerTime);
                            updatePrayerTimeUI(parseToJSON);
                        }
                    }
                }
            }
        });
    }
}
