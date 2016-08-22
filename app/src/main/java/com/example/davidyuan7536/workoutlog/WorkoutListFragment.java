package com.example.davidyuan7536.workoutlog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutListFragment extends Fragment {


    TextView userNameTxt;
    TextView workoutTitleTxt;
    ImageView userAvatarImg;

    public WorkoutListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.workout_list, null);

        userNameTxt = (TextView) v.findViewById(R.id.workoutListUserName);
        workoutTitleTxt = (TextView) v.findViewById(R.id.workoutListWorkoutTitle);
        userAvatarImg = (ImageView) v.findViewById(R.id.workoutListAvatar);

        String userName = getArguments().getString("userName");
        String workoutTitle = getArguments().getString("workoutTitle");

        userNameTxt.setText(userName);
        workoutTitleTxt.setText(workoutTitle);

        Fragment exerciseFragment = new GroupExerciseFragment();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.workoutListFragContainer, exerciseFragment).commit();

        return v;
    }

}
