package com.ntu.moulsocial;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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

    public static String listToJson(List<Friend> friends) {
        Gson gson = new Gson();
        return gson.toJson(friends);
    }

    public static List<Friend> jsonToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Friend>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
