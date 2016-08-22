package com.example.davidyuan7536.workoutlog;

import java.util.List;

/**
 * Created by davidyuan7536 on 8/3/2016.
 */
public class FirebaseExercises {

    private String name;
    private List<String> sets;

    public FirebaseExercises() {
    }

    public String getName() {
        return name;
    }

    public List<String> getSets() {
        return sets;
    }

    @Override
    public String toString() {
        return "FirebaseExercises{" +
                "name='" + name + '\'' +
                ", sets=" + sets +
                '}';
    }
}
