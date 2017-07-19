package com.mephistosoftware.firebaseandroid.donation;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.util.Constants;

public class CityDonationFragment extends PostListFragment {

    public CityDonationFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myCity = mSharedPreferences.getString(Constants.CITY, "San Diego");
        Query query = databaseReference.limitToFirst(Constants.LIMIT_ROWS).orderByChild("mapModel/city").equalTo(myCity);

        return query;
    }
}
