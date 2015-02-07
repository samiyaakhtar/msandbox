package com.uwmsa.msandbox.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.*;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Models.User;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User currentUser = (User) User.getCurrentUser();
        if(currentUser == null || !currentUser.isAuthenticated()) {
            goToLoginScreen();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_main_logout) {
            User.logOut();
            goToLoginScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void goToLoginScreen() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(loginIntent);
    }

}
