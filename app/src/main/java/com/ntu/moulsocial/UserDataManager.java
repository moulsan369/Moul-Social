package com.ntu.moulsocial;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UserDataManager {

    private static final String PREF_NAME = "user_data_pref";
    private static final String KEY_USER = "user";
    private static final String KEY_POSTS = "posts";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_FRIENDS = "friends";

    private SharedPreferences sharedPreferences;

    public UserDataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER, new Gson().toJson(user));
        editor.apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER, null);
        if (userJson != null) {
            return new Gson().fromJson(userJson, User.class);
        }
        return null;
    }

    public void savePosts(List<Post> posts) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_POSTS, Post.listToJson(posts));
        editor.apply();
    }

    public List<Post> getPosts() {
        String postsJson = sharedPreferences.getString(KEY_POSTS, null);
        if (postsJson != null) {
            return Post.jsonToList(postsJson);
        }
        return new ArrayList<>();
    }

    public void saveNotifications(List<Notification> notifications) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NOTIFICATIONS, Notification.listToJson(notifications));
        editor.apply();
    }

    public List<Notification> getNotifications() {
        String notificationsJson = sharedPreferences.getString(KEY_NOTIFICATIONS, null);
        if (notificationsJson != null) {
            return Notification.jsonToList(notificationsJson);
        }
        return new ArrayList<>();
    }

    public void saveFriends(List<Friend> friends) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FRIENDS, Friend.listToJson(friends));
        editor.apply();
    }

    public List<Friend> getFriends() {
        String friendsJson = sharedPreferences.getString(KEY_FRIENDS, null);
        if (friendsJson != null) {
            return Friend.jsonToList(friendsJson);
        }
        return new ArrayList<>();
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
