package com.example.davidyuan7536.workoutlog;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by davidyuan7536 on 7/26/2016.
 */
public class HomeFeedRecycleViewAdapter extends RecyclerView.Adapter<HomeFeedRecycleViewAdapter.ViewHolder>{

    Context context;
    private List<HomeFeedRecycleViewUser> homeFeedRecycleViewUser;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        MLRoundedImageView avatar;
        TextView userName;
        TextView workoutTitle;
        LinearLayout exercisesContainer;
        LinearLayout singleExerciseContainer;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            avatar = (MLRoundedImageView)itemView.findViewById(R.id.homeFeedUserAvatar);
            userName = (TextView)itemView.findViewById(R.id.homeFeedUserName);
            workoutTitle = (TextView)itemView.findViewById(R.id.homeFeedWorkoutTitle);
            exercisesContainer = (LinearLayout)itemView.findViewById(R.id.homeFeedExercisesContainer);



        }
    }

    HomeFeedRecycleViewAdapter(Context context, List<HomeFeedRecycleViewUser> homeFeedRecycleViewUser){
        this.homeFeedRecycleViewUser = homeFeedRecycleViewUser;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return homeFeedRecycleViewUser.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_home_feed, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(homeFeedRecycleViewUser.get(position).getAvatar() != null || homeFeedRecycleViewUser.get(position).getAvatar() != ""){
            Picasso.with(context).load(homeFeedRecycleViewUser.get(position).getAvatar()).into(holder.avatar);
        }
        holder.userName.setText(homeFeedRecycleViewUser.get(position).getUserName());
        holder.workoutTitle.setText(homeFeedRecycleViewUser.get(position).getWorkoutTitle());

        RecyclerView.LayoutParams exercisesParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams linearLayoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        LinearLayout.LayoutParams linearLayoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);



        float density = holder.exercisesContainer.getResources().getDisplayMetrics().density;
        int paddingPixel10 = (int)(10 * density);

        int currentExercise = 1;

        for (String exerciseName : homeFeedRecycleViewUser.get(position).getExerciseName()) {
            TextView tv = new TextView(holder.exercisesContainer.getContext());
            tv.setLayoutParams(exercisesParams);
            tv.setText(exerciseName);
            tv.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
            tv.setBackgroundColor(Color.parseColor("#c8e8ff"));

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            holder.exercisesContainer.addView(tv);

            LinearLayout ll = new LinearLayout(holder.exercisesContainer.getContext());
            ll.setLayoutParams(exercisesParams);
            ll.setOrientation(LinearLayout.VERTICAL);


            holder.exercisesContainer.addView(ll);



            int currentSet = 1;

            for (String singleExerciseDescription : homeFeedRecycleViewUser.get(position).getSingleExerciseDescription(currentExercise-1)) {

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

    public void clearAll(){
        int size = this.homeFeedRecycleViewUser.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.homeFeedRecycleViewUser.get(0).deleteExerciseName();
                this.homeFeedRecycleViewUser.get(0).deleteSingleExerciseDescription();
                this.homeFeedRecycleViewUser.remove(0);
            }


        }
        this.notifyDataSetChanged();
    }
}
