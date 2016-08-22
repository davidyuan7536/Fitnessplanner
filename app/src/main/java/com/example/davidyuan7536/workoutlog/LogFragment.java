package com.example.davidyuan7536.workoutlog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class LogFragment extends Fragment {

    RecyclerView rv;
    private List<LogFeedRecyclerViewExercise> initalizer;

    LogFeedRecyclerViewAdapter adapter;
    Button addWorkout;
    Button datePicker;
    ImageButton previousDate;
    ImageButton nextDate;

    int mYear;
    int mMonth;
    int mDay;

    public LogFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        initalizer = new ArrayList<LogFeedRecyclerViewExercise>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_log, container, false);

        ////////////////////////////////////////////////////////////////////////////////////////////
        //DATE HANDLER//////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        datePicker = (Button) view.findViewById(R.id.logFeedDatePicker);
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

        previousDate = (ImageButton) view.findViewById(R.id.logFeedPreviousDate);
        previousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay);
                cal.add(Calendar.DAY_OF_MONTH, -1);

                ((HomeActivity)getActivity()).onDatePicked(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), "log");

                setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


            }
        });

        nextDate = (ImageButton) view.findViewById(R.id.logFeedNextDate);
        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay);
                cal.add(Calendar.DAY_OF_MONTH, 1);


                ((HomeActivity)getActivity()).onDatePicked(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), "log");

                setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////



        rv = (RecyclerView) view.findViewById(R.id.logRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);


        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {


            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);

            }
        });


        adapter = new LogFeedRecyclerViewAdapter(getActivity(), initalizer);

        rv.setAdapter(adapter);


        addWorkout = (Button) view.findViewById(R.id.logAddWorkoutBtn);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String leadingZeroMonth = String.format("%02d", mMonth);
                String leadingZeroDay = String.format("%02d", mDay);
                String date = ""+mYear + leadingZeroMonth + leadingZeroDay;


                String uniqueID = ((HomeActivity)getActivity()).saveWorkout("Workout Title (Click to Change)", date);

                initalizer.add(new LogFeedRecyclerViewExercise("Workout Title (Click to Change)", uniqueID, date));
                rv.setAdapter(null);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
//
//                adapter.notifyItemInserted(initalizer.size()-1);

            }
        });


        ((HomeActivity)getActivity()).onDatePicked(mYear, mMonth, mDay, "log");

        return view;
    }


    public void customLoadMoreDataFromApi(int page) {

    }



    public void deleteWorkout(int position){
        initalizer.remove(position);
        rv.setAdapter(null);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void initializeEmptyWorkout(){
        initalizer = null;
        initalizer = new ArrayList<LogFeedRecyclerViewExercise>();
        adapter = null;
        adapter = new LogFeedRecyclerViewAdapter(getActivity(), initalizer);
        rv.setAdapter(null);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void initializeAddWorkout(String workoutTitle, String workoutID, String date){

        LogFeedRecyclerViewExercise initialize = new LogFeedRecyclerViewExercise(workoutTitle, workoutID, date);
        initalizer.add(initialize);
        rv.setAdapter(null);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        adapter.notifyItemInserted(initalizer.size()-1);
    }

    public void initializeAddWorkout2(String workoutTitle, String workoutID, String date, boolean reset){
        if(reset){
            initalizer = null;
            initalizer = new ArrayList<LogFeedRecyclerViewExercise>();
            adapter = null;
            adapter = new LogFeedRecyclerViewAdapter(getActivity(), initalizer);
        }
        LogFeedRecyclerViewExercise initialize = new LogFeedRecyclerViewExercise(workoutTitle, workoutID, date);
        initalizer.add(initialize);
        rv.setAdapter(null);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        adapter.notifyItemInserted(initalizer.size()-1);
    }


    public void addExerciseToWorkout(String exerciseName, List<String> exerciseSets, String workoutID, String exerciseID){

        for(int x = 0; x!= initalizer.size(); x++){
            if(initalizer.get(x).getWorkoutUniqueID().matches(workoutID)){
                initalizer.get(x).addExerciseName(exerciseName);
                initalizer.get(x).addExerciseSet(exerciseSets);
                initalizer.get(x).addExerciseID(exerciseID);
                rv.setAdapter(null);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                return;
            }
        }

    }

    public void updateExercise(String exerciseName, List<String> exerciseSets, String workoutID, String exerciseID){


        for(int x = 0; x!= initalizer.size(); x++){
            if(initalizer.get(x).getWorkoutUniqueID().matches(workoutID)){

                for(int y = 0; y!= initalizer.get(x).getExerciseIDList().size(); y++){
                    if(initalizer.get(x).getExerciseIDList().get(y).matches(exerciseID)){

                        initalizer.get(x).updateExerciseName(exerciseName, y);
                        initalizer.get(x).updateExerciseSet(exerciseSets, y);
                        rv.setAdapter(null);
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Log.d("WorkoutLog", ""+initalizer.get(x).getExerciseName().size());

                    }
                }
            }
        }

    }

    public void deleteExercise(String workoutID, String exerciseID){
        for(int x = 0; x!= initalizer.size(); x++){
            if(initalizer.get(x).getWorkoutUniqueID().matches(workoutID)){
                for(int y = 0; y!= initalizer.get(x).getExerciseIDList().size(); y++){
                    Log.d("WorkoutLog", initalizer.get(x).getExerciseID(y));
                    if(initalizer.get(x).getExerciseID(y).matches(exerciseID)){
                        initalizer.get(x).removeExerciseName(y);
                        initalizer.get(x).removeExerciseSets(y);
                        initalizer.get(x).removeExerciseID(y);
                        adapter.exerciseDeleted(x, y);
                        rv.setAdapter(null);
                        rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        return;


                    }
                }
            }
        }
    }

    public void updateWorkout(String workoutID, String workoutTitle){
        for(int x = 0; x!= initalizer.size(); x++){
            if(initalizer.get(x).getWorkoutUniqueID().matches(workoutID)){
                initalizer.get(x).setWorkoutTitle(workoutTitle);
                rv.setAdapter(null);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return;
            }
        }

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
            void onDatePicked(int year, int month, int day, String fragment);
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
                mCallback.onDatePicked(year, month, day, "log");
            }


        }
    }




}
