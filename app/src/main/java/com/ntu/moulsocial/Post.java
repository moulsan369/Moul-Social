package com.ntu.moulsocial;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String id;
    private String userId;
    private String content;
    private String imageUri;
    private int likes;
    private boolean isLiked;
    private List<Comment> comments;
    private long timestamp;

    // No-argument constructor required for Firebase
    public Post() {
        this.comments = new ArrayList<>();
    }

    public Post(String id, String userId, String content, String imageUri, int likes, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imageUri = imageUri;
        this.likes = likes;
        this.isLiked = false;
        this.comments = new ArrayList<>();
        this.timestamp = timestamp;
    }

    // Getter and setter methods...
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public String getImageUri() { return imageUri; }
    public int getLikes() { return likes; }
    public boolean isLiked() { return isLiked; }
    public List<Comment> getComments() { return comments; }
    public long getTimestamp() { return timestamp; }

    public void setLiked(boolean liked) { isLiked = liked; }
    public void setLikes(int likes) { this.likes = likes; }
    public void addComment(Comment comment) { comments.add(comment); }
    public void incrementLikes() { likes++; }

    public void setComments(List<Comment> comments) { this.comments = comments; }

    public static String listToJson(List<Post> posts) {
        Gson gson = new Gson();
        return gson.toJson(posts);
    }

    public static List<Post> jsonToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Post>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
