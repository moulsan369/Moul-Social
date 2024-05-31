package com.ntu.moulsocial;

public class Friend {
    private String name;
    private String profilePictureUri;

    public Friend(String name, String profilePictureUri) {
        this.name = name;
        this.profilePictureUri = profilePictureUri;
    }

    public String getName() {
        return name;
    }

    public String getProfilePictureUri() {
        return profilePictureUri;
    }
}
