package com.mephistosoftware.firebaseandroid.util;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.donation.PostViewHolder;
import com.mephistosoftware.firebaseandroid.model.Post;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by eodonnell on 12/2/16.
 */

// public abstract class GenericRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>  {
    public class GenericRecyclerAdapter extends PostRecyclerAdapter<Post, PostViewHolder> {

    private final static String TAG = "GenericRecyleAdapter";

    List<Post> posts = new ArrayList<Post>();
    List<Post> postCopies = new ArrayList<Post>();
    private Context context;
    Class<PostViewHolder> viewHolderClass;
    PostViewHolder postViewHolder;




    public GenericRecyclerAdapter(Query query) {
        super(Post.class, R.layout.item_post, PostViewHolder.class, query);
    }

    public GenericRecyclerAdapter(Context context, Class<Post> modelClass, int modelLayout, Class<PostViewHolder> viewHolderClass, DatabaseReference ref, List<Post> postsIn) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        posts = postsIn;
        postCopies = postsIn;
        Log.d(TAG, " CONSTRUCTOR size of postsIn,postCopies " + postsIn.size() + "\t" + postCopies.size());
        this.context = context;
    }

    public GenericRecyclerAdapter(Class<Post> modelClass, int modelLayout, Class<PostViewHolder> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }



    @Override
    public void populateViewHolder(PostViewHolder postViewHolder, Post post, int position) {
        postViewHolder.titleView.setText(post.title);
        postViewHolder.bodyView.setText("where is the body");
    }



}
