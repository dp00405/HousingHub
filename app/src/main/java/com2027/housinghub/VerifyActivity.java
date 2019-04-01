package com2027.housinghub;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class VerifyActivity extends AppCompatActivity {

    public static final String DIALOG_LANDLORD = "";
    protected Integer REQUEST_CAMERA = 1;
    protected Integer SELECT_FILE = 0;
    protected ImageView camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        //Sets background imageview to the background image within the drawable folder
        ImageView background = (ImageView) findViewById(R.id.imageView5);
        background.setImageResource(R.drawable.background);

        //On press the camera imageview will execute the code contained within the onClick function.
        camera = (ImageView) findViewById(R.id.imageView7);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opens camera and allows the user to take photos.
                //Opens camera and allows the user to take photos.
                final CharSequence[] selection = {"Camera", "Gallery"};

                //Creates a dialog box to allow the user to either take or upload a photo
                AlertDialog.Builder builder = new AlertDialog.Builder(VerifyActivity.this);
                builder.setTitle("Select option:")
                        .setItems(selection, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(selection[which].equals("Camera")) {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(intent, REQUEST_CAMERA);
                                    }
                                } else if (selection[which].equals("Gallery")) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                                }
                            }
                        });
                builder.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Closes the dialog
                    }
                });
                builder.create().show();

            }
        });

        //On press the verify button will execute the code contained within the onClick function.
        Button verify = (Button) findViewById(R.id.button7);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates an intent and starts a new activity, returns a boolean value to inform
                //the login activity to display the dialog box associated with this activity.
                Intent verifyact = new Intent(VerifyActivity.this, LoginActivity.class);
                verifyact.putExtra(DIALOG_LANDLORD, true);
                startActivity(verifyact);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                Bitmap image = (Bitmap) bundle.get("data");
                camera.setImageBitmap(image);
            } else if (requestCode == SELECT_FILE) {
                try {
                    Uri selectImage = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(selectImage);
                    camera.setImageURI(selectImage);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

}
