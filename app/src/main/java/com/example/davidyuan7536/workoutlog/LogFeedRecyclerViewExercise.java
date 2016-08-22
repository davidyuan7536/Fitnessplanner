package com.example.davidyuan7536.workoutlog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidyuan7536 on 7/28/2016.
 */
public class LogFeedRecyclerViewExercise {

    private String workoutTitle;
    private List<String> exerciseName;
    private List<String> exerciseID;
    private List<List<String>> singleExerciseDescription;

    private String workoutUniqueID;
    private String date;


    public LogFeedRecyclerViewExercise(String workoutTitle, List<String> exerciseName, List<String> exerciseID, List<List<String>> singleExerciseDescription, String workoutUniqueID, String date) {
        this.workoutTitle = workoutTitle;
        this.singleExerciseDescription = singleExerciseDescription;
        this.exerciseName = exerciseName;
        this.exerciseID = exerciseID;
        this.workoutUniqueID = workoutUniqueID;
        this.date = date;

    }

    public LogFeedRecyclerViewExercise(String workoutTitle, String workoutUniqueID, String date) {
        this.workoutTitle = workoutTitle;
        this.singleExerciseDescription = new ArrayList<List<String>>();
        this.exerciseName = new ArrayList<String>();
        this.exerciseID = new ArrayList<String>();
        this.workoutUniqueID = workoutUniqueID;
        this.date = date;

    }


    public String getWorkoutTitle() {
        return workoutTitle;
    }

    public void setWorkoutTitle(String title){
        this.workoutTitle = title;
    }

    public String getExerciseID(int position){
        return exerciseID.get(position);
    }

    public List<String> getExerciseIDList(){
        return exerciseID;
    }

    public List<String> getExerciseName() {
        return exerciseName;
    }


    public List<List<String>> getExerciseSets(){
        return singleExerciseDescription;
    }

    public void removeExerciseSets(int position){
        singleExerciseDescription.remove(position);
    }

    public void removeExerciseName(int position){
        exerciseName.remove(position);
    }

    public void removeExerciseID(int position){
        exerciseID.remove(position);
    }


    public List<String> getSingleExerciseDescription(int position) {
        return singleExerciseDescription.get(position);
    }

    public String getWorkoutUniqueID(){
        return workoutUniqueID;
    }

    public String getDate(){
        return date;
    }

    public int getNumOfExercises(){
        return exerciseName.size()+1;
    }


    public void addExerciseName(String exerciseName){
        this.exerciseName.add(exerciseName);
    }

    public void addExerciseSet(List<String> exerciseSet) {
        this.singleExerciseDescription.add(exerciseSet);
    }

    public void addExerciseID(String exerciseID){
        this.exerciseID.add(exerciseID);
    }

    public void updateExerciseName(String exerciseName, int position){
        this.exerciseName.set(position, exerciseName);
    }

    public void updateExerciseSet(List<String> exerciseSet, int position) {
        this.singleExerciseDescription.set(position, exerciseSet);
    }



}
