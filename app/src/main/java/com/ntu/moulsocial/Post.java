package com.ntu.moulsocial;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String content;
    private String imageUri;
    private String profilePictureUri;
    private int likeCount;
    private boolean isLiked;
    private List<Comment> comments;

    public Post(String content, String imageUri) {
        this.content = content;
        this.imageUri = imageUri;
        this.likeCount = 0;
        this.isLiked = false;
        this.comments = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }

    public void setProfilePictureUri(String profilePictureUri) {
        this.profilePictureUri = profilePictureUri;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
