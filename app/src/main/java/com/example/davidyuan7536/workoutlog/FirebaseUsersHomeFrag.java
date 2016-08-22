package com.example.davidyuan7536.workoutlog;

import java.util.HashMap;

/**
 * Created by davidyuan7536 on 8/19/2016.
 */
public class FirebaseUsersHomeFrag {

    private String avatar;
    private String username;

    public FirebaseUsersHomeFrag() {
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "FirebaseUsersHomeFrag{" +
                "avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
