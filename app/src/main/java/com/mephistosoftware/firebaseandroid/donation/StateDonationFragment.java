package com.mephistosoftware.firebaseandroid.donation;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.util.Constants;

public class StateDonationFragment extends PostListFragment {

    public StateDonationFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        String myState = mSharedPreferences.getString(Constants.STATE, "CA");
        Query query = databaseReference.limitToFirst(Constants.LIMIT_ROWS).orderByChild("mapModel/state").equalTo(myState);
        return query;
    }
}
