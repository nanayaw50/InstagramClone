package com.example.boatengfranklaud.instagramclone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        setTitle("Social Media App");


        // initialize components
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postImageItem:
                // request user permission explicitly during the use of the app.
                if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 300);
                }else {
                    captureImage(); // a user defined method
                }
                break;
            case R.id.logOutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(SocialMediaActivity.this, "You logged out successfully.",
                                    FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                            startActivity(new Intent(SocialMediaActivity.this, LoginActivity.class));
                            finish();
                        }else {
                            FancyToast.makeText(SocialMediaActivity.this, e.getLocalizedMessage(),
                                    FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 300){ // using the request code that was supply during request permission above
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage(); // execute this method once we have permission
            }
        }
    }

    // user define method to read image/files from users device
    private void captureImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4000 && resultCode == RESULT_OK && data != null){
            try {
                Uri capturedImg = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), capturedImg);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                // Parse code to upload the image
                ParseFile parseFile = new ParseFile("img.png", bytes);
                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture", parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                // show progress dialog
               final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading image to the server...");
                progressDialog.show();

                // check the status the process
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(SocialMediaActivity.this,"Picture uploading done!",FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        }else {
                            FancyToast.makeText(SocialMediaActivity.this, e.getLocalizedMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR, false).show();
                        }
                        progressDialog.dismiss(); // dismiss progress dialog
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
