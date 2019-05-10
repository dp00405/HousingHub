package com2027.housinghub.Home;
/**
 * The Main Activity which holds the fragments and sidebar navigation
 */

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar ;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import com2027.housinghub.Account.AccountFragment;
import com2027.housinghub.Favourites.FavouriteFragment;
import com2027.housinghub.Group.GroupFragment;
import com2027.housinghub.LoginActivity;
import com2027.housinghub.Models.User;
import com2027.housinghub.Account.EditLandlordProfileFragment;
import com2027.housinghub.Account.LandlordProfileFragment;

import com2027.housinghub.R;
import com2027.housinghub.Settings.SettingsFragment;

//Imports NavigationView
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    private Context mContext = HomeActivity.this;

    // Firebase Declarations
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;


    // User's information
    private String firstName;
    private String surname;
    private String email;

    //The side layout
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"onCreate: starting");

        // the Firebase database reference is declared. This reference is used to access the database
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        //calls the method that sets up the navigation side view
        setUpNavigationView(savedInstanceState);


    }

    @Override
    protected void onStart() {
        super.onStart();

        // If a user is already not logged in, they are taken to the LogIn page
        if(mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }




    /**
     *This sets up the navigation view
     * @param savedInstanceState
     */
    private void setUpNavigationView(Bundle savedInstanceState) {
        Log.d(TAG, "setupNavigationView: setting up NavigationView");

        Toolbar toolbar = findViewById(R.id.widget_toolbar);
        setSupportActionBar(toolbar);

        loadUserInformation();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar, R.string.navigationn_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        View headerView = navigationView.getHeaderView(0);
        TextView navEmail = (TextView) headerView.findViewById(R.id.navEmail);
        navEmail.setText(firstName);

        //if there are saved instance states the app doesnt reset on rotation.
        if(savedInstanceState == null)
        { getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    private void loadUserInformation() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            User userInfo = new User();

            userInfo.setFirstName(ds.child(userID).getValue(User.class).getFirstName());
            userInfo.setSurname(ds.child(userID).getValue(User.class).getSurname());
//
            firstName = userInfo.getFirstName();
            surname = userInfo.getSurname();
            email = mAuth.getCurrentUser().getEmail();
        }
    }

    /**
     * When a navigation item is selected, this method chooses the appropriate
     * Fragment to open
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //READ =============================================================================== READ
        //Cases within this would have to be different depending on which user type is logged in
        //In the case of having a student the below cases are fine, but a landlord would have no
        //need for groups and favourites and might rather want to see his own listings instead
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;

            case R.id.nav_group:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GroupFragment()).commit();
                break;

            case R.id.nav_favourite:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavouriteFragment()).commit();
                break;

            //This will have to change for user and landlord,  (so make two account fragments) if user is student
            //show this, if not, show this.
            case R.id.nav_account:
                //READ ============================================================= READ
                //This should show the edit version of their account, unless an edit button is added
                //The uneditable version will be shown when a different account views their profile
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LandlordProfileFragment()).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                //        new StudentProfileFragment()).commit();
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;

            case R.id.nav_signout_:
                Toast.makeText(this,"Sign out", Toast.LENGTH_SHORT).show();

                mAuth.signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;

            case R.id.help:
                Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     *is called to close the app when back is pressed
     */
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    /**
     *
     */
    @Override
    public void onStop() {
        super.onStop();
        //...
    }


}
