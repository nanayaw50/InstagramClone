package com.example.boatengfranklaud.instagramclone;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
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

        // set click listener
        usersList.setOnItemClickListener(UsersTab.this);
        usersList.setOnItemLongClickListener(UsersTab.this);

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
                if (e == null && users.size() > 0) {

                    for (ParseUser user : users) {
                        arrayList.add(user.getUsername());
                    }

                    // set adapter for the listView
                    usersList.setAdapter(arrayAdapter);
                    usersList.setVisibility(View.VISIBLE);
                }
                progressDialog.dismiss();
            }
        });
        // return statement
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), UserPostActivity.class);
        intent.putExtra("username", arrayList.get(position).toString());
        startActivity(intent);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));

        parseQuery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) {

                    final PrettyDialog prettyDialog = new PrettyDialog(getContext());
                    prettyDialog.setTitle(user.getUsername() + "'s Info")
                            .setMessage(user.getString("username") + "\n"
                                    + user.getString("profileProfession") + "\n"
                                    + user.getString("profileFavSport"))
                            .setIcon(R.drawable.person)
                            .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_blue, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss(); // dismiss the dialog
                                }
                            }).show();


                }
            }
        });
        // return value
        return false;
    }
}
