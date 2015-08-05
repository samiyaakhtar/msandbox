package com.uwmsa.msandbox.Fragments;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.uwmsa.msandbox.Activities.EventDetailsActivity;
import com.uwmsa.msandbox.Activities.LoginActivity;
import com.uwmsa.msandbox.Activities.MainActivity;
import com.uwmsa.msandbox.Adapters.NavigationDrawerAdapter;
import com.uwmsa.msandbox.Models.NavigationBarOption;
import com.uwmsa.msandbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment implements NavigationDrawerAdapter.ClickListener {

//    private android.support.v4.app.ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View containerView;
    private RecyclerView recyclerView;


    public static final String PREF_FILE_NAME = "userprefs";
    public static final String KEY_USER_USED_DRAWER= "user_used_drawer";

    private Boolean mUserUsedDrawer;
    private Boolean mFromSavedInstanceState;

    private NavigationDrawerAdapter navigationBarAdapter;
    private ItemSelectionListener itemSelectionListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserUsedDrawer = Boolean.getBoolean(readFromPreferences(getActivity(), KEY_USER_USED_DRAWER, "false"));

        if(savedInstanceState != null) {
            mFromSavedInstanceState = true;
        } else
            mFromSavedInstanceState = false;

    }

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.navbar_drawer_list);

        navigationBarAdapter = new NavigationDrawerAdapter(getActivity(), getData());
        navigationBarAdapter.SetClickListener(this);
        recyclerView.setAdapter(navigationBarAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


//        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectItem(position);
//            }
//        });

        return layout;
    }

    public static List<NavigationBarOption> getData() {
        List<NavigationBarOption> navigationBarOptions = new ArrayList<>();
        String[] optionTitles = {"Home Feed","Events","Prayer Information"};
        int [] optionIcons = {R.drawable.ic_home_black_48dp, R.drawable.ic_event_black_48dp, R.drawable.ic_people_black_48dp};

        for(int i = 0; i < optionTitles.length; i++ ) {
            NavigationBarOption currentOption = new NavigationBarOption();
            currentOption.title = optionTitles[i];
            currentOption.imageId = optionIcons[i];
            navigationBarOptions.add(currentOption);
        }

        return navigationBarOptions;
    }

    public void setItemSelectionListener (ItemSelectionListener itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        this.mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserUsedDrawer) {
                    mUserUsedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_USED_DRAWER, mUserUsedDrawer.toString());
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if(!mUserUsedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        this.mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void ItemClicked(int position) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        if(itemSelectionListener!= null)
            itemSelectionListener.ItemSelected(position);
    }

    public interface ItemSelectionListener {
        public void ItemSelected(int position);
    }
}
