package com.mephistosoftware.firebaseandroid.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.adapter.PostListAdapter;
import com.mephistosoftware.firebaseandroid.donation.PostDetailActivity;
import com.mephistosoftware.firebaseandroid.model.Post;
import com.mephistosoftware.firebaseandroid.util.Constants;
import com.mephistosoftware.firebaseandroid.util.Util;

import java.util.ArrayList;
import java.util.List;


public class SearchableActivity extends BaseActivity {

    private static final String TAG = "SearchableActivity";
    private PostListAdapter mPostListAdapter;
    private DatabaseReference mDatabase;
    private ListView mListView;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donations);
        mListView = (ListView) findViewById(R.id.search_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Constants.DONATIONS);
        query = getQuery(mDatabase);
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name) +  ", " + user.getDisplayName());
                } else {
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                }
            }
        };

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshots) {
                mPostListAdapter = new PostListAdapter(SearchableActivity.this, query);
                mListView.setAdapter(mPostListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = (Post) mListView.getAdapter().getItem(position);
                Intent intent = new Intent(SearchableActivity.this, PostDetailActivity.class);
                intent.putExtra(Constants.EXTRA_POST_KEY, post.key);
                startActivity(intent);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        // Get the search close button image view
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Search close button clicked");
                EditText editText = (EditText) findViewById(R.id.search_src_text);
                //Clear the text from EditText view
                editText.setText("");

                //Clear query
                searchView.setQuery("", false);
                mPostListAdapter = new PostListAdapter(SearchableActivity.this, query);
                mListView.setAdapter(mPostListAdapter);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String search) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                mPostListAdapter.filter(search);
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        return true;
    }


    public Query getQuery(DatabaseReference databaseReference) {
        Query query = databaseReference;
        final SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String location = sharedPref.getString(Constants.PREFERENCES_LOCATION_KEY, Constants.ZIP);
        Log.d(TAG, "top of getQuery location " + location);

        String myZip = sharedPref.getString(Constants.ZIP, "92103");
        String myCity = sharedPref.getString(Constants.CITY, "San Diego");
        String myState = sharedPref.getString(Constants.STATE, "CA");

        switch (location) {
            case Constants.ZIP:
                Log.d(TAG, "getQuery in ZIP myzip = " + myZip);
                query = databaseReference.orderByChild("mapModel/postalCode").equalTo(myZip);
                break;
            case Constants.CITY:
                Log.d(TAG, "getQuery in CITY = " + myCity);
                query = databaseReference.orderByChild("mapModel/city").equalTo(myCity);
                break;
            case Constants.STATE:
                Log.d(TAG, "getQuery in STATE  = " + myState);
                query = databaseReference.orderByChild("mapModel/state").equalTo(myState);
                break;
            default:
                query = databaseReference;
                Log.d(TAG, "getQuery in default ");
        }

        return query;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "getQuery in onResume ");
        query = getQuery(mDatabase);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
