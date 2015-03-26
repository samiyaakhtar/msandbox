package com.uwmsa.msandbox.Activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
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

    private Event mEvent;
    private ParseImageView vImageView;
    private TextView vTitle;
    private TextView vDescription;
    private TextView vCategory;
    private TextView vFacebookEvent;
    private TextView vLocation;
    private TextView vStartTime;
    private TextView vEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        String eventObjectId = getIntent().getStringExtra("eventObjectId");
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

        vImageView = (ParseImageView) findViewById(R.id.eventDetails_thumbnail);
        vTitle = (TextView) findViewById(R.id.eventDetails_title);
        vDescription = (TextView) findViewById(R.id.eventDetails_description);
        vLocation = (TextView) findViewById(R.id.eventDetails_location);
        vStartTime = (TextView) findViewById(R.id.eventDetails_startTime);
        vEndTime = (TextView) findViewById(R.id.eventDetails_endTime);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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


    protected void fillEventDetailsView() {
        vTitle.setText(mEvent.getTitle());
        vDescription.setText(mEvent.getDescription());
        vImageView.setParseFile(mEvent.getImage());
        vImageView.loadInBackground();
        vStartTime.setText("Starts: " + Utilities.getStringFromDate(mEvent.getStartTime()));
        vEndTime.setText("Ends: " + Utilities.getStringFromDate(mEvent.getEndTime()));
        vLocation.setText("Location: " + mEvent.getLocation());

        setTitle(mEvent.getTitle());
    }
}
