package com.example.boatengfranklaud.instagramclone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class WelcomeActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView txtWelcome = findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome back! " + ParseUser.getCurrentUser().getString("username"));

        Button btnLogout = findViewById(R.id.btnLogOut);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(WelcomeActivity.this, "You logged out successfully.", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                            startActivity(new Intent(WelcomeActivity.this, SignUpLoginActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
    }
}
