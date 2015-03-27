package com.uwmsa.msandbox.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by dx179 on 3/27/15.
 */
@ParseClassName(EventLike.CLASSNAME)
public class EventLike extends ParseObject {
    public static final String CLASSNAME = "EventLike";
    public static final String EVENT = "event";
    public static final String USER = "user";

    public EventLike() {

    }

    public User getUser() {
        return (User)get(USER);
    }

    public Event getEvent() {
        return (Event)get(EVENT);
    }

    public void setUser() {
        put(USER, User.getCurrentUser());
    }

    public void setEvent(Event event) {
        put(EVENT, event);
    }
}
