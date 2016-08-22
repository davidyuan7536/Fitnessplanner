package com.example.davidyuan7536.workoutlog;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidyuan7536 on 7/28/2016.
 */
public class LogFeedRecyclerViewAdapter extends RecyclerView.Adapter<LogFeedRecyclerViewAdapter.ViewHolder> {

    private List<LogFeedRecyclerViewExercise> logFeedRecyclerViewExercise;
    private Context context;


    List<List<ImageButton>> exerciseListEdit;
    List<List<TextView>> exerciseListName;
    List<List<String>> exerciseListExerciseID;
    List<List<List<TextView>>> exerciseListSets;
    List<List<String>> exerciseListWorkoutID;

    AlertDialog.Builder alert;
    int curPos;
    String alertText;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView workoutTitle;
        LinearLayout exercisesContainer;
        Button addExercise;
        ImageButton deleteWorkout;


        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.logFeedCv);
            workoutTitle = (TextView) itemView.findViewById(R.id.logFeedWorkoutTitle);
            exercisesContainer = (LinearLayout)itemView.findViewById(R.id.logFeedExercisesContainer);
            addExercise = (Button) itemView.findViewById(R.id.logFeedAddExercise);
            deleteWorkout = (ImageButton) itemView.findViewById(R.id.logFeedDeleteWorkout);




        }
    }

    LogFeedRecyclerViewAdapter(final Activity context, final List<LogFeedRecyclerViewExercise> logFeedRecyclerViewExercise){
        this.logFeedRecyclerViewExercise = logFeedRecyclerViewExercise;
        this.context = context;
        exerciseListEdit = new ArrayList<List<ImageButton>>();
        exerciseListName = new ArrayList<List<TextView>>();
        exerciseListSets = new ArrayList<List<List<TextView>>>();
        exerciseListWorkoutID = new ArrayList<List<String>>();
        exerciseListExerciseID = new ArrayList<List<String>>();
        curPos = 0;

        alert = new AlertDialog.Builder(context);
        alert.setTitle("New Workout Title");



        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(alertText.matches("")){
                    Toast.makeText(context, "Workout title must not be empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    ((HomeActivity)context).updateWorkout(logFeedRecyclerViewExercise.get(curPos).getWorkoutUniqueID(), alertText);
                    ((HomeActivity)context).onUpdateWorkout(logFeedRecyclerViewExercise.get(curPos).getWorkoutUniqueID(), alertText);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return logFeedRecyclerViewExercise.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_log_feed, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        List<ImageButton> tempELE = new ArrayList<ImageButton>();
        exerciseListEdit.add(tempELE);

        List<TextView> tempELN = new ArrayList<TextView>();
        exerciseListName.add(tempELN);

        List<List<TextView>> tempELS = new ArrayList<List<TextView>>();
        exerciseListSets.add(tempELS);

        List<String> tempELWID = new ArrayList<String>();
        exerciseListWorkoutID.add(tempELWID);

        List<String> tempELEID = new ArrayList<String>();
        exerciseListExerciseID.add(tempELEID);


        float density = holder.exercisesContainer.getResources().getDisplayMetrics().density;
        final int paddingPixel10 = (int)(10 * density);
        final int paddingPixel20 = (int)(20 * density);

        holder.workoutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curPos = position;
                alertText = "";

                alert.setView(null);

                LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                final LinearLayout ll = new LinearLayout(context);
                ll.setLayoutParams(linearLayoutParams);
                ll.setPadding(paddingPixel20, paddingPixel10, paddingPixel20, paddingPixel10);
                final EditText input = (EditText) View.inflate(view.getContext(), R.layout.material_factory_edit_text, null);
                input.setLayoutParams(linearLayoutParams);
                input.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                ll.addView(input);
                alert.setView(ll);
                alert.show();

                input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        alertText = editable.toString();
                    }
                });
            }
        });




        holder.deleteWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((HomeActivity)context).deleteWorkout(logFeedRecyclerViewExercise.get(position).getWorkoutUniqueID().toString(), logFeedRecyclerViewExercise.get(position).getDate());

                ((HomeActivity)context).onDeleteWorkout(position);

                exerciseListEdit.remove(position);
                exerciseListName.remove(position);
                exerciseListExerciseID.remove(position);
                exerciseListSets.remove(position);
                exerciseListWorkoutID.remove(position);


//                logFeedRecyclerViewExercise.remove(position);
//
//
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, logFeedRecyclerViewExercise.size());
            }
        });



        holder.addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle data = new Bundle();
                data.putString("workoutID", logFeedRecyclerViewExercise.get(position).getWorkoutUniqueID().toString());
                data.putString("workoutDate", logFeedRecyclerViewExercise.get(position).getDate());
                data.putString("exerciseNum", "" + logFeedRecyclerViewExercise.get(position).getNumOfExercises());

                FragmentTransaction trans = ((Activity)context).getFragmentManager().beginTransaction();
                LogExerciseFragment lef = new LogExerciseFragment();
                lef.setArguments(data);
                trans.replace(R.id.root_frag_log, lef);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();

            }
        });


        holder.workoutTitle.setText(logFeedRecyclerViewExercise.get(position).getWorkoutTitle());

        if(exerciseListWorkoutID.size() == position){
            exerciseListWorkoutID.get(position).add(logFeedRecyclerViewExercise.get(position).getWorkoutUniqueID());
        }


        RecyclerView.LayoutParams exercisesParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams linearLayoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout.LayoutParams linearLayoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);

        LinearLayout.LayoutParams linearLayoutParamsEdit = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.2f);
        LinearLayout.LayoutParams linearLayoutParamsName = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);




        int currentExercise = 1;


        for (String exerciseName : logFeedRecyclerViewExercise.get(position).getExerciseName()) {




            LinearLayout exerciseTitle = new LinearLayout(holder.exercisesContainer.getContext());
            exerciseTitle.setLayoutParams(exercisesParams);
            exerciseTitle.setOrientation(LinearLayout.HORIZONTAL);
            holder.exercisesContainer.addView(exerciseTitle);

            TextView tv = new TextView(holder.exercisesContainer.getContext());
            tv.setLayoutParams(linearLayoutParamsName);
            tv.setText(exerciseName);
            tv.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
            tv.setBackgroundColor(Color.parseColor("#c8e8ff"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setId(0 + currentExercise);
            exerciseTitle.addView(tv);
            if(exerciseListName.get(position).size() == currentExercise-1){
                exerciseListName.get(position).add(tv);
                exerciseListExerciseID.get(position).add(logFeedRecyclerViewExercise.get(position).getExerciseID(currentExercise-1));
//                Log.d("WorkoutLog", "exerciseListName: " + tv.getText().toString());
//                Log.d("WorkoutLog", "exerciseListExerciseID: " +logFeedRecyclerViewExercise.get(position).getExerciseID(currentExercise-1));
            }
            ImageButton editExercise = new ImageButton(holder.exercisesContainer.getContext(),null, R.style.AppTheme);
            editExercise.setLayoutParams(linearLayoutParamsEdit);
            editExercise.setBackgroundColor(Color.parseColor("#c8e8ff"));
            editExercise.setImageResource(R.drawable.ic_edit_black_24dp);
            editExercise.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
            editExercise.setId(0 + currentExercise);
            MyEditExerciseOnClick editExerciseOnClick = new MyEditExerciseOnClick(logFeedRecyclerViewExercise.get(position).getWorkoutUniqueID(), position, holder);
            editExercise.setOnClickListener(editExerciseOnClick);
            exerciseTitle.addView(editExercise);
            if(exerciseListEdit.get(position).size() == currentExercise-1){
                exerciseListEdit.get(position).add(editExercise);
//                Log.d("WorkoutLog", "exerciseListEdit: new edit added");
            }


            LinearLayout ll = new LinearLayout(holder.exercisesContainer.getContext());
            ll.setLayoutParams(exercisesParams);
            ll.setOrientation(LinearLayout.VERTICAL);


            holder.exercisesContainer.addView(ll);

            int currentSet = 1;

            boolean secondIteration = false;

            for (String singleExerciseDescription : logFeedRecyclerViewExercise.get(position).getSingleExerciseDescription(currentExercise-1)) {



                LinearLayout ll2 = new LinearLayout(holder.exercisesContainer.getContext());
                ll2.setLayoutParams(exercisesParams);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll.addView(ll2);

                TextView setTxt = new TextView(holder.exercisesContainer.getContext());
                setTxt.setLayoutParams(linearLayoutParams1);
                setTxt.setText("Set "+ currentSet+":");
                setTxt.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setTxt.setSingleLine(true);
                setTxt.setEllipsize(TextUtils.TruncateAt.END);
                ll2.addView(setTxt);

                TextView tv2 = new TextView(holder.exercisesContainer.getContext());
                tv2.setLayoutParams(linearLayoutParams2);
                tv2.setText(singleExerciseDescription);
                tv2.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                tv2.setId(0 + currentSet);
                ll2.addView(tv2);

//                Log.d("WorkoutLog", "exercise: " +currentExercise + " set: " + currentSet + " exerciseListSet Size: " + exerciseListSets.get(position).size());

                if(exerciseListSets.get(position).size() == currentExercise-1 || secondIteration){

                    if(secondIteration){
                        exerciseListSets.get(position).get(currentExercise-1).add(tv2);
                    }
                    else{
                        List<TextView> tempList = new ArrayList<TextView>();
                        exerciseListSets.get(position).add(tempList);
                        exerciseListSets.get(position).get(currentExercise-1).add(tv2);
                    }
                    secondIteration = true;

//                    Log.d("dWorkoutLog", "exercise: " +currentExercise + " set: " + currentSet + " text: " + tv2.getText().toString());
                }

                currentSet++;
            }

            currentExercise++;
        }


    }


    public void exerciseDeleted(int position, int exercisePosition){

        exerciseListEdit.get(position).remove(exercisePosition);
        exerciseListName.get(position).remove(exercisePosition);
        exerciseListExerciseID.get(position).remove(exercisePosition);
        exerciseListSets.get(position).remove(exercisePosition);


    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class MyEditExerciseOnClick implements View.OnClickListener
    {
        String workoutID;
        int position;
        ViewHolder holder;

        public MyEditExerciseOnClick(String workoutID, int position, ViewHolder holder) {
            this.workoutID = workoutID;
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onClick(View view) {

            String eName = exerciseListName.get(position).get(view.getId()-1).getText().toString();
            ArrayList<String> eSets = new ArrayList<String>();
            for (TextView set: exerciseListSets.get(position).get(view.getId()-1)){
                eSets.add(set.getText().toString());
            }

            String workoutID = this.workoutID;
            String exerciseID = exerciseListExerciseID.get(position).get(view.getId()-1);



            Bundle bundle = new Bundle();
            bundle.putString("exerciseName", eName);
            bundle.putStringArrayList("exerciseSets", eSets);
            bundle.putString("workoutID", workoutID);
            bundle.putString("exerciseID", exerciseID);

            FragmentTransaction trans = ((Activity)context).getFragmentManager().beginTransaction();
            LogExerciseFragment lef = new LogExerciseFragment();
            lef.setArguments(bundle);
            trans.replace(R.id.root_frag_log, lef);
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            trans.addToBackStack(null);
            trans.commit();

        }
    }


}
