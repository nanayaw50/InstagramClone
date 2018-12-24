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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // UI widgets
    private EditText username, password;
    private Button btnLogin, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        // initialize UI components
        username = findViewById(R.id.edit_login_username);
        password = findViewById(R.id.edit_login_password);

        btnLogin = findViewById(R.id.login_btnLogin);
        btnSignUp = findViewById(R.id.login_btnSignUp);

        // set click listener for the buttons
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        // set key event listener on password when the enter/return key is pressed
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser() != null){
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_btnLogin:
                loginUser();
                break;
            case R.id.login_btnSignUp:
                Intent signUpIntent = new Intent(this, SignUp.class);
                startActivity(signUpIntent);
                break;
        }

    }

    private void loginUser() {
        if(username.getText().toString().length() <= 0 || password.getText().toString().length() <= 0){
            FancyToast.makeText(this, "All fields are required",
                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
        }else {

            // show progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Logging in " + username.getText().toString());
            progressDialog.show();

            ParseUser.logInInBackground(username.getText().toString().trim(),
                    password.getText().toString().trim(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user != null && e == null){
                                progressDialog.dismiss(); // dismiss progress dialog
                                clearWidgets();
                                FancyToast.makeText(LoginActivity.this, user.getUsername() + " logged in successfully.",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                            }else {
                                FancyToast.makeText(LoginActivity.this, e.getLocalizedMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                            }
                        }
                    });
        }
    }

    private void clearWidgets(){
        username.setText("");
        password.setText("");
    }

    // check if the root layout tapped and dismiss the soft keyboard
    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
