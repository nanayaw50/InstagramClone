package com.example.boatengfranklaud.instagramclone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpLoginActivity extends AppCompatActivity {

    private Button btnLogin, btnSignUp;
    private EditText edtUsernameSignUp, edtPwdSignUp, edtUsernameLogin, edtPwdLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);

        // initialize UI components
        edtUsernameSignUp = findViewById(R.id.edtUsernameSignup);
        edtPwdSignUp = findViewById(R.id.edtPwdSignup);
        edtUsernameLogin = findViewById(R.id.edtLoginUsername);
        edtPwdLogin = findViewById(R.id.edtLoginPwd);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ParseUser parseUser = new ParseUser();
                parseUser.setUsername(edtUsernameSignUp.getText().toString().trim());
                parseUser.setPassword(edtPwdSignUp.getText().toString().trim());

                // use the callback sign up anonymous class to get feedback
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(SignUpLoginActivity.this, parseUser.get("username") + " is signed up successfully.", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        } else {
                            FancyToast.makeText(SignUpLoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                        }
                    }
                });

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(edtUsernameLogin.getText().toString().trim(),
                        edtPwdLogin.getText().toString().trim(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(user != null && e == null){
                                    FancyToast.makeText(SignUpLoginActivity.this, user.get("username") + " is logged in successfully.", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                }else {
                                    FancyToast.makeText(SignUpLoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                }
                            }
                        });
            }
        });
    }
}
