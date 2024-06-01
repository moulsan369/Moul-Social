package com.ntu.moulsocial;

public class Friend {
    private final String name;
    private final String profilePictureUri;

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
