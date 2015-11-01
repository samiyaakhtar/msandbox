package com.uwmsa.msandbox.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by samiya on 28/02/15.
 */
@ParseClassName(Event.CLASSNAME)
public class Event extends ParseObject {
    public static final String CLASSNAME = "Event";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String LOCATION = "location";
    public static final String FACEBOOKEVENT = "facebookEvent";
    public static final String CATEGORY = "category";
    public static final String IMAGE = "image";
    public static final String OBJECTID = "objectId";
    public static final String TICKET_PRICE = "ticketPrice";


    public Event() {
    }

    public static void fetchEventInBackground(String objectId, GetCallback<Event> eventGetCallback) {
        ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
        eventQuery.getInBackground(objectId, eventGetCallback);
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public String getDescription() {
        return getString(DESCRIPTION);
    }

    public Date getStartTime() {
        return getDate(STARTTIME);
    }

    public Date getEndTime() {
        return getDate(ENDTIME);
    }

    public String getLocation() {
        return getString(LOCATION);
    }

    public String getFacebookEvent() {
        return getString(FACEBOOKEVENT);
    }

    public String getCategory() {
        return getString(CATEGORY);
    }

    public ParseFile getImage() {
        return getParseFile(IMAGE);
    }

    public Number getTicketPrice() {
        return getNumber(TICKET_PRICE);
    }

}
