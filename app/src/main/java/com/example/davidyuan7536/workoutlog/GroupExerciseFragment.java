package com.example.davidyuan7536.workoutlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupExerciseFragment extends Fragment {

    private static boolean exerciseHidden = true;
    private TextView groupExerciseName;
    private LinearLayout groupExerciseFragContainer;
    private TextView groupExerciseComment;


    public GroupExerciseFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.group_exercise, container, false);


        groupExerciseName = (TextView) view.findViewById(R.id.groupExerciseName);
        groupExerciseFragContainer = (LinearLayout) view.findViewById(R.id.groupExerciseFragContainer);
        groupExerciseComment = (TextView) view.findViewById(R.id.groupExerciseComment);

        groupExerciseFragContainer.setVisibility(View.GONE);
        groupExerciseComment.setVisibility(View.GONE);

        Fragment exerciseFragment = new SingleExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("exerciseWeight", "100");
        bundle.putString("exerciseWeightUnit", "lbs");
        bundle.putString("exerciseRep", "10");
        bundle.putString("exerciseRepUnit", "reps");
        exerciseFragment.setArguments(bundle);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.groupExerciseFragContainer, exerciseFragment).commit();






//        groupExerciseName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(exerciseHidden){
//                    groupExerciseName.setBackgroundResource(R.color.exerciseShown);
//                    groupExerciseFragContainer.setVisibility(View.VISIBLE);
//                    groupExerciseComment.setVisibility(View.VISIBLE);
//                    exerciseHidden = false;
//                }else{
//                    groupExerciseName.setBackgroundResource(R.color.exerciseHidden);
//                    groupExerciseFragContainer.setVisibility(View.GONE);
//                    groupExerciseComment.setVisibility(View.GONE);
//                    exerciseHidden = true;
//                }
//            }
//        });


        return view;
    }

}
