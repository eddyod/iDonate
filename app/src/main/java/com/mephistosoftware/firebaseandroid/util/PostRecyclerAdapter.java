package com.mephistosoftware.firebaseandroid.util;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by eddyod on 12/3/16.
 */

public abstract class PostRecyclerAdapter <Post, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private static final String TAG = PostRecyclerAdapter.class.getSimpleName();

    private FirebaseArray mSnapshots;
    private FirebaseArray mSnapshotCopy;
    //private List<FirebaseArray> mSnapshots;
    private Class<Post> mModelClass;
    protected Class<VH> mViewHolderClass;
    protected int mModelLayout;
    private Query query;
    private int mLength;

    PostRecyclerAdapter(Class<Post> modelClass,
                            @LayoutRes int modelLayout,
                            Class<VH> viewHolderClass,
                            FirebaseArray snapshots) {
        mModelClass = modelClass;
        mModelLayout = modelLayout;
        mViewHolderClass = viewHolderClass;
        mSnapshots = snapshots;
        mSnapshotCopy = snapshots;
        mLength = mSnapshots.getCount();


        mSnapshots.setOnChangedListener(new FirebaseArray.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case ADDED:
                        notifyItemInserted(index);
                        break;
                    case CHANGED:
                        notifyItemChanged(index);
                        break;
                    case REMOVED:
                        notifyItemRemoved(index);
                        break;
                    case MOVED:
                        notifyItemMoved(oldIndex, index);
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                PostRecyclerAdapter.this.onCancelled(databaseError);
            }
        });
    }



    /**
     * @param modelClass      Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list. You will be responsible for populating an
     *                        instance of the corresponding view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                        combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public PostRecyclerAdapter(Class<Post> modelClass,
                                   int modelLayout,
                                   Class<VH> viewHolderClass,
                                   Query ref) {
        this(modelClass, modelLayout, viewHolderClass, new FirebaseArray(ref));
        query = ref;
        mLength = 4;
    }

    public void cleanup() {
        mSnapshots.cleanup();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.getCount();
    }

    public Post getItem(int position) {
        return parseSnapshot(mSnapshots.getItem(position));
    }

    /**
     * This method parses the DataSnapshot into the requested type. You can override it in subclasses
     * to do custom parsing.
     *
     * @param snapshot the DataSnapshot to extract the model from
     * @return the model extracted from the DataSnapshot
     */
    protected Post parseSnapshot(DataSnapshot snapshot) {
        return snapshot.getValue(mModelClass);
    }

    public DatabaseReference getRef(int position) {
        return mSnapshots.getItem(position).getRef();
    }

    @Override
    public long getItemId(int position) {
        // http://stackoverflow.com/questions/5100071/whats-the-purpose-of-item-ids-in-android-listview-adapter
        return mSnapshots.getItem(position).getKey().hashCode();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        try {
            Constructor<VH> constructor = mViewHolderClass.getConstructor(View.class);
            return constructor.newInstance(view);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        Post model = getItem(position);
        populateViewHolder(viewHolder, model, position);
    }

    public void filter(String text) {
        Log.d(TAG, " top of filter size of mSnapshots " + mLength);
        if(text.isEmpty()){
            //mSnapshots = mSnapshotCopy;
            Log.d(TAG, " top of filter text is empty ");
            //posts.addAll(postCopies);
        } else{
            text = text.toLowerCase();
            for (int i = 0; i < mLength; i++) {
                DataSnapshot snapshot = mSnapshots.getItem(i);
                String title = (String) snapshot.child("title").getValue();
                if (title.toLowerCase().contains(text)) {
                    Log.d(TAG, "Found search '" + text + "'\tat child " + i + "\t" + title);
                } else {
                    mSnapshots.onChildRemoved(mSnapshots.getItem(i));
                    Log.d(TAG, "Did not find search '" + text + "'\tat child " + i + "\t" + title);
                    mLength--;
                }
            }
        }
        Log.d(TAG, " bottom of filter size of mSnapshots " + mLength);
        //mSnapshots = mSnapshotCopy;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return mModelLayout;
    }

    /**
     * This method will be triggered in the event that this listener either failed at the server,
     * or is removed as a result of the security and Firebase Database rules.
     *
     * @param error A description of the error that occurred
     */
    protected void onCancelled(DatabaseError error) {
        Log.w(TAG, error.toException());
    }

    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The first two arguments correspond to the mLayout and mModelClass given to the constructor of
     * this class. The third argument is the item's position in the list.
     * <p>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param viewHolder The view to populate
     * @param model      The object containing the data used to populate the view
     * @param position   The position in the list of the view being populated
     */
    abstract protected void populateViewHolder(VH viewHolder, Post model, int position);
}
