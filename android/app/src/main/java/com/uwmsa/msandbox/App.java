package com.uwmsa.msandbox;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
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
        // Enable Local Datastore with Parse.com
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Constants.APPLICATION_ID, Constants.CLIENT_ID );

        ParseObject.registerSubclass(User.class);
    }
}
