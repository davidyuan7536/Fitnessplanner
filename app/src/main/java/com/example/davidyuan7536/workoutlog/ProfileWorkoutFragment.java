package com.example.davidyuan7536.workoutlog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class ProfileWorkoutFragment extends Fragment {

    Context context;

    RecyclerView rv;
    private List<ProfileWorkoutRecyclerView> initalizer;
    ProfileWorkoutAdapter adapter;

    int mYear;
    int mMonth;
    int mDay;

    Button datePicker;
    ImageButton previousDate;
    ImageButton nextDate;



    MLRoundedImageView userAvatar;
    TextView userName;
    TextView userQuote;


    public ProfileWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        initalizer = new ArrayList<ProfileWorkoutRecyclerView>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_profle_workout, container, false);

        context = getContext();


        ////////////////////////////////////////////////////////////////////////////////////////////
        //DATE HANDLER//////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        datePicker = (Button) view.findViewById(R.id.profileDatePicker);
        datePicker.setText(mMonth+"/"+mDay+"/"+mYear + " (Today)");
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("year", mYear);
                bundle.putInt("month", mMonth);
                bundle.putInt("day", mDay);

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setArguments(bundle);
                newFragment.show(getFragmentManager(), "datePicker");

            }
        });

        previousDate = (ImageButton) view.findViewById(R.id.profilePreviousDate);
        previousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay);
                cal.add(Calendar.DAY_OF_MONTH, -1);

                ((ProfileActivity)getActivity()).onDatePicked(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


            }
        });

        nextDate = (ImageButton) view.findViewById(R.id.profileNextDate);
        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay);
                cal.add(Calendar.DAY_OF_MONTH, 1);

                ((ProfileActivity)getActivity()).onDatePicked(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

                setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////



        rv = (RecyclerView) view.findViewById(R.id.profileWorkoutRecycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        initialize();

        userAvatar = (MLRoundedImageView) view.findViewById(R.id.userAvatar);
        userName = (TextView) view.findViewById(R.id.profileUserName);
        userQuote = (TextView) view.findViewById(R.id.profileUserQuote);

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfileActivity)context).saveUserAvatar();
            }
        });




        return view;
    }

    public void initialize(){

//        ((ProfileActivity)context).onDatePicked(mYear, mMonth, mDay);
    }


    public void setUserAvatar(String link){
        Picasso.with(context).load(link).fit().centerInside().into(userAvatar);
    }


    public void initializeEmptyWorkout(){
        initalizer = null;
        initalizer = new ArrayList<ProfileWorkoutRecyclerView>();
        adapter = null;

        adapter = new ProfileWorkoutAdapter(getActivity(), initalizer, ((ProfileActivity)context).personalProfile());
        rv.setAdapter(null);
        rv.setAdapter(adapter);
    }


    public void addWorkout(String workoutName, String workoutID, String date, boolean reset){

        if(reset){
            initalizer = null;
            initalizer = new ArrayList<ProfileWorkoutRecyclerView>();
            adapter = null;
            adapter = new ProfileWorkoutAdapter(getActivity(), initalizer, ((ProfileActivity)context).personalProfile());
        }

        List<String> exercises = new ArrayList<String>();
        List<String> exerciseID = new ArrayList<String>();
        List<List<String>> singleExercise = new ArrayList<List<String>>();


        ProfileWorkoutRecyclerView temp = new ProfileWorkoutRecyclerView(workoutName, workoutID, exercises, exerciseID, singleExercise, date );
        initalizer.add(temp);
        rv.setAdapter(null);
        rv.setAdapter(adapter);
    }


    public void addExerciseToWorkout(String exerciseName, String exerciseID, List<String> exerciseSets, String workoutID){
        for(int x = 0; x!= initalizer.size(); x++){
            if(initalizer.get(x).getWorkoutID().matches(workoutID)){
                initalizer.get(x).addExerciseName(exerciseName);
                initalizer.get(x).addExerciseID(exerciseID);
                initalizer.get(x).addSingleExerciseDescription(exerciseSets);
                adapter.notifyItemChanged(x);
                return;
            }
        }
    }

    public void updateUserInfo(String name, String quote){
        userName.setText(name);
        userQuote.setText(quote);
    }

    public void updateName(String name){
        userName.setText(name);
    }

    public void updateUserQuote(String quote){
        userQuote.setText(quote);
    }


    public void setDate(int year, int month, int day){
        mYear = year;
        mMonth = month;
        mDay = day;

        Calendar cNext = Calendar.getInstance();
        cNext.add(Calendar.DAY_OF_MONTH, 1);

        Calendar cPrev = Calendar.getInstance();
        cPrev.add(Calendar.DAY_OF_MONTH, -1);

        Calendar c = Calendar.getInstance();

        if(cNext.get(Calendar.YEAR) == mYear && cNext.get(Calendar.MONTH) == month && cNext.get(Calendar.DAY_OF_MONTH) == mDay){
            datePicker.setText(mMonth+"/"+mDay+"/"+mYear + " (Tomorrow)");
        }
        else if(cPrev.get(Calendar.YEAR) == mYear && cPrev.get(Calendar.MONTH) == month && cPrev.get(Calendar.DAY_OF_MONTH) == mDay){
            datePicker.setText(mMonth+"/"+mDay+"/"+mYear + " (Yesterday)");
        }
        else if(c.get(Calendar.YEAR) == mYear && c.get(Calendar.MONTH) == month && c.get(Calendar.DAY_OF_MONTH) == mDay){
            datePicker.setText(mMonth+"/"+mDay+"/"+mYear + " (Today)");
        }
        else{
            datePicker.setText(mMonth+"/"+mDay+"/"+mYear);
        }




    }



    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        OnLogDatePickerDatePicked mCallback;

        public interface OnLogDatePickerDatePicked {
            void onDatePicked(int year, int month, int day);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);


            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (OnLogDatePickerDatePicked) (Activity) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker


            Bundle bundle=getArguments();

            int year = bundle.getInt("year");
            int month = bundle.getInt("month");
            int day = bundle.getInt("day");
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            if(view.isShown()){
                mCallback.onDatePicked(year, month, day);
            }


        }
    }


}
