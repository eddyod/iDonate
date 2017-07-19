package com.mephistosoftware.firebaseandroid.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.category;
import static android.R.attr.key;
import static com.mephistosoftware.firebaseandroid.R.id.timestamp;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String uid;
    public String displayName;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String displayName, String email) {
        this.uid = uid;
        this.displayName = displayName;
        this.email = email;
    }

    // [START user_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("displayName", displayName);
        result.put("email", email);

        return result;
    }
    // [END user_to_map]



}
// [END blog_user_class]
