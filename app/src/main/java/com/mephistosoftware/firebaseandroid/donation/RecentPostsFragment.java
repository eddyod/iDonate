package com.mephistosoftware.firebaseandroid.donation;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.util.Constants;

public class RecentPostsFragment extends PostListFragment {

    public RecentPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentPostsQuery = databaseReference.limitToFirst(Constants.LIMIT_ROWS);
        return recentPostsQuery;
    }
}
