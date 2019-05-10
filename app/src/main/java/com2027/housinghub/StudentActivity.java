package com2027.housinghub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.InputStream;

public class StudentActivity extends AppCompatActivity {

    public static final String DIALOG_STUDENT = "";
    protected Integer REQUEST_CAMERA = 1;
    protected Integer SELECT_FILE = 0;
    protected ImageView camera;

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private EditText editTextFirstName;
    private EditText editTextSurname;


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
        setContentView(R.layout.activity_student);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.btVerifyYourAccountStudent);


        editTextEmail = (EditText) findViewById(R.id.etStudentEmail);
        editTextPassword = (EditText) findViewById(R.id.etStudentPassword);
        editTextFirstName = findViewById(R.id.etStudentAccountFirstName);
        editTextSurname = findViewById(R.id.etStudentAccountSurname);


        //Sets background imageview to the background image within the drawable folder
        ImageView background = findViewById(R.id.imStudentActivityBackground);
        background.setImageResource(R.drawable.background);

        //On press the camera imageview will execute the code contained within the onClick function.
        camera =  findViewById(R.id.imStudentActivityCamera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opens camera and allows the user to take photos.
                final CharSequence[] selection = {"Camera", "Gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
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
        Button verify =  findViewById(R.id.btVerifyYourAccountStudent);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                final String firstName = editTextFirstName.getText().toString().trim();
                final String surname = editTextSurname.getText().toString().trim();
                final String userType = "Student";

                if(TextUtils.isEmpty(email)) {
                    //email is empty
                    editTextEmail.setError("Please Enter your email");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please Enter a valid email");
                    editTextEmail.requestFocus();
                    return;
                }
                if(password.length() <6) {
                    editTextPassword.setError("Minimum length of password is 6");
                    editTextPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    //password is empty
                    editTextPassword.setError("Please Enter Your Password");
                    editTextPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(firstName)) {
                    //first name is empty
                    editTextFirstName.setError("Please Enter Your First Name");
                    editTextFirstName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(surname)) {
                    //surname is empty
                    editTextSurname.setError("Please Enter Your Surname");
                    editTextSurname.requestFocus();
                    return;
                }
                // if validations are ok

                progressDialog.setMessage("Registering Student...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(StudentActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.hide();
                                if (task.isSuccessful()) {
                                    // user is successfully registered
                                    User user = new User(
                                            firstName,
                                            surname,
                                            email,
                                            userType
                                    );

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                finish();
                                                Intent registerUser = new Intent(StudentActivity.this, HomeActivity.class);
                                                registerUser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(registerUser);
                                                Toast.makeText(StudentActivity.this, "Student Is Now Registered", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(StudentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    // failed to register
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(StudentActivity.this, "This email already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(StudentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
