package com.example.boatengfranklaud.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    // UI widgets
    private EditText email, username, password, confirmPassword;
    private Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        // initialize UI components
        email = findViewById(R.id.edit_signup_email);
        username = findViewById(R.id.edit_signup_username);
        password = findViewById(R.id.edit_signup_password);
        confirmPassword = findViewById(R.id.edit_signup_confirm_password);

        btnSignUp = findViewById(R.id.signup_btnSignUp);
        btnLogin = findViewById(R.id.signup_btnLogin);


        // set click event for the buttons
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        // set key event listener for the enter/return key on confirm password widget
        confirmPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                  onClick(btnSignUp);
                }
                return false;
            }
        });

        // check if user has already sign in
        if (ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOutInBackground();
            //ParseUser.logOut();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_btnSignUp:
                signUp();
                break;
            case R.id.signup_btnLogin:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }

    private void signUp() {
        if (email.getText().toString().length() <= 0 || username.getText().toString().length() <= 0) {
            FancyToast.makeText(this, "All fields are required",
                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
        } else if (!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
            FancyToast.makeText(this, "Password mismatch! Please check.",
                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
        } else {

            final ParseUser appUser = new ParseUser();
            appUser.setEmail(email.getText().toString().trim());
            appUser.setUsername(username.getText().toString().trim());
            appUser.setPassword(password.getText().toString().trim());

            // show progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing up " + username.getText().toString());
            progressDialog.show();

            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        clearWidgets();
                        progressDialog.dismiss(); // dismiss progress dialog
                        FancyToast.makeText(SignUp.this, appUser.getUsername() + " is signed successfully.",
                                FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(SignUp.this, e.getLocalizedMessage(),
                                FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                    }
                }
            });
        }
    }

    private void clearWidgets() {
        email.setText("");
        username.setText("");
        password.setText("");
        confirmPassword.setText("");

    }

    // onClick method for the root Layout
    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
