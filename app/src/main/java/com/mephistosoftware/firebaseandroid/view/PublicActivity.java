package com.mephistosoftware.firebaseandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mephistosoftware.firebaseandroid.MainActivity;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.donation.StateDonationFragment;
import com.mephistosoftware.firebaseandroid.donation.CityDonationFragment;
import com.mephistosoftware.firebaseandroid.donation.ZipDonationFragment;
import com.mephistosoftware.firebaseandroid.util.Constants;
import com.mephistosoftware.firebaseandroid.util.Util;

public class PublicActivity extends BaseActivity {

    private static final String TAG = "PublicActivity";

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_donation);

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new ZipDonationFragment(),
                    new CityDonationFragment(),
                    new StateDonationFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    Constants.ZIP,
                    Constants.CITY,
                    Constants.STATE,
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        mFirebaseAuth = FirebaseAuth.getInstance();

        if (Util.isLoggedIn(mFirebaseAuth)) {
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name) +  ", " + mFirebaseUser.getDisplayName());

        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.public_container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.public_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    // menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goHome:
                Intent launchNewIntent = new Intent(PublicActivity.this,MainActivity.class);
                startActivityForResult(launchNewIntent, 0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
