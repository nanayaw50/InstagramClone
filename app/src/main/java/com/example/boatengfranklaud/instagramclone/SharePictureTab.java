package com.example.boatengfranklaud.instagramclone;


import android.Manifest.permission;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePictureTab extends Fragment implements View.OnClickListener {

    // UI component
    private ImageView imgShare;
    private EditText txtImgDescription;
    private Button btnShareImg;
    private Bitmap receivedBitmapImage;

    public SharePictureTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_picture_tab, container, false);

        // init
        imgShare = view.findViewById(R.id.imgShare);
        txtImgDescription = view.findViewById(R.id.edt_img_description);
        btnShareImg = view.findViewById(R.id.btn_share_picture);

        imgShare.setOnClickListener(SharePictureTab.this); // an instance of the tab
        btnShareImg.setOnClickListener(SharePictureTab.this); // an instance of the tab

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgShare:
                // already set permission in the manifest xml to read external storage
                // but its a dangerous permission and we need to explicitly ask the
                // user to grant the app permission for reading external storage (SD-card, Rom etc)
                if ((Build.VERSION.SDK_INT >= 23) && (ActivityCompat.checkSelfPermission(getContext(),
                        permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[]{permission.READ_EXTERNAL_STORAGE}, 1000);
                } else {
                    getSelectImg(); // call and execute user-defined method to select image from the user device
                }
                break;

            case R.id.btn_share_picture:
                if (receivedBitmapImage != null) {
                    if (txtImgDescription.getText().toString().equals("")) {
                        FancyToast.makeText(getContext(), "Image description is required!", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    } else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedBitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();

                        // Parse objects
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_desc", txtImgDescription.getText().toString().trim());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());

                        // show progress dialog
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("uploading image to server...");
                        progressDialog.show();

                        // save in background thread
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(getContext(), "Done!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                } else {
                                    FancyToast.makeText(getContext(), "Unknown error!", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                } else {
                    FancyToast.makeText(getContext(), "Please select image first.", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }
                break;
        }
    }

    // user define method to select img
    private void getSelectImg() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }

    // this method gives us the select img
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {

                // do something with your captured image
                try {
                    Uri selectedImg = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    receivedBitmapImage = BitmapFactory.decodeFile(picturePath);
                    imgShare.setImageBitmap(receivedBitmapImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // check for the result from the request (permission)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSelectImg();
            }
        }
    }
}
