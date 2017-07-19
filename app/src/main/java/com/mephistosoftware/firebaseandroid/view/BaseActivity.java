package com.mephistosoftware.firebaseandroid.view;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.model.User;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;


    public void updateUser(FirebaseAuth mFirebaseAuth,  DatabaseReference mDatabase) {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        //    public User(String uid, String displayName, String email) {
        String uid = firebaseUser.getUid();
        String displayName = firebaseUser.getDisplayName();
        if (displayName == null || displayName.length() < 2) {
            Log.d("BASE", "no display name, getting it from the email");
            displayName = getNameFromEmail(firebaseUser);
        }
        User user = new User(uid, displayName, firebaseUser.getEmail());
        Map<String, Object> userValues = user.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + uid, userValues);
        mDatabase.updateChildren(childUpdates);
    }

    private String getNameFromEmail(FirebaseUser user) {
        String email = user.getEmail();
        int index = email.indexOf("@");
        String displayName =  email.substring(0,index);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("BASE", "User profile updated.");
                        }
                    }
                });
        return displayName;
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void initializeBackground(LinearLayout linearLayout) {

        /**
         * Set different background image for landscape and portrait layouts
         */
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen_land);
        } else {
            linearLayout.setBackgroundResource(R.drawable.background_loginscreen);
        }
    }



    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}
