package com.example.boatengfranklaud.instagramclone;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText name, bio, profession, hobbies, favSports;
    private Button btnSaveInfo;
    private ParseUser appUser = null;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        // initialize UI component on the xml file (fragment_profile_tab)
        name = view.findViewById(R.id.profile_name);
        bio = view.findViewById(R.id.profile_bio);
        profession = view.findViewById(R.id.profile_profession);
        hobbies = view.findViewById(R.id.profile_hobbies);
        favSports = view.findViewById(R.id.profile_sports);

        btnSaveInfo = view.findViewById(R.id.profile_btnSave);

        // get the current app user
        appUser = ParseUser.getCurrentUser();

        // load the profile of current user
        hasProfileDetails();

        // TODO: more improvement can be made to include image and other functionality.

        // save details to the parse server
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appUser.put("ProfileName", name.getText().toString().trim());
                appUser.put("ProfileBio", bio.getText().toString().trim());
                appUser.put("ProfileProfession", profession.getText().toString().trim());
                appUser.put("ProfileHobbies", hobbies.getText().toString().trim());
                appUser.put("ProfileFavSports", favSports.getText().toString().trim());

                // show progress dialog
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Updating profile, please wait...");
                progressDialog.show();

                appUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            progressDialog.dismiss(); // dismiss progress dialog
                            clearWidgets(); // clear old values
                            hasProfileDetails(); // load the updated values
                            FancyToast.makeText(getContext(), appUser.getUsername() + " is signed successfully.",
                                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                        } else {
                            FancyToast.makeText(getContext(), e.getLocalizedMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                        }
                    }
                });
            }
        });

        return view;
    }

    private void clearWidgets() {
        name.setText("");
        bio.setText("");
        profession.setText("");
        hobbies.setText("");
        favSports.setText("");
    }

    private void hasProfileDetails() {
        // load  profile of the current user and
        // check for null object from the server
        if (appUser.getString("ProfileName") != null) {
            name.setText(appUser.getString("ProfileName"));
        } else {
            name.setText("");
        }

        if (appUser.getString("ProfileBio") != null) {
            bio.setText(appUser.getString("ProfileBio"));
        } else {
            bio.setText("");

        }
        if (appUser.getString("ProfileProfession") != null) {
            profession.setText(appUser.getString("ProfileProfession"));
        } else {
            profession.setText("");
        }
        if (appUser.getString("ProfileHobbies") != null) {
            hobbies.setText(appUser.getString("ProfileHobbies"));
        } else {
            hobbies.setText("");
        }
        if (appUser.getString("ProfileFavSports") != null) {
            favSports.setText(appUser.getString("ProfileFavSports"));
        } else {
            favSports.setText("");
        }
    }
}
