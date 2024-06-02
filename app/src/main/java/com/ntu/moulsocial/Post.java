package com.ntu.moulsocial;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String id;
    private String userId;
    private String content;
    private String imageUri;
    private int likes;
    private boolean isLiked;
    private List<String> comments;

    public Post(String id, String userId, String content, String imageUri, int likes) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imageUri = imageUri;
        this.likes = likes;
        this.isLiked = false;
        this.comments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void incrementLikes() {
        likes++;
    }

    public List<String> getComments() {
        return comments;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }
}
