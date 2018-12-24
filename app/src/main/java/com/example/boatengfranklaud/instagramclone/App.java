package com.example.boatengfranklaud.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("UVeLo4k506fpOMahvjJSnpH6vIuHUND35D29v0Ko")
                // if defined
                .clientKey("P0FWi7ux9jaVCNKXD0k8Ec0RjfEyHlLBSadHRRd4")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
