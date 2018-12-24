package com.example.boatengfranklaud.instagramclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class SignUp extends AppCompatActivity {

    private EditText name, kspeed, kpower, pspeed, ppower;
    private TextView txtGetData;
    private Button btnGetAllData;
    private String allKickBoxers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.editText_name);
        kspeed = findViewById(R.id.editText_kickspeed);
        kpower = findViewById(R.id.editText_kickPower);
        pspeed = findViewById(R.id.editText_punchSpeed);
        ppower = findViewById(R.id.editText_punchPower);

        txtGetData = findViewById(R.id.txtGetData);

        btnGetAllData = findViewById(R.id.btnGetAllData);

        txtGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("KrofrsV9Cp", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        if (object != null && e == null) {

                            txtGetData.setText(object.getString("name") + " " + object.getInt("kickSpeed"));
                        }
                    }
                });
            }
        });

        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allKickBoxers = "";

                ParseQuery<ParseObject> allquery = ParseQuery.getQuery("KickBoxer");

                allquery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects.size() > 0 && e == null) {

                            for(ParseObject names : objects){

                                allKickBoxers += "name: " + names.getString("name") + " kick speed: "+ names.getInt("kickSpeed") + " punch power: " + names.getInt("punchPower") + "\n";
                            }

                            txtGetData.setText(allKickBoxers);

                            FancyToast.makeText(SignUp.this, allKickBoxers,
                                    FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        }
                    }
                });
            }
        });

    }

    public void helloTap(View v) {

//        ParseObject boxer = new ParseObject("Boxer");
//        boxer.put("punch_speed", 200);
//        boxer.saveEventually(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null){
//                    Toast.makeText(SignUp.this, "the boxer object is saved succesfully", Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(SignUp.this, "Oops! Something went wrong: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        final ParseObject kickBoxer = new ParseObject("KickBoxer");
        kickBoxer.put("name", name.getText().toString());
        kickBoxer.put("kickPower", Integer.parseInt(kpower.getText().toString()));
        kickBoxer.put("kickSpeed", Integer.parseInt(kspeed.getText().toString()));
        kickBoxer.put("punchPower", Integer.parseInt(ppower.getText().toString()));
        kickBoxer.put("punchSpeed", Integer.parseInt(pspeed.getText().toString()));

        kickBoxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    FancyToast.makeText(SignUp.this, kickBoxer.get("name") + " details saved successfully.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                } else {

                    Toast.makeText(SignUp.this, "Oops! Something went wrong: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
