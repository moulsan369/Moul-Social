package com.ntu.moulsocial;

import java.util.ArrayList;
import java.util.List;

public class ItemPost {
    private final String username;
    private final String postTime;
    private final String postContent;
    private final int profileImage;
    private final int postImage;
    private boolean isLiked;
    private int likeCount;
    private final List<String> comments;

    public ItemPost(String username, String postTime, String postContent, int profileImage, int postImage) {
        this.username = username;
        this.postTime = postTime;
        this.postContent = postContent;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.isLiked = false;
        this.likeCount = 0;
        this.comments = new ArrayList<>();
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public int getPostImage() {
        return postImage;
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

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    public List<String> getComments() {
        return comments;
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }

    public int getCommentCount() {
        return comments.size();
    }
}

