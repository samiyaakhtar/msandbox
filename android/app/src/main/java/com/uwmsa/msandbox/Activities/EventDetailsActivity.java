package com.uwmsa.msandbox.Activities;

import android.graphics.drawable.Drawable;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Utilities.Utilities;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;

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
    ShareActionProvider shareActionProvider;
    BottomSheetLayout bottomSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        context = this;

        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.container);

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

//
//        // Create the share Intent
//        String playStoreLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
//        String yourShareText = "Test Share Link" + playStoreLink;
//        Intent shareIntent = ShareCompat.IntentBuilder.from(this).setType("text/plain").setText(yourShareText).getIntent();
//
//
//        MenuItem menuItem = menu.findItem(R.id.ed_action_share);
//        shareActionProvider = new ShareActionProvider(this);
//        shareActionProvider.setShareIntent(shareIntent);
//        MenuItemCompat.setActionProvider(menuItem, shareActionProvider);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Toast.makeText(context, "SOME SELECTED", Toast.LENGTH_LONG).show();
//        if(item.getItemId() == R.id.ed_action_share){
//            Toast.makeText(context, "SHARE SELECTED", Toast.LENGTH_LONG).show();
//            onShareAction();
//        }

        Toast.makeText(context, "SOME SELECTED", Toast.LENGTH_LONG).show();
        if(item.getItemId() == R.id.ed_action_share){
            Toast.makeText(context, "SHARE SELECTED", Toast.LENGTH_LONG).show();
            bottomSheetShareAction();
        }




        return super.onOptionsItemSelected(item);
    }

    private void onShareAction(){
        // Create the share Intent
        String playStoreLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
        String yourShareText = "Test Share Link" + playStoreLink;
        Intent shareIntent = ShareCompat.IntentBuilder.from(this).setType("text/plain").setText(yourShareText).getIntent();
        // Set the share Intent
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
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
    public void onBackPressed() {
        MainActivity.comingFromEventDetail =true;
        super.onBackPressed();
    }



    private void bottomSheetShareAction(){

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the MSA APP");
        shareIntent.setType("text/plain");
        IntentPickerSheetView intentPickerSheet = new IntentPickerSheetView(EventDetailsActivity.this, shareIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
            @Override
            public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {
                bottomSheetLayout.dismissSheet();
                startActivity(activityInfo.getConcreteIntent(shareIntent));
            }
        });
        // Filter out built in sharing options such as bluetooth and beam.
        intentPickerSheet.setFilter(new IntentPickerSheetView.Filter() {
            @Override
            public boolean include(IntentPickerSheetView.ActivityInfo info) {
                return !info.componentName.getPackageName().startsWith("com.android");
            }
        });
        // Sort activities in reverse order for no good reason
        intentPickerSheet.setSortMethod(new Comparator<IntentPickerSheetView.ActivityInfo>() {
            @Override
            public int compare(IntentPickerSheetView.ActivityInfo lhs, IntentPickerSheetView.ActivityInfo rhs) {
                return rhs.label.compareTo(lhs.label);
            }
        });

        // Add custom mixin example
//        Drawable customDrawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, null);
//        IntentPickerSheetView.ActivityInfo customInfo = new IntentPickerSheetView.ActivityInfo(customDrawable, "Custom mix-in", EventDetailsActivity.this, MainActivity.class);
//        intentPickerSheet.setMixins(Collections.singletonList(customInfo));

        bottomSheetLayout.showWithSheetView(intentPickerSheet);
    }
}
