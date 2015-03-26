package com.uwmsa.msandbox.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by dx179 on 3/25/15.
 */
@ParseClassName(PrayerRoomLocation.CLASSNAME)
public class PrayerRoomLocation extends ParseObject {
    public static final String CLASSNAME = "PrayerRoomLocation";
    public static final String ROOMNUMBER = "roomNumber";
    public static final String DESCRIPTION = "Description";

    public PrayerRoomLocation() {

    }

    public String getRoomNumber() {
        return getString(ROOMNUMBER);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }
}
