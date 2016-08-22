package com.example.davidyuan7536.workoutlog;

import java.util.List;

/**
 * Created by davidyuan7536 on 8/12/2016.
 */
public class ProfileWorkoutRecyclerView {

    private String workoutName;
    private String workoutID;
    private List<String> exerciseName;
    private List<String> exerciseID;
    private List<List<String>> singleExerciseDescription;
    private String date;

    public ProfileWorkoutRecyclerView(String workoutName, String workoutID, List<String> exerciseName, List<String> exerciseID, List<List<String>> singleExerciseDescription, String date) {
        this.workoutName = workoutName;
        this.workoutID = workoutID;
        this.exerciseName = exerciseName;
        this.exerciseID = exerciseID;
        this.singleExerciseDescription = singleExerciseDescription;
        this.date = date;
    }


    public String getWorkoutName() {
        return workoutName;
    }

    public String getWorkoutID() {
        return workoutID;
    }

    public List<String> getExerciseID() {
        return exerciseID;
    }

    public List<String> getExerciseName() {
        return exerciseName;
    }

    public List<List<String>> getSingleExerciseDescription() {
        return singleExerciseDescription;
    }

    public List<String> getSingleExerciseDescription(int position){
        return singleExerciseDescription.get(position);
    }

    public String getDate() {
        return date;
    }

    public void setExerciseName(List<String> exerciseName) {
        this.exerciseName = exerciseName;
    }

    public void setExerciseID(List<String> exerciseID) {
        this.exerciseID = exerciseID;
    }

    public void setSingleExerciseDescription(List<List<String>> singleExerciseDescription) {
        this.singleExerciseDescription = singleExerciseDescription;
    }

    public void addExerciseName(String exerciseName) {
        this.exerciseName.add(exerciseName);
    }

    public void addExerciseID(String exerciseID) {
        this.exerciseID.add(exerciseID);
    }

    public void addSingleExerciseDescription(List<String> singleExerciseDescription) {
        this.singleExerciseDescription.add(singleExerciseDescription);
    }


}
