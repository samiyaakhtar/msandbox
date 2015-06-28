package com.uwmsa.msandbox.Activities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;
import com.uwmsa.msandbox.Adapters.PrayerLocationDailyAdapter;
import com.uwmsa.msandbox.Fragments.EventListFragment;
import com.uwmsa.msandbox.Fragments.NavigationDrawerFragment;
import com.uwmsa.msandbox.Fragments.PlaceholderFragment;
import com.uwmsa.msandbox.Fragments.PrayerLocationMainFragment;
import com.uwmsa.msandbox.Models.*;
import com.uwmsa.msandbox.Utilities.*;
import com.uwmsa.msandbox.R;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                User cUser = (User) User.getCurrentUser();
                if(cUser == null || !cUser.isAuthenticated()) {
                    Utilities.goToLoginScreen(this);
                }
            } else {
                Utilities.goToLoginScreen(this);
            }
        } catch (Exception ex) {
            Log.e("Failed: ", ex.getMessage());
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_home_layout));

//        ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4B9D8F")));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();


        Utilities.NAVIGATION_DRAWER_ITEMS itemName = Utilities.getNavigationDrawerItem(position);
        switch (itemName) {

            case EVENTS:
                fragment = EventListFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                break;
            case PRAYER_LOCATIONS:
                fragment = PrayerLocationMainFragment.newInstance(position);
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                break;
            case HOME:
            default:
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                fragment = PlaceholderFragment.newInstance(position);
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

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
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
}
