package com.example.boatengfranklaud.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UserPostActivity extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);

        linearLayout = findViewById(R.id.linearLayout);


        Intent receivedIntentObject = getIntent();
        final String receivedUsername = receivedIntentObject.getStringExtra("username");
        setTitle(receivedUsername + "'s posts");

        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username", receivedUsername);
        parseQuery.orderByDescending("createdAt");

        // show progress dialog
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> posts, ParseException e) {
                if (posts.size() > 0 && e == null) {
//                    FancyToast.makeText(UserPostActivity.this,"loading done!", FancyToast.LENGTH_LONG,
//                            FancyToast.INFO, false).show();
                    for (ParseObject post : posts) {
                        final TextView imgDes = new TextView(UserPostActivity.this);
                        imgDes.setText(post.getString("image_desc"));
                        ParseFile postImage = (ParseFile) post.get("picture");
                        postImage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(UserPostActivity.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(5, 15, 5, 5);
                                    postImageView.setLayoutParams(params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams desc_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    desc_params.setMargins(5, 5, 5, 15);
                                    imgDes.setLayoutParams(desc_params);
                                    imgDes.setGravity(Gravity.CENTER);
                                    imgDes.setTextSize(30f);

                                    // add views to the linear layout in the scroll view (View)
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(imgDes);
                                }
                            }
                        });
                    }
                } else {
                    FancyToast.makeText(UserPostActivity.this, receivedUsername + " does not have any post.", FancyToast.LENGTH_LONG,
                            FancyToast.INFO, false).show();
                    finish();
                }
                dialog.dismiss();
            }
        });
    }
}
