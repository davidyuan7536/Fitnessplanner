package com.example.davidyuan7536.workoutlog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidyuan7536 on 7/26/2016.
 */
public class HomeFeedRecycleViewUser {
    private String avatar;
    private String uID;
    private String userName;
    private String workoutID;
    private String workoutTitle;
    private String timestamp;
    private List<String> exerciseName;
    private List<List<String>> singleExerciseDescription;

    public HomeFeedRecycleViewUser(String uID, String userName, String avatar, String workoutID, String workoutTitle, String timestamp) {
        this.uID = uID;
        this.userName = userName;
        this.avatar = avatar;
        this.workoutID = workoutID;
        this.workoutTitle = workoutTitle;
        this.timestamp = timestamp;
        this.exerciseName = new ArrayList<String>();
        this.singleExerciseDescription = new ArrayList<List<String>>();
    }



    public String getUserName() {
        return userName;
    }

    public String getWorkoutTitle() {
        return workoutTitle;
    }


    public String getuID() {
        return uID;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getWorkoutID() {
        return workoutID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<String> getExerciseName() {
        return exerciseName;
    }

    public List<String> getSingleExerciseDescription(int position) {
        return singleExerciseDescription.get(position);
    }





    public void setWorkoutTitle(String workoutTitle) {
        this.workoutTitle = workoutTitle;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName.add(exerciseName);
    }

    public void setSingleExerciseDescription(List<String> singleExerciseDescription) {
        this.singleExerciseDescription.add(singleExerciseDescription);
    }

    public void deleteSingleExerciseDescription() {
        this.singleExerciseDescription = new ArrayList<List<String>>();
    }

    public void deleteExerciseName() {
        this.exerciseName = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "HomeFeedRecycleViewUser{" +
                "avatar='" + avatar + '\'' +
                ", uID='" + uID + '\'' +
                ", userName='" + userName + '\'' +
                ", workoutID='" + workoutID + '\'' +
                ", workoutTitle='" + workoutTitle + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", exerciseName=" + exerciseName +
                ", singleExerciseDescription=" + singleExerciseDescription +
                '}';
    }
}

