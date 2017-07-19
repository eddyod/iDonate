package com.mephistosoftware.firebaseandroid.donation;


import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.util.Constants;

public class ZipDonationFragment extends PostListFragment {


    public ZipDonationFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myZip = mSharedPreferences.getString(Constants.ZIP, "92103");
        Query query = databaseReference.limitToFirst(Constants.LIMIT_ROWS).orderByChild("mapModel/postalCode").equalTo(myZip);
        return query;
    }
}
