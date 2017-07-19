package com.mephistosoftware.firebaseandroid.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage {

    private String userId;
    private String name; // name of user
    private String message;
    private Long timestamp;
    private FileModel file;
    private MapModel mapModel;


    public ChatMessage() {
    }


    public ChatMessage(String userId, String name, String message, Long timestamp) {
        this.userId = userId;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessage(String userId, String name, Long timestamp, MapModel mapModel) {
        this.userId = userId;
        this.name = name;
        this.timestamp = timestamp;
        this.mapModel = mapModel;
    }

    public ChatMessage(String userId, String name, Long timestamp, FileModel file) {
        this.userId = userId;
        this.name = name;
        this.timestamp = timestamp;
        this.file = file;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }


    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("message", message);
        result.put("name", name);
        result.put("timestamp", timestamp);
        result.put("default", "default");
        result.put("userId", userId);
        return result;
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                ", timestamp='" + timestamp + '\'' +
                ", message='" + message + '\'' +
                ", userId=" + userId +
                '}';
    }
}
