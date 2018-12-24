package com.example.boatengfranklaud.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment {
    private ListView usersList;
    private ArrayList arrayList;
    private ArrayAdapter arrayAdapter;


    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);

        // init listView
        usersList = view.findViewById(R.id.users_listView);

        // init arrayList
        arrayList = new ArrayList<>();

        // init array adapter and set constructor parameters
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);

        // Parse query for users
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        // show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading users, please wait...");
        progressDialog.show();

        // exclude the current user from the list the user is using the app
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null && users.size() > 0){

                    for(ParseUser user : users){
                        arrayList.add(user.getUsername());
                    }

                    // set adapter for the listView
                    usersList.setAdapter(arrayAdapter);
                    progressDialog.dismiss();
                    usersList.setVisibility(View.VISIBLE);
                }
            }
        });
        // return statement
        return view;
    }

}
