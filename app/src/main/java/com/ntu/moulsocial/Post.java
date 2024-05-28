package com.ntu.moulsocial;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String content;
    private String imageUri;
    private boolean isLiked;
    private int likeCount;
    private List<Comment> comments;

    public Post(String content, String imageUri) {
        this.content = content;
        this.imageUri = imageUri;
        this.isLiked = false;
        this.likeCount = 0;
        this.comments = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
