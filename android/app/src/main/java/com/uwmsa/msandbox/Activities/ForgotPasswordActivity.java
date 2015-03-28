package com.uwmsa.msandbox.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.parse.RequestPasswordResetCallback;
import com.uwmsa.msandbox.Models.User;
import com.uwmsa.msandbox.R;

public class ForgotPasswordActivity extends ActionBarActivity {

    EditText vEmailEditText;
    Button vSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        vEmailEditText = (EditText)findViewById(R.id.forgotPassword_emailTextField);
        vSubmitButton = (Button) findViewById(R.id.forgotPassword_submitButton);

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
        vSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                String email = vEmailEditText.getText().toString();

                User.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();

                        if (e != null) {
                            Toast.makeText(ForgotPasswordActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setMessage("A password reset link was sent to the email address you entered.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent loginIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                        ForgotPasswordActivity.this.startActivity(loginIntent);
                                    }
                                })
                                .show();


                    }
                });

            }
        });
    }
}
