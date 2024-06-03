package com.ntu.moulsocial;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Notification {
    private String id;
    private String message;

    public Notification(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String listToJson(List<Notification> notifications) {
        Gson gson = new Gson();
        return gson.toJson(notifications);
    }

    public static List<Notification> jsonToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Notification>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
