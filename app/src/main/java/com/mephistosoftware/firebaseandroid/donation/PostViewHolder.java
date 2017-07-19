package com.mephistosoftware.firebaseandroid.donation;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.model.Post;


public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView categoryView;
    public TextView locationView;
    public TextView bodyView;
    public ImageView imageView;
    public TextView distanceView;
    public TextView timestampView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        categoryView = (TextView) itemView.findViewById(R.id.post_category);
        locationView = (TextView) itemView.findViewById(R.id.post_location);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        imageView = (ImageView) itemView.findViewById(R.id.post_picture);
        distanceView = (TextView) itemView.findViewById(R.id.post_distance);
        timestampView = (TextView) itemView.findViewById(R.id.post_timestamp);
    }

    public void bindToPost(Post post, View.OnClickListener titleClickListener) {
        titleView.setText(post.title);
        categoryView.setText(post.category);
        String displayLocation = "NA";
        if (post.mapModel != null && post.mapModel.getLocation() != null) {
            displayLocation = post.mapModel.getLocation();
        }
        locationView.setText(displayLocation);
        bodyView.setText(post.body);
        distanceView.setText(post.distance);
        String displayTimestamp = "";
        if (post.timestamp != null) {
            displayTimestamp = convertTimestamp(post.timestamp.toString()).toString();
        }
        timestampView.setText(displayTimestamp);
        titleView.setOnClickListener(titleClickListener);
    }

    private CharSequence convertTimestamp(String milliSeconds){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(milliSeconds),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }




    public void setDonationPhoto(String url){
        if (imageView == null)return;
        Glide.with(imageView.getContext()).load(url)
                .override(100, 100)
                .fitCenter()
                .into(imageView);
    }

}
