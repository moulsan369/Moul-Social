package com.ntu.moulsocial;

public class Comment {
    private final String content;
    private final String profilePictureUri;

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
