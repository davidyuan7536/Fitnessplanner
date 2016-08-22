package com.example.davidyuan7536.workoutlog;

/**
 * Created by davidyuan7536 on 8/18/2016.
 */
public class FirebaseWorkouts {

    private String title;
    private String timestamp;

    public FirebaseWorkouts() {
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "FirebaseWorkouts{" +
                "title='" + title + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
