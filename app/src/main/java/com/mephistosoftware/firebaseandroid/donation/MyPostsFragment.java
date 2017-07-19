package com.mephistosoftware.firebaseandroid.donation;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.util.Constants;

public class MyPostsFragment extends PostListFragment {

    private FirebaseAuth mFirebaseAuth;
    private static final String NOUSER = "XXXX";


    public MyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        String uid = NOUSER;
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth != null && mFirebaseAuth.getCurrentUser() != null) {
            uid = mFirebaseAuth.getCurrentUser().getUid();
        }
        Log.d("MyPostsFragmentZZ", "here is the uid for my posts " + uid);
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        return query;
    }
}
