package com.uwmsa.msandbox.Models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.json.JSONObject;

/**
 * Created by dx179 on 3/25/15.
 */
@ParseClassName(PrayerRoomLocation.CLASSNAME)
public class PrayerRoomLocation extends ParseObject {
    public static final String CLASSNAME = "PrayerRoomLocation";
    public static final String ROOMNUMBER = "roomNumber";
    public static final String USERCOUNT = "userCount";
    public static final String BUILDING = "building";
    public static final String DESCRIPTION = "Description";
    public static final String TYPE = "type";
    public static final String LOCATION = "location";
    public static final String ENTRYCODE = "entryCode";

    public PrayerRoomLocation() {    }

    public String getBuilding() {
        return getString(BUILDING);
    }

    public Number getUsercount() {
        return getNumber(USERCOUNT);
    }

    public String getRoomnumber() {
        return getString(ROOMNUMBER);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public String getType() {
        return getString(TYPE);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(LOCATION);
    }

    public JSONObject getEntryCodeObj() {
        return getJSONObject(ENTRYCODE);
    }
}
