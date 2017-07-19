package com.mephistosoftware.firebaseandroid.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.mephistosoftware.firebaseandroid.R;
import com.mephistosoftware.firebaseandroid.model.Post;
import com.google.firebase.database.Query;


/**
 * Created by eodonnell on 12/6/16.
 */

public class AutocompleteAdapter extends FirebaseListAdapter<Post> {


    public AutocompleteAdapter(Activity activity,Class<Post> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    public AutocompleteAdapter(Activity activity,Class<Post> modelClass, int modelLayout, Query ref, String titleIn) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }


    @Override
    public void populateView(View view, final Post post, int position) {
        TextView title = (TextView)view.findViewById(R.id.text_view_autocomplete_item);
        title.setText(post.title);
    }


}
