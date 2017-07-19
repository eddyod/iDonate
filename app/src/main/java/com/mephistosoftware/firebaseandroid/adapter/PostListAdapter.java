package com.mephistosoftware.firebaseandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.donation.PostViewHolder;
import com.mephistosoftware.firebaseandroid.model.Post;

import java.util.List;


/**
 * Created by eodonnell on 12/2/16.
 */

// public abstract class GenericRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>  {
    public class PostListAdapter extends FirebasePostListAdapter {

    private final static String TAG = "GenericRecyleAdapter";

    private Context context;
    Class<PostViewHolder> viewHolderClass;
    PostViewHolder postViewHolder;

    public PostListAdapter(Activity activity, Query query) {
        super(query, Post.class, R.layout.item_post, activity);
    }


    @Override
    public void populateView(View view, Post post) {
        TextView title = (TextView)view.findViewById(R.id.post_title);
        title.setText(post.title);
        TextView location = (TextView) view.findViewById(R.id.post_location);
        String displayLocation = "";
        if (post.mapModel != null && post.mapModel.getLocation() != null) {
            displayLocation = post.mapModel.getLocation();
        }
        location.setText(displayLocation);
    }

    @Override
    public void populateView(View view, Object o, int position) {
        Post post = (Post) o;
        TextView title = (TextView)view.findViewById(R.id.post_title);
        title.setText("hey you");
    }

}
