package com.uwmsa.msandbox.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.R;


public class HalalFoodFinderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String PREF_FILE_NAME = "userprefs";

    private Context context;
    private SharedPreferences prefs;

    public static HalalFoodFinderFragment newInstance(int sectionNumber) {
        HalalFoodFinderFragment fragment = new HalalFoodFinderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HalalFoodFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View halalFoodFinderView = inflater.inflate(R.layout.fragment_halal_food_finder, container, false);

        context = getActivity();
        prefs = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);

        return halalFoodFinderView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
