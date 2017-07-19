package com.mephistosoftware.firebaseandroid.adapter;

/**
 * Extended by eodonnell on 12/5/16.
 */


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.model.Post;

import java.util.ArrayList;
import java.util.List;


public abstract class FirebasePostListAdapter extends FirebaseListAdapter {

    private Query mRef;
    private Class<Post> mModelClass;
    private int mLayout;
    private LayoutInflater mInflater;
    private List<Post> posts = new ArrayList<>();
    private List<Post> postsCopy= new ArrayList<>();
    private List<String> mKeys;
    private ChildEventListener mListener;


    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public FirebasePostListAdapter(Query mRef, Class<Post> mModelClass, int mLayout, Activity activity) {
        super(activity, mModelClass, mLayout, mRef);
        this.mRef = mRef;
        this.mModelClass = mModelClass;
        this.mLayout = mLayout;
        mInflater = activity.getLayoutInflater();
        posts = new ArrayList<Post>();
        mKeys = new ArrayList<String>();
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                Post model = dataSnapshot.getValue(FirebasePostListAdapter.this.mModelClass);
                String key = dataSnapshot.getKey();

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    posts.add(0, model);
                    postsCopy.add(0, model);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == posts.size()) {
                        posts.add(model);
                        postsCopy.add(model);
                        mKeys.add(key);
                    } else {
                        posts.add(nextIndex, model);
                        postsCopy.add(nextIndex, model);
                        mKeys.add(nextIndex, key);
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the posts changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                Post newModel = dataSnapshot.getValue(FirebasePostListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);

                posts.set(index, newModel);
                postsCopy.set(index, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                posts.remove(index);
                postsCopy.remove(index);

                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                Post newModel = dataSnapshot.getValue(FirebasePostListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);
                posts.remove(index);
                postsCopy.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    posts.add(0, newModel);
                    postsCopy.add(0,newModel);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == posts.size()) {
                        posts.add(newModel);
                        postsCopy.add(newModel);
                        mKeys.add(key);
                    } else {
                        posts.add(nextIndex, newModel);
                        postsCopy.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the posts
        mRef.removeEventListener(mListener);
        posts.clear();
        postsCopy.clear();
        mKeys.clear();
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Post getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(mLayout, viewGroup, false);
        }

        Post model = posts.get(i);
        // Call out to subclass to marshall this model into the provided view
        populateView(view, model);
        return view;
    }


    public void filter(String text) {
        posts.clear();
        if(text.isEmpty()){
            posts.addAll(postsCopy);
        } else{
            text = text.toLowerCase();
            for(Post post : postsCopy){
                if(post.title.toLowerCase().contains(text)){
                    posts.add(post);
                }
            }
        }
        notifyDataSetChanged();
    }


    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The arguments correspond to the mLayout and mModelClass given to the constructor of this class.
     * <p/>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param v     The view to populate
     * @param model The object containing the data used to populate the view
     */
    protected abstract void populateView(View v, Post model);
}
