package com.example.davidyuan7536.workoutlog;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by davidyuan7536 on 7/25/2016.
 */
public class SingleExerciseAdapter extends ArrayAdapter<SingleExercise> {


    public SingleExerciseAdapter(Context context, ArrayList<SingleExercise> singleExercise){
        super(context, 0, singleExercise);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SingleExercise singleExercise = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_exercise, parent, false);

            TextView weight = (TextView) convertView.findViewById(R.id.exerciseWeight);
            TextView weightUnit = (TextView) convertView.findViewById(R.id.exerciseWeightUnit);
            TextView rep = (TextView) convertView.findViewById(R.id.exerciseRep);
            TextView repUnit = (TextView) convertView.findViewById(R.id.exerciseRepUnit);

            weight.setText(singleExercise.getWeight());
            weightUnit.setText(singleExercise.getWeightUnit());
            rep.setText(singleExercise.getReps());
            repUnit.setText(singleExercise.getRepsUnit());


        }

        return convertView;

    }
}
