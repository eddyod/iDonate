package com.mephistosoftware.firebaseandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mephistosoftware.firebaseandroid.donation.MyDonationActivity;
import com.mephistosoftware.firebaseandroid.donation.NewPostActivity;
import com.mephistosoftware.firebaseandroid.model.MapModel;
import com.mephistosoftware.firebaseandroid.util.Constants;
import com.mephistosoftware.firebaseandroid.util.GPSTracker;
import com.mephistosoftware.firebaseandroid.util.Util;
import com.mephistosoftware.firebaseandroid.view.BaseActivity;
import com.mephistosoftware.firebaseandroid.view.MyChatListActivity;
import com.mephistosoftware.firebaseandroid.view.PublicActivity;
import com.mephistosoftware.firebaseandroid.view.SearchableActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "XXX";

    private Button mSearchableButton;
    private Button mPublicButton;
    private Button mCreateDonationButton;
    private Button mMyDonationButton;
    private Button mMyChatRoomButton;
    private Button mSignoutButton;
    private AdView mAdView;
    // location stuff
    private Location location;
    GPSTracker gps;
    // auth stuff
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearchableButton = (Button) findViewById(R.id.searchable_donations);
        mPublicButton = (Button)findViewById(R.id.public_donations);
        mCreateDonationButton = (Button) findViewById(R.id.create_donation);
        mMyDonationButton = (Button)findViewById(R.id.my_donations);
        mMyChatRoomButton = (Button) findViewById(R.id.my_chatrooms);
        mSignoutButton = (Button) findViewById(R.id.sign_out);

        // Initialize and request AdMob ad.
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //TODO put this stuff in a method, check for locationfirst then do the gps thing

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        gps = new GPSTracker(MainActivity.this);

        if(gps.canGetLocation()){
            Double latitude = gps.getLatitude();
            Double longitude = gps.getLongitude();
            MapModel mapModel = gps.getMapModel();


            editor.putString(Constants.LATITUDE, latitude.toString());
            editor.putString(Constants.LONGITUDE, longitude.toString());
            if (mapModel != null && mapModel.getPostalCode() != null && mapModel.getCity() != null && mapModel.getState() != null) {
                editor.putString(Constants.ZIP, mapModel.getPostalCode());
                editor.putString(Constants.CITY, mapModel.getCity());
                editor.putString(Constants.STATE, mapModel.getState());
            }
            editor.putString(Constants.PREFERENCES_LOCATION_KEY, Constants.ZIP);
            editor.commit();


            Toast.makeText(getApplicationContext(), "Latitude = " + latitude + "  Longitude = " + longitude, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }
        //Are they logged in, if so carry on, otherwise go to login
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name) +  ", " + firebaseUser.getDisplayName());
                    mSignoutButton.setVisibility(View.VISIBLE);
                } else {
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                }
            }
        };

        mSearchableButton.setOnClickListener(this);
        mPublicButton.setOnClickListener(this);
        mCreateDonationButton.setOnClickListener(this);
        mMyDonationButton.setOnClickListener(this);
        mMyChatRoomButton.setOnClickListener(this);
        mSignoutButton.setOnClickListener(this);
        LinearLayout linearLayoutMainActivity = (LinearLayout) findViewById(R.id.activity_main);
        initializeBackground(linearLayoutMainActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchable_donations:
                searchableDonations();
                break;
            case R.id.public_donations:
                publicDonations();
                break;
            case R.id.create_donation:
                createDonation();
                break;
            case R.id.my_donations:
                myDonations();
                break;
            case R.id.my_chatrooms:
                myChatRooms();
                break;
            case R.id.sign_out:
                AuthUI.getInstance().signOut(this);
                mSignoutButton.setVisibility(View.INVISIBLE);
                Util.initToast(this, "You have been signed out.");
                break;
            default:
                return;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void searchableDonations() {
        Intent intent = new Intent(this, SearchableActivity.class);
        startActivity(intent);
    }


    public void createDonation() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }

    public void publicDonations() {
        Intent intent = new Intent(this, PublicActivity.class);
        startActivity(intent);
    }

    public void myDonations() {
        Intent intent = new Intent(this, MyDonationActivity.class);
        startActivity(intent);
    }

    public void myChatRooms() {
        Intent intent = new Intent(this, MyChatListActivity.class);
        startActivity(intent);
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        if (mAuthListener != null) {
            mFirebaseAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
