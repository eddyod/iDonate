package com.mephistosoftware.firebaseandroid.donation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.model.MapModel;
import com.mephistosoftware.firebaseandroid.model.Post;
import com.mephistosoftware.firebaseandroid.util.Constants;

public abstract class PostListFragment extends Fragment  {

    private static final String TAG = "PostListFragment XXXX";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    //Firebase and GoogleApiClient


    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    protected SharedPreferences mSharedPreferences;



    public PostListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.DONATIONS);
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(super.getActivity());

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.item_post,
                PostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post post, final int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();
                post.distance = getDistance(post.mapModel);
                // Bind Post to ViewHolder, setting OnClickListener for the title
                viewHolder.bindToPost(post, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(Constants.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    private String getDistance(MapModel mapModel) {
        String displayDistance = "";
        //origin.set
        String sLongitude = mSharedPreferences.getString(Constants.LONGITUDE, "0.0");
        String sLatitude = mSharedPreferences.getString(Constants.LATITUDE, "0.0");
        Location here = new Location("");


        Location newLocation = new Location("newlocation");
        if (mapModel == null || mapModel.getLatitude() == null || mapModel.getLocation() == null) {
            displayDistance = "NA";
        } else {
            newLocation.setLatitude(mapModel.getLatitude());
            newLocation.setLongitude(mapModel.getLongitude());
            here.setLongitude(Double.parseDouble(sLongitude));
            here.setLatitude(Double.parseDouble(sLatitude));
            float distance = here.distanceTo(newLocation) / 1000; // in km
            displayDistance = distance + " km";
        }
        return displayDistance;

    }



    public abstract Query getQuery(DatabaseReference databaseReference);

}
