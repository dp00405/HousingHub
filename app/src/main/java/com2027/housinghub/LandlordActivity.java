package com2027.housinghub;

import android.app.ProgressDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;


import com2027.housinghub.Home.HomeActivity;
import com2027.housinghub.Models.User;

import java.io.IOException;
import java.io.InputStream;


public class LandlordActivity extends AppCompatActivity {

    protected Integer REQUEST_CAMERA = 1;
    protected Integer SELECT_FILE = 0;
    protected ImageView camera;

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private EditText editTextFirstName;
    private EditText editTextSurname;
    private EditText editTextPhoneNumber;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onStart() {
        super.onStart();

        // If a user is already logged in, go straight to the home page
        if(firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord);

        // Initializes the Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize the progress Dialog
        progressDialog = new ProgressDialog(this);

        // set all the text boxes and buttons on the page
        buttonRegister = (Button) findViewById(R.id.btLandlordVerifyAccount);
        editTextEmail = (EditText) findViewById(R.id.etEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.etPassword);
        editTextFirstName = findViewById(R.id.etFirstname);
        editTextSurname = findViewById(R.id.etSurname);
        editTextPhoneNumber = findViewById(R.id.etPhoneNumber);


        //Sets background imageview to the background image within the drawable folder
        ImageView background = findViewById(R.id.imBackgroundLandlordActivity);
        background.setImageResource(R.drawable.background);

        //On press the camera image view will execute the code contained within the onClick function.
        ImageView camera = findViewById(R.id.imLandlordPictureCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opens camera and allows the user to take photos.
                final CharSequence[] selection = {"Camera", "Gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(LandlordActivity.this);
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


        // When the Register Button is selected
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make all the text boxes Strings so they can be added to the database
                final String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                final String firstName = editTextFirstName.getText().toString().trim();
                final String surname = editTextSurname.getText().toString().trim();
                final String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                final String userType = "Landlord";

                // checks if the email text box is empty
                if(TextUtils.isEmpty(email)) {
                    //email is empty
                    editTextEmail.setError("Please Enter your email");
                    editTextEmail.requestFocus();
                    return;
                }
                // checks if the entered email is the right email format
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please Enter a valid email");
                    editTextEmail.requestFocus();
                    return;
                }
                // checks if the password is longer than 6 characters
                if(password.length() <6) {
                    editTextPassword.setError("Minimum length of password is 6");
                    editTextPassword.requestFocus();
                    return;
                }
                // checks if the password text box is empty
                if (TextUtils.isEmpty(password)) {
                    //password is empty
                    editTextPassword.setError("Please Enter Your Password");
                    editTextPassword.requestFocus();
                    return;
                }
                // checks if the first name box is empty
                if (TextUtils.isEmpty(firstName)) {
                    //password is empty
                    editTextFirstName.setError("Please Enter Your First Name");
                    editTextFirstName.requestFocus();
                    return;
                }
                // checks if the surname box is empty
                if (TextUtils.isEmpty(surname)) {
                    //password is empty
                    editTextSurname.setError("Please Enter Your Surname");
                    editTextSurname.requestFocus();
                    return;
                }
                // checks if the phone number box is empty
                if (TextUtils.isEmpty(phoneNumber)) {
                    //password is empty
                    editTextPhoneNumber.setError("Please Enter Your Phone Number");
                    editTextPhoneNumber.requestFocus();
                    return;
                }

                // if validations are ok then the User is registered

                // creates a dialog box while the user is being registered
                progressDialog.setMessage("Registering Landlord...");
                progressDialog.show();

                // Here the user is created with the firebase method
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LandlordActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.hide();
                                if (task.isSuccessful()) {
                                    // user object is created
                                    User user = new User(
                                            firstName,
                                            surname,
                                            email,
                                            userType
                                    );
                                    user.setPhone(phoneNumber);

                                    // The user is added to the User class in storage
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())        // The userID is the primary key
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                finish();
                                                Intent registerUser = new Intent(LandlordActivity.this, HomeActivity.class);
                                                registerUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(registerUser);
                                                Toast.makeText(LandlordActivity.this, "Landlord Is Now Registered", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LandlordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    // failed to register, shows the error message
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(LandlordActivity.this, "This email already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(LandlordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                Bitmap bmp = (Bitmap) bundle.get("data");
                camera.setImageBitmap(bmp);
            } else if (requestCode == SELECT_FILE) {
                try {
                    Uri selectImage = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(selectImage);
                    camera.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

}
