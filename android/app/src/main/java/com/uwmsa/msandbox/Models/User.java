package com.uwmsa.msandbox.Models;

import com.parse.ParseUser;

/**
 * Created by samiya on 04/02/15.
 */
public class User extends ParseUser {
    public String uwStudentId;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String UWSTUDENTID = "uwStudentId";


    public User() {

    }

    public void putUsername(String username) {
        put(USERNAME, username);
    }

    public void putPassword(String password) {
        put(PASSWORD, password);
    }

    public void putUwStudentId(String uwStudentId) {
        put(UWSTUDENTID, uwStudentId);
    }

    public String getUsername() {
        return getString(USERNAME);
    }

    public String getUwStudentId() {
        return getString(UWSTUDENTID);
    }


}
