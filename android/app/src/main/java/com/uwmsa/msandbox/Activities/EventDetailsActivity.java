package com.uwmsa.msandbox.Activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Utilities.Utilities;

public class EventDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, EventDetailsFragment.newInstance(getIntent().getStringExtra("eventObjectId")))
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EventDetailsFragment extends Fragment {

        private Event mEvent;
        private ParseImageView vImageView;
        private TextView vTitle;
        private TextView vDescription;
        private TextView vCategory;
        private TextView vFacebookEvent;
        private TextView vLocation;
        private TextView vStartTime;
        private TextView vEndTime;


        public EventDetailsFragment() {

        }

        public static final EventDetailsFragment newInstance(String objectId) {
            EventDetailsFragment fragment = new EventDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventObjectId", objectId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            String eventObjectId = getArguments().getString("eventObjectId");

            Event.fetchEventInBackground(eventObjectId, new GetCallback<Event>() {
                @Override
                public void done(Event event, ParseException e) {
                    if (e == null) {
                        mEvent = event;
                        fillEventDetailsView();
                    } else {
                        // TODO: handle event error
                    }
                }
            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);

            vImageView = (ParseImageView) rootView.findViewById(R.id.eventDetails_thumbnail);
            vTitle = (TextView) rootView.findViewById(R.id.eventDetails_title);
            vDescription = (TextView) rootView.findViewById(R.id.eventDetails_description);
            vLocation = (TextView) rootView.findViewById(R.id.eventDetails_location);
            vStartTime = (TextView) rootView.findViewById(R.id.eventDetails_startTime);
            vEndTime = (TextView) rootView.findViewById(R.id.eventDetails_endTime);

            return rootView;
        }

        protected void fillEventDetailsView() {
            vTitle.setText(mEvent.getTitle());
            vDescription.setText(mEvent.getDescription());
            vImageView.setParseFile(mEvent.getImage());
            vImageView.loadInBackground();
            vStartTime.setText(Utilities.getStringFromDate(mEvent.getStartTime()));
            vEndTime.setText(Utilities.getStringFromDate(mEvent.getEndTime()));
            vLocation.setText(mEvent.getLocation());
        }
    }
}
