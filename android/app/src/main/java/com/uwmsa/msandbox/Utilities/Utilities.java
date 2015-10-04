package com.uwmsa.msandbox.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.uwmsa.msandbox.Activities.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dx179 on 3/24/15.
 */
public class Utilities {
    public static Activity mainActivity;

    public enum NAVIGATION_DRAWER_ITEMS {
        HOME, EVENTS, PRAYER_LOCATIONS
    }

    public static NAVIGATION_DRAWER_ITEMS getNavigationDrawerItem(int position) {
        return Utilities.NAVIGATION_DRAWER_ITEMS.values()[position];
    }

    public static void goToLoginScreen(Activity currentActivity) {
        mainActivity = currentActivity;
        Intent loginIntent = new Intent(mainActivity, LoginActivity.class);
        mainActivity.startActivityForResult(loginIntent, 0);
    }

    public static String getStringFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
        return dateFormat.format(date);
    }
}


