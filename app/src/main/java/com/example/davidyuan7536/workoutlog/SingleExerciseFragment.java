package com.example.davidyuan7536.workoutlog;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SingleExerciseFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.single_exercise, null);

        TextView weight = (TextView) v.findViewById(R.id.exerciseWeight);
        TextView weightUnit = (TextView) v.findViewById(R.id.exerciseWeightUnit);
        TextView rep = (TextView) v.findViewById(R.id.exerciseRep);
        TextView repUnit = (TextView) v.findViewById(R.id.exerciseRepUnit);

        String exerciseWeight = getArguments().getString("exerciseWeight");
        String exerciseWeightUnit = getArguments().getString("exerciseWeightUnit");
        String exerciseRep = getArguments().getString("exerciseRep");
        String exerciseRepUnit = getArguments().getString("exerciseRepUnit");

        weight.setText(exerciseWeight);
        weightUnit.setText(exerciseWeightUnit);
        rep.setText(exerciseRep);
        repUnit.setText(exerciseRepUnit);


        return v;
    }
}
