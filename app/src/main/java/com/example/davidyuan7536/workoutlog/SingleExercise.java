package com.example.davidyuan7536.workoutlog;

/**
 * Created by davidyuan7536 on 7/25/2016.
 */
public class SingleExercise
{
    private String weight, reps, weightUnit, repsUnit;
    private int singleExerciseId;


    public SingleExercise(int singleExerciseId, String weight, String weightUnit, String reps, String repsUnit) {
        this.singleExerciseId = singleExerciseId;
        this.repsUnit = repsUnit;
        this.reps = reps;
        this.weight = weight;
        this.weightUnit = weightUnit;
    }

    public String getWeight() {
        return weight;
    }

    public String getReps() {
        return reps;
    }

    public String getRepsUnit() {
        return repsUnit;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public int getSingleExerciseId() {
        return singleExerciseId;
    }
}
