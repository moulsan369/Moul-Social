package com.ntu.moulsocial;

public class User {
    private String userId;
    private String userName;
    private String avatarUri;

    public User(String userId, String userName, String avatarUri) {
        this.userId = userId;
        this.userName = userName;
        this.avatarUri = avatarUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
