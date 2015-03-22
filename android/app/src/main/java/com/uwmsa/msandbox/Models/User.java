package com.uwmsa.msandbox.Models;

import com.parse.ParseUser;

/**
 * Created by samiya on 04/02/15.
 */
public class User extends ParseUser {
    public static final String CLASSNAME = "User";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String UWSTUDENTID = "uwStudentId";
    public static final String EMAIL = "email";


    public User() {

    }


    public void setUwStudentId(String uwStudentId) {
        put(UWSTUDENTID, uwStudentId);
    }

    public String getUwStudentId() {
        return getString(UWSTUDENTID);
    }


}
