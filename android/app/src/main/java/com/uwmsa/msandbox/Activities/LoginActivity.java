package com.uwmsa.msandbox.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.uwmsa.msandbox.R;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupButtonListeners();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        Button loginButton = (Button) findViewById(R.id.login_loginButton);
        loginButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText usernameTextField = (EditText)findViewById(R.id.login_usernameTextField);
                EditText passwordTextField = (EditText)findViewById(R.id.login_passwordTextField);

                ParseUser.logInInBackground(usernameTextField.getText().toString(), passwordTextField.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null) {
                            // Navigate to home screen of app
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        Button signUpButton = (Button) findViewById(R.id.login_signUpButton);
        signUpButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(loginIntent);
            }
        });

        TextView forgotPasswordButton = (TextView) findViewById(R.id.login_forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                LoginActivity.this.startActivity(loginIntent);
            }
        });
    }
}
