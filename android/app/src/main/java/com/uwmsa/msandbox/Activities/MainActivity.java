package com.uwmsa.msandbox.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;
import com.uwmsa.msandbox.Adapters.NavigationDrawerAdapter;
import com.uwmsa.msandbox.Fragments.EventListFragment;
import com.uwmsa.msandbox.Fragments.GalleryFragment;
import com.uwmsa.msandbox.Fragments.HalalFoodFinderFragment;
import com.uwmsa.msandbox.Fragments.HomeFragment;
import com.uwmsa.msandbox.Fragments.NavigationDrawerFragment;
import com.uwmsa.msandbox.Fragments.ParticipateFragment;
import com.uwmsa.msandbox.Fragments.PlaceholderFragment;
import com.uwmsa.msandbox.Fragments.PrayerLocationMainFragment;
import com.uwmsa.msandbox.Models.*;
import com.uwmsa.msandbox.Utilities.*;
import com.uwmsa.msandbox.R;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.ItemSelectionListener {

    private CharSequence mTitle;
    public static boolean comingFromEventDetail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                User cUser = (User) User.getCurrentUser();
                if (cUser == null || !cUser.isAuthenticated()) {
                    Utilities.goToLoginScreen(this);
                }
            } else {
                Utilities.goToLoginScreen(this);
            }
        } catch (Exception ex) {
            Log.e("Failed: ", ex.getMessage());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        mTitle = getTitle();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_home_layout);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout, toolbar);
        navigationDrawerFragment.setItemSelectionListener(this);

        if (comingFromEventDetail) {
            Fragment fragment;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment = EventListFragment.newInstance(0);
            fragmentManager.beginTransaction().replace(R.id.container_layout, fragment).commit();
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            mTitle = getString(R.string.title_section2);
            comingFromEventDetail = false;
        } else if (savedInstanceState == null) {
            Fragment fragment;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment = HomeFragment.newInstance(0);
            fragmentManager.beginTransaction().replace(R.id.container_layout, fragment).commit();
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void ItemSelected(int position) {
        final Fragment fragment;
        final FragmentManager fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();

        Utilities.NAVIGATION_DRAWER_ITEMS itemName = Utilities.getNavigationDrawerItem(position);
        switch (itemName) {

            case HOME:
                fragment = HomeFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case EVENTS:
                fragment = EventListFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case PRAYER_LOCATIONS:
                fragment = PrayerLocationMainFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case HALAL_FOOD_FINDER:
                fragment = HalalFoodFinderFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case PARTICIPATE:
                fragment = ParticipateFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case GALLERY:
                fragment = GalleryFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            default:
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                fragment = PlaceholderFragment.newInstance(position);
                break;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                fragmentManager.beginTransaction()
                        .replace(R.id.container_layout, fragment)
                        .commit();
            }

        }, 50);

    }

    public void onSectionAttached(int number) {
        Utilities.NAVIGATION_DRAWER_ITEMS itemName = Utilities.getNavigationDrawerItem(number);
        switch (itemName) {
            case HOME:
                mTitle = getString(R.string.title_section1);
                break;
            case EVENTS:
                mTitle = getString(R.string.title_section2);
                break;
            case PRAYER_LOCATIONS:
                mTitle = getString(R.string.title_section3);
                break;
            case HALAL_FOOD_FINDER:
                mTitle = getString(R.string.title_section4);
                break;
            case PARTICIPATE:
                mTitle = getString(R.string.title_section5);
                break;
            case GALLERY:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main_logout) {
            User.logOut();
            Utilities.goToLoginScreen(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}
