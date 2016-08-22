package com.example.davidyuan7536.workoutlog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidyuan7536 on 8/12/2016.
 */
public class ProfileWorkoutAdapter extends RecyclerView.Adapter<ProfileWorkoutAdapter.ViewHolder> {

    Context context;
    private List<ProfileWorkoutRecyclerView> profileWorkoutRecyclerView;
    Boolean editable;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView workoutTitle;
        ImageButton editWorkout;
        LinearLayout exerciseContainer;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.profileWorkoutCV);
            workoutTitle = (TextView)itemView.findViewById(R.id.profileWorkoutTitle);
            editWorkout = (ImageButton) itemView.findViewById(R.id.profileEditWorkout);
            exerciseContainer = (LinearLayout) itemView.findViewById(R.id.profileWorkoutExercisesContainer);


        }
    }

    ProfileWorkoutAdapter(Context context, List<ProfileWorkoutRecyclerView> profileWorkoutRecyclerView, Boolean editable){
        this.profileWorkoutRecyclerView = profileWorkoutRecyclerView;
        this.context = context;
        this.editable = editable;
    }

    @Override
    public int getItemCount() {
        return profileWorkoutRecyclerView.size();
    }

    @Override
    public ProfileWorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_profile_workout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProfileWorkoutAdapter.ViewHolder holder, final int position) {

        holder.workoutTitle.setText(profileWorkoutRecyclerView.get(position).getWorkoutName());

        if(editable){
            holder.editWorkout.setOnClickListener(new EditWorkoutOnclick(profileWorkoutRecyclerView.get(position).getDate()));
        }
        else{
            ((ViewGroup)holder.editWorkout.getParent()).removeView(holder.editWorkout);
        }




        RecyclerView.LayoutParams exercisesParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams exercisesParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams linearLayoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout.LayoutParams linearLayoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);




        int currentExercise = 1;
        float density = holder.exerciseContainer.getResources().getDisplayMetrics().density;
        final int paddingPixel10 = (int)(10 * density);
        final int paddingPixel20 = (int)(20 * density);


        for (String exerciseName : profileWorkoutRecyclerView.get(position).getExerciseName()) {


            TextView tv = new TextView(context);
            tv.setLayoutParams(exercisesParams2);
            tv.setText(exerciseName);
            tv.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
            tv.setBackgroundColor(Color.parseColor("#c8e8ff"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tv.setId(0 + currentExercise);
            holder.exerciseContainer.addView(tv);


            LinearLayout ll = new LinearLayout(context);
            ll.setLayoutParams(exercisesParams);
            ll.setOrientation(LinearLayout.VERTICAL);


            holder.exerciseContainer.addView(ll);

            int currentSet = 1;


            for (String singleExerciseDescription : profileWorkoutRecyclerView.get(position).getSingleExerciseDescription(currentExercise-1)) {


                LinearLayout ll2 = new LinearLayout(context);
                ll2.setLayoutParams(exercisesParams);
                ll2.setOrientation(LinearLayout.HORIZONTAL);
                ll.addView(ll2);

                TextView setTxt = new TextView(context);
                setTxt.setLayoutParams(linearLayoutParams1);
                setTxt.setText("Set "+ currentSet+":");
                setTxt.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                setTxt.setSingleLine(true);
                setTxt.setEllipsize(TextUtils.TruncateAt.END);
                ll2.addView(setTxt);

                TextView tv2 = new TextView(context);
                tv2.setLayoutParams(linearLayoutParams2);
                tv2.setText(singleExerciseDescription);
                tv2.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                tv2.setId(0 + currentSet);
                ll2.addView(tv2);

                currentSet++;
            }

            currentExercise++;
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class EditWorkoutOnclick implements View.OnClickListener
    {

        String workoutID;
        String workoutDate;

        public EditWorkoutOnclick(String workoutDate) {
            this.workoutDate = workoutDate;
        }

        @Override
        public void onClick(View v)
        {
            String yearS = workoutDate.substring(0, 4);
            String monthS = workoutDate.substring(4, 6);
            String dayS = workoutDate.substring(6);

            Log.d("WorkoutLog", yearS + monthS + dayS);

            ((ProfileActivity)context).editWorkout(Integer.parseInt(yearS), Integer.parseInt(monthS), Integer.parseInt(dayS));

        }

    };


}
