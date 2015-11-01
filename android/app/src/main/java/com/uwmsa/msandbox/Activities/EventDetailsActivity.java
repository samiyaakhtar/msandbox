package com.uwmsa.msandbox.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Utilities.Utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class EventDetailsActivity extends ActionBarActivity {

    private Context context;
    private Event mEvent;
    private ParseImageView vImageView;
    private TextView vLocation;
    private TextView vPrice;
    private TextView vStartEndTime;
    private TextView vDescription;
    private TextView vTitle;

    private ScrollView scrollView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        context = this;

        vImageView = (ParseImageView) findViewById(R.id.eventDetails_thumbnail);
        vLocation = (TextView) findViewById(R.id.ed_location_venue_tv);
        vPrice = (TextView) findViewById(R.id.ed_price_tv);
        vDescription = (TextView) findViewById(R.id.ed_more_info_tv);
        vStartEndTime = (TextView) findViewById(R.id.ed_start_end_tv);
        vTitle = (TextView) findViewById(R.id.ed_title_tv);

        String eventObjectId = getIntent().getStringExtra("eventObjectId");
        Event.fetchEventInBackground(eventObjectId, new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                if (e == null) {
                    mEvent = event;
                    fillEventDetailsView();
                } else {
                    // TODO: handle event error
                    Toast.makeText(context, "Oops, something went wrong ", Toast.LENGTH_LONG).show();
                }
            }
        });

        toolbar = (Toolbar) findViewById(R.id.transparent_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.eventdetails_menu, menu);
        return true;
    }


    protected void fillEventDetailsView() {
        vDescription.setText(mEvent.getDescription());
        vTitle.setText(mEvent.getTitle());
        vLocation.setText(mEvent.getLocation());

        DecimalFormat df = new DecimalFormat("#.##");
        vPrice.setText("$"+df.format(mEvent.getTicketPrice().doubleValue()));

        vImageView.setParseFile(mEvent.getImage());
        vImageView.loadInBackground();

        vStartEndTime.setText(getFormattedStartEndDate());
    }


    private String getFormattedStartEndDate(){
        String date;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
        date = dateFormat.format(mEvent.getStartTime());
        date += " - " + dateFormat.format(mEvent.getEndTime()) + ", ";

        dateFormat = new SimpleDateFormat("hh:mm");
        date += dateFormat.format(mEvent.getStartTime()) + " - ";

        dateFormat = new SimpleDateFormat("hh:mm a");
        date += dateFormat.format(mEvent.getStartTime());

        return date;
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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
