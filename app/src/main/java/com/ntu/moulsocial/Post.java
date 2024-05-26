package com.ntu.moulsocial;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private int likeCount;
    private boolean liked;
    private List<Comment> comments;

    public Post(int likeCount, boolean liked) {
        this.likeCount = likeCount;
        this.liked = liked;
        this.comments = new ArrayList<>();
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

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
