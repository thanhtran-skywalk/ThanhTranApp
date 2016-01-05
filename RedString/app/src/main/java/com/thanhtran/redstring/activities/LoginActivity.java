package com.thanhtran.redstring.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.thanhtran.redstring.R;

/**
 * Created by admin on 10/19/15.
 */
public class LoginActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }


    public void loginAction(View view){
        showProgressDialog("Logging In", "One moment please...");
        String userName = ((EditText) findViewById(R.id.txt_login_email_field)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.txt_login_password_field)).getText().toString().trim();

        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    startMainActivity();
                } else {
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this, "Error logging in, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void startMainActivity() {
        Intent messagingActivity = new Intent(this, MainActivity.class);
        startActivity(messagingActivity);
        finish();
    }

    public void startSignUpActivity(View view) {
        Intent signUpActivity = new Intent(this, SignUpActivity.class);
        startActivity(signUpActivity);
    }

    private void showProgressDialog(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.hide();
    }

}
