package com.uwmsa.msandbox.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.uwmsa.msandbox.R;
import com.uwmsa.msandbox.Models.User;

public class SignupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupButtonListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    protected void setupButtonListeners() {
        Button signUpButton = (Button) findViewById(R.id.signup_signupButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String username = ((EditText)findViewById(R.id.signup_usernameTextField)).getText().toString();
            String password = ((EditText)findViewById(R.id.signup_passwordTextField)).getText().toString();
            String confirmPassword = ((EditText)findViewById(R.id.signup_confirmPasswordTextField)).getText().toString();
            String uwStudentId = ((EditText)findViewById(R.id.signup_uwStudentIdTextField)).getText().toString();
            String email = ((EditText)findViewById(R.id.signup_emailAddressTextField)).getText().toString();

            if(!confirmPassword.equals(password)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            } else if(password.equals("")) {
                Toast.makeText(SignupActivity.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setUwStudentId(uwStudentId);
            newUser.setEmail(email);

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent loginIntent = new Intent(SignupActivity.this, MainActivity.class);
                SignupActivity.this.startActivity(loginIntent);

                }
            });
            }
        });
    }

}
