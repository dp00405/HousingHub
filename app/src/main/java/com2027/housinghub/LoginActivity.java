package com2027.housinghub;

import com2027.housinghub.Home.HomeActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com2027.housinghub.Home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText editTextEmail;
    EditText editTextPassword;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);

        //Retrieves data from other activities which return to this activity when a pressable
        //item is activated. In most cases a button.
        Intent intent = getIntent();
        if (intent.getBooleanExtra(StudentActivity.DIALOG_STUDENT, false) || intent.getBooleanExtra(VerifyActivity.DIALOG_LANDLORD, false)) {
            openDialog(0);
        }

        //Sets the background to the image by finding the imageView through its id, the image chosen
        //is in the drawable folder name background
        ImageView background = findViewById(R.id.imLoginBackground);
        background.setImageResource(R.drawable.background);

        //On press the login button will execute the code contained within the onClick function.
        Button login =  findViewById(R.id.btLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        editTextEmail = findViewById(R.id.etLoginEmailAddress);
        editTextPassword = findViewById(R.id.etLoginPassword);

        //On press the account textview will execute the code contained within the onClick function.
        TextView NoAccount = findViewById(R.id.tvNoAccount);
        NoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Creates an intent and starts a new activity
                Intent accountact = new Intent(LoginActivity.this, AccountActivity.class);
                startActivity(accountact);
            }
        });

    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
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

        // Validations OK..
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.hide();
                if (task.isSuccessful()) {
                    finish();
                    // successful log In
                    // The User will be directed to their profile page here
                    // in the intent remember to make a intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    Intent signIn = new Intent(LoginActivity.this, HomeActivity.class);
                    signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signIn);

                    Toast.makeText(getApplicationContext(), "User is Logged In", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Function creates a dialog box, the contents of the box is determined by the inputted
     * parameter
     * @param type
     *          an integer which determins the contents of the dialog box
     */
    protected void openDialog(int type) {
        //Dialog box will be displayed on this activity
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        if (type == 0) {
            //Sets the title, message and button of the dialog box
            builder.setTitle("Verification Email Sent!")
                    .setMessage("Please check your email to verify your account.")
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Closes the dialog
                        }
                    });
        } else if (type == 1) {
            //Sets the title, message and button of the dialog box
            builder.setTitle("Recovery Email Sent!")
                    .setMessage("Please check your email to recover your account.")
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Closes the dialog
                        }
                    });
        } else {
            //Sets the title, message and button of the dialog box
            builder.setTitle("An error occurred!")
                    .setMessage("Something happend, our top engineers will fix it soon. Thank you for your patience")
                    .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Closes the dialog
                        }
                    });
        }
        //Shows the dialog box
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If a user is already logged in, go straight to the home page
        if(mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
