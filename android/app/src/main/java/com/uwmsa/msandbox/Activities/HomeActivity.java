package com.uwmsa.msandbox.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.uwmsa.msandbox.Adapters.EventAdapter;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.Models.User;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, EventAdapter.OnEventClickListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Used to display events in a list of cards
     */
    private RecyclerView mEventRecyclerView;

    private int currentNavigationDrawerSelectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        currentNavigationDrawerSelectedIndex = 0;

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_home_layout));

        // Set up the event recycler view
        mEventRecyclerView = (RecyclerView) findViewById(R.id.main_eventRecyclerView);
        mEventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mEventRecyclerView.setLayoutManager(llm);

        fillEventsRecyclerView();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

        if (currentNavigationDrawerSelectedIndex != position) {
            currentNavigationDrawerSelectedIndex = position;
            fillEventsRecyclerView();
        }
    }

    public void fillEventsRecyclerView() {
        if (currentNavigationDrawerSelectedIndex == Utilities.NAVIGATION_DRAWER_ITEMS.EVENTS.ordinal()) {
            ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.CLASSNAME);

            eventQuery.findInBackground(new FindCallback<Event>() {
                @Override
                public void done(List<Event> events, ParseException e) {
                    if (e != null) {
                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EventAdapter eventAdapter = new EventAdapter(events);
                    eventAdapter.setOnEventClickListener(HomeActivity.this);
                    mEventRecyclerView.setAdapter(eventAdapter);
                }
            });
        } else {
            EventAdapter eventAdapter = new EventAdapter(new ArrayList<Event>());
            mEventRecyclerView.setAdapter(eventAdapter);
        }
    }

    public void onSectionAttached(int number) {
        Utilities.NAVIGATION_DRAWER_ITEMS itemName = Utilities.getNavigationDrawerItem(number - 1);
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
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_home, menu);
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
        if (id == R.id.action_home_logout) {
            User.logOut();
            //TODO: goToLoginScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnEventClickListener(Event event) {
        Intent eventDetailsIntent = new Intent(HomeActivity.this, EventDetails.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("event", event);
        eventDetailsIntent.putExtras(bundle);

        HomeActivity.this.startActivity(eventDetailsIntent);
    }
}
