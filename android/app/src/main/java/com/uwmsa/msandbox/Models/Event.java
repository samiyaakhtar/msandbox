package com.uwmsa.msandbox.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

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

    public Event() {

    }

}
