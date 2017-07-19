package com.mephistosoftware.firebaseandroid.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String key;
    public String uid;
    public String title;
    public String category;
    public String body;
    public Long timestamp;
    public MapModel mapModel;
    public FileModel file;
    public String distance;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String key, String uid, String title, String body) {
        this.key = key;
        this.uid = uid;
        this.title = title;
        this.body = body;
    }

    public Post(String key, String uid, String title, String body, MapModel mapModel) {
        this.key = key;
        this.uid = uid;
        this.title = title;
        this.body = body;
        this.mapModel = mapModel;
    }

    public Post(String key, String uid, String title, String body, MapModel mapModel, FileModel file) {
        this.key = key;
        this.uid = uid;
        this.title = title;
        this.body = body;
        this.mapModel = mapModel;
        this.file = file;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uid", uid);
        result.put("title", title);
        result.put("category", category);
        result.put("timestamp", timestamp);
        result.put("body", body);
        result.put("mapModel", mapModel);
        result.put("file", file);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
