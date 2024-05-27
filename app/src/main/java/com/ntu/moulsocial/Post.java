package com.ntu.moulsocial;

import android.graphics.Bitmap;

public class Post {
    private final String username;
    private final String postTime;
    private final String postContent;
    private final Bitmap postImage;
    private int likeCount;

    public Post(String username, String postTime, String postContent, Bitmap postImage, int likeCount) {
        this.username = username;
        this.postTime = postTime;
        this.postContent = postContent;
        this.postImage = postImage;
        this.likeCount = likeCount;
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

    public Bitmap getPostImage() {
        return postImage;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void incrementLikeCount() {
        likeCount++;
    }
}
