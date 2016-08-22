package com.example.davidyuan7536.workoutlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by davidyuan7536 on 8/16/2016.
 */
public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {

    Context context;
    private List<UserSearchRecyclerView> userSearchRecyclerView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView userName;
        TextView name;
        MLRoundedImageView userAvatar;
        Button relationBtn;


        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.searchCV);
            userName = (TextView)itemView.findViewById(R.id.userName);
            name = (TextView) itemView.findViewById(R.id.name);
            userAvatar = (MLRoundedImageView) itemView.findViewById(R.id.userAvatar);
            relationBtn = (Button) itemView.findViewById(R.id.relationBtn);
        }
    }


    UserSearchAdapter(Context context, List<UserSearchRecyclerView> userSearchRecyclerView){
        this.userSearchRecyclerView = userSearchRecyclerView;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return userSearchRecyclerView.size();
    }

    @Override
    public UserSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_search, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UserSearchAdapter.ViewHolder holder, final int position) {


        final UserSearchAdapter.ViewHolder finalHolder = holder;

        if(userSearchRecyclerView.get(position).getAvatar().matches("")){

        }
        else{
            Picasso.with(context).load(userSearchRecyclerView.get(position).getAvatar()).fit().centerInside().into(holder.userAvatar);
        }

        if(userSearchRecyclerView.get(position).getFollowing()){
            holder.relationBtn.setText("Unfollow");
        }
        else{
            holder.relationBtn.setText("Follow");
        }


        holder.userName.setText(userSearchRecyclerView.get(position).getUsername());
        holder.name.setText(userSearchRecyclerView.get(position).getName());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((UserSearchActivity)context).viewUserProfile(userSearchRecyclerView.get(position).getuID(), userSearchRecyclerView.get(position).getFollowing(), position);

            }
        });

        holder.relationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userSearchRecyclerView.get(position).getFollowing()){
                    ((UserSearchActivity)context).unfollowUser(userSearchRecyclerView.get(position).getuID());
                    Toast.makeText(context, "You have unfollowed " + userSearchRecyclerView.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                    finalHolder.relationBtn.setText("Follow");
                    userSearchRecyclerView.get(position).setFollowing(false);

                }
                else{
                    ((UserSearchActivity)context).followUser(userSearchRecyclerView.get(position).getuID());
                    Toast.makeText(context, "You are now following " + userSearchRecyclerView.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                    finalHolder.relationBtn.setText("Unfollow");
                    userSearchRecyclerView.get(position).setFollowing(true);
                }

            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
