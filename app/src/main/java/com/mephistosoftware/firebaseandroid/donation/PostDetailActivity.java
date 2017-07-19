package com.mephistosoftware.firebaseandroid.donation;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mephistosoftware.firebaseandroid.MainActivity;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.model.Post;
import com.mephistosoftware.firebaseandroid.util.Constants;
import com.mephistosoftware.firebaseandroid.util.Util;
import com.mephistosoftware.firebaseandroid.view.BaseActivity;
import com.mephistosoftware.firebaseandroid.view.ChatActivity;


public class PostDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";


    private DatabaseReference mPostReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser firebaseUser;


    private ValueEventListener mPostListener;

    private String mPostKey;

    private TextView mTitleView;
    private TextView mCategoryView;
    private TextView mLocationView;
    private TextView mBodyView;
    private ImageView mImageView;
    private Button mChatButton;
    private PostViewHolder viewHolder;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(Constants.EXTRA_POST_KEY);
        Log.d(TAG, "XXX PostDetailActivity onCreate  key " + mPostKey);
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("donations").child(mPostKey);

        // Initialize Views
        mTitleView = (TextView) findViewById(R.id.post_title);
        mCategoryView = (TextView) findViewById(R.id.post_category);
        mLocationView = (TextView) findViewById(R.id.post_location);
        mBodyView = (TextView) findViewById(R.id.post_body);
        mImageView = (ImageView) findViewById(R.id.post_picture);
        mChatButton = (Button) findViewById(R.id.button_start_chat);
        mBodyView.setVisibility(View.VISIBLE);

        //If they are logged in, show the comment stuff
        if (Util.isLoggedIn(mFirebaseAuth)) {
            firebaseUser = mFirebaseAuth.getCurrentUser();
            mChatButton.setVisibility(View.VISIBLE);
            mChatButton.setEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name) +  ", " + firebaseUser.getDisplayName());
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            mChatButton.setVisibility(View.VISIBLE);
            mChatButton.setEnabled(false);
        }
        mChatButton.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                post = dataSnapshot.getValue(Post.class);

                // [START_EXCLUDE]
                if (post != null) {
                    mTitleView.setText(post.title);
                    mCategoryView.setText(post.category);
                    if (post.mapModel != null) {
                        mLocationView.setText(post.mapModel.getLocation());
                    } else {
                        mLocationView.setText("NA");
                    }
                    mBodyView.setText(post.body);

                    if (post.file != null){
                        mImageView.setVisibility(View.VISIBLE);
                        Log.d(TAG, "PostDetailActivity onStart " + post.file.getUrl_file());
                        Glide.with(mImageView.getContext()).load(post.file.getUrl_file())
                                .fitCenter()
                                .into(mImageView);
                    }



                } else {
                    Toast.makeText(PostDetailActivity.this, "No post object.",
                            Toast.LENGTH_LONG).show();
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_start_chat) {
            startChat();
        }
    }

    private void startChat() {
        String chatRoom = mPostKey;
        Log.d(TAG, "XXX PostDetailActivity startChat with new chatRoom: " + chatRoom);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_CHAT_KEY, chatRoom);
        intent.putExtra(Constants.EXTRA_POST_TITLE, post.title);
        intent.putExtra(Constants.EXTRA_POST_UID, post.uid);
        startActivity(intent);

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
                Intent launchNewIntent = new Intent(PostDetailActivity.this,MainActivity.class);
                startActivityForResult(launchNewIntent, 0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
