package com.ntu.moulsocial;

public class Friend {
    private String friendId;
    private String friendName;
    private String avatarUri;

    public Friend(String friendId, String friendName, String avatarUri) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.avatarUri = avatarUri;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}
