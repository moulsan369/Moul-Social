package com.ntu.moulsocial;

/**
 * Represents a social media post.
 */
public class ItemPost {
    private String username;
    private String postTime;
    private String postContent;
    private int profileImage;
    private int postImage;
    private boolean isLiked;
    
    public ItemPost(String username, String postTime, String postContent, int profileImage, int postImage) {
        this.username = username;
        this.postTime = postTime;
        this.postContent = postContent;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.isLiked = false;
    }

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

    public String getContent() {
        return postContent;
    }
}
