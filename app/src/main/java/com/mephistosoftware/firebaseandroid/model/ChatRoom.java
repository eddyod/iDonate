package com.mephistosoftware.firebaseandroid.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eddyod on 11/19/16.
 *
 */

public class ChatRoom {

    private String id;
    private Long createdAt;
    private String createdByUserId;
    private String name;

    private String type;

    public ChatRoom() {}

    public ChatRoom(Long createdAt, String createdByUserId, String name) {
        this.createdAt = createdAt;
        this.createdByUserId = createdByUserId;
        this.name = name;
    }

    public ChatRoom(String id, Long createdAt, String createdByUserId, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdByUserId = createdByUserId;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }


    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("createdAt", createdAt);
        result.put("createdByUserId", createdByUserId);
        result.put("id", id);
        result.put("name", name);
        result.put("type", type);
        return result;
    }

    @Exclude
    public Map<String, Object> userMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("createdAt", createdAt);
        result.put("name", name);
        return result;
    }

}
