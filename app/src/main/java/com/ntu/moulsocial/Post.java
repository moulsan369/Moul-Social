package com.ntu.moulsocial;

public class Post {
    private String username;
    private String postTime;
    private String postContent;
    private int profileImage;
    private int postImage;

    public Post(String username, String postTime, String postContent, int profileImage, int postImage) {
        this.username = username;
        this.postTime = postTime;
        this.postContent = postContent;
        this.profileImage = profileImage;
        this.postImage = postImage;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPostTime() { return postTime; }
    public void setPostTime(String postTime) { this.postTime = postTime; }

    public String getPostContent() { return postContent; }
    public void setPostContent(String postContent) { this.postContent = postContent; }

    public int getProfileImage() { return profileImage; }
    public void setProfileImage(int profileImage) { this.profileImage = profileImage; }

    public int getPostImage() { return postImage; }
    public void setPostImage(int postImage) { this.postImage = postImage; }
}
