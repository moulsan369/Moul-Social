package com.ntu.moulsocial;

public class Comment {
    private String content;
    private String profilePictureUri;

    public Comment(String content, String profilePictureUri) {
        this.content = content;
        this.profilePictureUri = profilePictureUri;
    }

    public String getContent() {
        return content;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }
}
