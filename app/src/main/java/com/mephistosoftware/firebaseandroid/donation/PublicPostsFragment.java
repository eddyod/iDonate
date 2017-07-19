package com.mephistosoftware.firebaseandroid.donation;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class PublicPostsFragment extends PostListFragment {

    public PublicPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START my_top_posts_query]
        Query publicPostsQuery = databaseReference.child("donations");
        return publicPostsQuery;
    }
}
