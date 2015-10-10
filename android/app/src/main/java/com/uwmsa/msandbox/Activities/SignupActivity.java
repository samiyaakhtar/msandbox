package com.uwmsa.msandbox.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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

    EditText vUsernameEditText;
    EditText vPasswordEditText;
    EditText vConfirmPasswordEditText;
    EditText vStudentIdEditText;
    EditText vEmailEditText;
    Button vSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        vUsernameEditText = (EditText)findViewById(R.id.signup_usernameTextField);
        vPasswordEditText = (EditText)findViewById(R.id.signup_passwordTextField);
        vConfirmPasswordEditText = (EditText)findViewById(R.id.signup_confirmPasswordTextField);
        vStudentIdEditText = (EditText)findViewById(R.id.signup_uwStudentIdTextField);
        vEmailEditText = (EditText)findViewById(R.id.signup_emailAddressTextField);
        vSignupButton = (Button) findViewById(R.id.signup_signupButton);

        vPasswordEditText.setTypeface(Typeface.DEFAULT);
        vPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
        vConfirmPasswordEditText.setTypeface(Typeface.DEFAULT);
        vConfirmPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());

        setupButtonListeners();
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
        vSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = vPasswordEditText.getText().toString().trim();
                String confirmPassword = vConfirmPasswordEditText.getText().toString().trim();

                if(!confirmPassword.equals(password)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                } else if(password.equals("")) {
                    Toast.makeText(SignupActivity.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }

                final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                progressDialog.setMessage("Signing Up");
                progressDialog.show();

                User newUser = new User();
                newUser.setUsername(vUsernameEditText.getText().toString().trim());
                newUser.setPassword(password);
                newUser.setUwStudentId(vStudentIdEditText.getText().toString().trim());
                newUser.setEmail(vEmailEditText.getText().toString().trim());

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();

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
