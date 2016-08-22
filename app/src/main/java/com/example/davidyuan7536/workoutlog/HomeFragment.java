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
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment {

    RecyclerView rv;
    private List<HomeFeedRecycleViewUser> initializer;
    HomeFeedRecycleViewAdapter adapter;

    Button datePicker;
    ImageButton previousDate;
    ImageButton nextDate;
    LinearLayout loading;

    int mYear;
    int mMonth;
    int mDay;

    boolean reset;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
//        initializeData();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initializer = new ArrayList<HomeFeedRecycleViewUser>();



        ////////////////////////////////////////////////////////////////////////////////////////////
        //DATE HANDLER//////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        datePicker = (Button) view.findViewById(R.id.homeDatePicker);
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

        previousDate = (ImageButton) view.findViewById(R.id.homePreviousDate);
        previousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay);
                cal.add(Calendar.DAY_OF_MONTH, -1);

                ((HomeActivity)getActivity()).onDatePicked(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), "home");

                setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


            }
        });

        nextDate = (ImageButton) view.findViewById(R.id.homeNextDate);
        nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                cal.set(mYear, mMonth, mDay);
                cal.add(Calendar.DAY_OF_MONTH, 1);


                ((HomeActivity)getActivity()).onDatePicked(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), "home");

                setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////


        loading = (LinearLayout)view.findViewById(R.id.homeLoadingAnimation);

        rv = (RecyclerView) view.findViewById(R.id.homeFeedRecycleView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener(llm) {


            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                customLoadMoreDataFromApi(page);
//                Log.d("WorkoutLog","scrolling");
            }
        });


        adapter = new HomeFeedRecycleViewAdapter(getContext(), initializer);

        rv.setAdapter(adapter);

        return view;
    }



    public void customLoadMoreDataFromApi(int page) {

    }

    public void clearInitializer(){



        initializer = new ArrayList<HomeFeedRecycleViewUser>();
//        adapter.notifyDataSetChanged();
        Log.d("WorkoutLog", "initializer cleared");
    }

    public void clearData() {
        int size = this.initializer.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.initializer.remove(0);
            }


        }
        adapter.notifyDataSetChanged();
    }

    public void addToInitializer(HomeFeedRecycleViewUser temp){

        for (int i = 0; i < initializer.size(); i++) {
            // if the element you are looking at is smaller than x,
            // go to the next element
            if (Integer.parseInt(initializer.get(i).getTimestamp()) < Integer.parseInt(temp.getTimestamp())){
                continue;
            }
            initializer.add(i, temp);
            adapter.notifyItemInserted(i);
            return;
        }

        initializer.add(temp);
        adapter.notifyItemInserted(initializer.size()-1);

        loading.setVisibility(View.GONE);

    }

    public void stopLoadingAnimation(){
        loading.setVisibility(View.GONE);
    }

    public void setDate(int year, int month, int day){

        initializer = null;
        initializer = new ArrayList<HomeFeedRecycleViewUser>();
        adapter = null;
        adapter = new HomeFeedRecycleViewAdapter(getContext(), initializer);
        adapter.notifyDataSetChanged();
        rv.setAdapter(null);
        rv.setAdapter(adapter);

        loading.setVisibility(View.VISIBLE);


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

        OnHomePickerDatePicked mCallback;


        public interface OnHomePickerDatePicked {

            void onDatePicked(int year, int month, int day, String fragment);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);


            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {
                mCallback = (OnHomePickerDatePicked) (Activity) context;
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
                mCallback.onDatePicked(year, month, day, "home");
            }


        }
    }


}
