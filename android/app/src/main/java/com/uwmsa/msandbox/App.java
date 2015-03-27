package com.uwmsa.msandbox;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;
import com.uwmsa.msandbox.Models.Event;
import com.uwmsa.msandbox.Models.EventLike;
import com.uwmsa.msandbox.Models.PrayerRoomLocation;
import com.uwmsa.msandbox.Models.User;

/**
 * Created by samiya on 07/02/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupParse();
    }

    protected void setupParse() {
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseCrashReporting.enable(this);

        // Enable Local Datastore with Parse.com
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Constants.APPLICATION_ID, Constants.CLIENT_ID );

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(PrayerRoomLocation.class);
        ParseObject.registerSubclass(EventLike.class);
    }
}
