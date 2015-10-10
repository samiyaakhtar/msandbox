package com.uwmsa.msandbox.Activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

    EditText vUsernameEditText;
    EditText vPasswordEditText;
    Button vLoginButton;
    Button vSignupButton;
    TextView vForgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vUsernameEditText = (EditText)findViewById(R.id.login_usernameTextField);
        vPasswordEditText = (EditText)findViewById(R.id.login_passwordTextField);
        vLoginButton = (Button) findViewById(R.id.login_loginButton);
        vSignupButton = (Button) findViewById(R.id.login_signUpButton);
        vForgotPasswordButton = (TextView) findViewById(R.id.login_forgotPasswordButton);

        vPasswordEditText.setTypeface(Typeface.DEFAULT);
        vPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());

        setupButtonListeners();

//        ActionBar bar = getSupportActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4B9D8F")));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected void setupButtonListeners() {
        vLoginButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Logging In");
                progressDialog.show();

                ParseUser.logInInBackground(vUsernameEditText.getText().toString(), vPasswordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        progressDialog.dismiss();

                        if(parseUser != null) {
                            // Navigate to home screen of app
                            Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.startActivity(newIntent);
                        }
                        else {
                            String errorMessage;
                            if (e.getMessage().equals("i/o failure: java.net.UnknownHostException: Unable to resolve host \"api.parse.com\": No address associated with hostname")) {
                                errorMessage = "No network connection";
                            } else {
                                errorMessage = e.getMessage().substring(0, 1).toUpperCase() + e.getMessage().substring(1);
                            }
                            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        vSignupButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(loginIntent);
            }
        });

        vForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                LoginActivity.this.startActivity(loginIntent);
            }
        });
    }
}
