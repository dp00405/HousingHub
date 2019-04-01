package com2027.housinghub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com2027.housinghub.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Sets background imageview to the background image within the drawable folder
        ImageView background = (ImageView) findViewById(R.id.imageView2);
        background.setImageResource(R.drawable.background);

        //On press the student button will execute the code contained within the onClick function.
        Button student = (Button) findViewById(R.id.button2);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates an intent and starts a new activity
                Intent studentact = new Intent(AccountActivity.this, StudentActivity.class);
                startActivity(studentact);
            }
        });

        //On press the student button will execute the code contained within the onClick function.
        Button landlord = (Button) findViewById(R.id.button3);
        landlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates an intent and starts a new activity
                Intent landlordact = new Intent(AccountActivity.this, LandlordActivity.class);
                startActivity(landlordact);
            }
        });

    }
}
