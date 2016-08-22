package com.example.davidyuan7536.workoutlog;

import java.util.HashMap;

/**
 * Created by davidyuan7536 on 8/19/2016.
 */
public class FirebaseUsers {
    private String avatar;
    private FirebaseEmail email;
    private HashMap<String, Object> followers;
    private HashMap<String, Object> following;
    private String name;
    private String number;
    private FirebasePictures pictues;
    private String quote;
    private String username;

    public FirebaseUsers() {
    }

    public String getAvatar() {
        return avatar;
    }

    public FirebaseEmail getEmail() {
        return email;
    }

    public HashMap<String, Object> getFollowers() {
        return followers;
    }

    public HashMap<String, Object> getFollowing() {
        return following;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public FirebasePictures getPictues() {
        return pictues;
    }

    public String getQuote() {
        return quote;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "FirebaseUsers{" +
                "avatar='" + avatar + '\'' +
                ", email=" + email +
                ", followers=" + followers +
                ", following=" + following +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", pictues=" + pictues +
                ", quote='" + quote + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
