package com.uwmsa.msandbox.Utilities;

import android.app.Activity;
import android.content.Intent;

import com.uwmsa.msandbox.Activities.LoginActivity;

/**
 * Created by dx179 on 3/24/15.
 */
public class Utilities {
    public enum NAVIGATION_DRAWER_ITEMS {
        HOME, EVENTS, PRAYER_LOCATIONS
    }

    public static NAVIGATION_DRAWER_ITEMS getNavigationDrawerItem(int position) {
        return Utilities.NAVIGATION_DRAWER_ITEMS.values()[position];
    }

    public static void goToLoginScreen(Activity currentActivity) {
        Intent loginIntent = new Intent(currentActivity, LoginActivity.class);
        currentActivity.startActivity(loginIntent);
    }
}


