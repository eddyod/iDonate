package com.mephistosoftware.firebaseandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mephistosoftware.firebaseandroid.MainActivity;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.donation.ChatViewHolder;
import com.mephistosoftware.firebaseandroid.model.ChatRoom;
import com.mephistosoftware.firebaseandroid.model.User;
import com.mephistosoftware.firebaseandroid.util.Constants;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.key;


public class MyChatListActivity extends BaseActivity {

    private static final String TAG = "MyChatListActivity";
    private FirebaseRecyclerAdapter<ChatRoom, ChatViewHolder> mAdapter;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mEmptyList;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mLinearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychatrooms);
        mRecycler = (RecyclerView) findViewById(R.id.chatroom_list);
        mEmptyList = (TextView) findViewById(R.id.chatroom_list_noitems);
        mProgressBar = (ProgressBar) findViewById(R.id.chatroom_list_loading);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecycler.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    getSupportActionBar().setTitle(getResources().getString(R.string.app_name) +  ", " + firebaseUser.getDisplayName());
                    readChatRoomFirebase(firebaseUser.getUid());
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER,
                                            AuthUI.FACEBOOK_PROVIDER)
                                    .build(), Constants.RC_SIGN_IN);
                }
            }
        };


    }

    public void readChatRoomFirebase(String search) {

        Query postsQuery = getQuery(mDatabase, search);
        mAdapter = new FirebaseRecyclerAdapter<ChatRoom, ChatViewHolder>(ChatRoom.class, R.layout.item_chat,
                ChatViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ChatViewHolder viewHolder, final ChatRoom room, final int position) {
                final DatabaseReference postRef = getRef(position);
                final String postKey = postRef.getKey();
                Log.d(TAG, " postKey is " + postKey.toString());
                // Bind Post to ViewHolder, setting OnClickListener for the title
                viewHolder.bindToPost(room, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        Intent intent = new Intent(MyChatListActivity.this, ChatActivity.class);
                        intent.putExtra(Constants.EXTRA_CHAT_KEY, postKey);
                        intent.putExtra(Constants.EXTRA_POST_TITLE, room.getName());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.setAdapter(mAdapter);

        postsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                mProgressBar.setVisibility(View.GONE);
                if (count > 0) {
                    Log.d(TAG, " get count is gt zero " + count);
                    mEmptyList.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, " get count is NOT gt zero " + count);
                    mEmptyList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Query getQuery(DatabaseReference databaseReference, String uid) {
        Query query = databaseReference.child(Constants.USERS).child(uid).child(Constants.ROOMS);
        /*
        if (search != null) {
            Log.d(TAG, "get query search is NOT null " + search);
            query = databaseReference.child(Constants.CHATROOM).orderByChild("createdByUserId").equalTo(search);
        }
        */
        return query;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                updateUser(mFirebaseAuth, mDatabase);
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAuthListener != null) {
            mFirebaseAuth.addAuthStateListener(mAuthListener);
        }
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
                Intent launchNewIntent = new Intent(MyChatListActivity.this,MainActivity.class);
                startActivityForResult(launchNewIntent, 0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
