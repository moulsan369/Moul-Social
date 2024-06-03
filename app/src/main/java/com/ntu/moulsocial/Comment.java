package com.ntu.moulsocial;

public class Comment {
    private String id;
    private String postId;
    private String userId;
    private String content;
    private String profilePictureUrl;

    // No-argument constructor
    public Comment() {
    }

    public Comment(String id, String postId, String userId, String content, String profilePictureUrl) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getter and setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
