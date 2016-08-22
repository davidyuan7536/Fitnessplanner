package com.example.davidyuan7536.workoutlog;

import java.util.HashMap;
import java.util.List;

/**
 * Created by davidyuan7536 on 8/20/2016.
 */
public class FirebaseUsersProfile {
    private String avatar;
    private String name;
    private String username;
    private String quote;
    private HashMap<String, FirebasePictures> pictures;

    public FirebaseUsersProfile() {
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getQuote() {
        return quote;
    }

    public HashMap<String, FirebasePictures> getPictures() {
        return pictures;
    }
}
