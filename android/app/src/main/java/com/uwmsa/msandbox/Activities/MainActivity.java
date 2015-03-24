package com.uwmsa.msandbox.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import com.parse.ParseUser;
import com.uwmsa.msandbox.Models.*;
import com.uwmsa.msandbox.Utilities.*;
import com.uwmsa.msandbox.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                User cUser = (User) User.getCurrentUser();
                if(cUser == null || !cUser.isAuthenticated()) {
                    Utilities.goToLoginScreen(this);
                }
            }
        } catch (Exception ex) {
            Log.e("Failed: ", ex.getMessage());
        }

        Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
        MainActivity.this.startActivity(homeIntent);
    }
}
