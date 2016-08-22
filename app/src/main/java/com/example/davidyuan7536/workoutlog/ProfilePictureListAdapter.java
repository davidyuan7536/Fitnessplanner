package com.example.davidyuan7536.workoutlog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by davidyuan7536 on 8/8/2016.
 */
public class ProfilePictureListAdapter extends RecyclerView.Adapter<ProfilePictureListAdapter.ViewHolder>{


    Context context;
    private List<ProfilePictureRecyclerView> profilePictureRecyclerView;
    Boolean editable;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView userName;
        MLRoundedImageView userAvatar;
        ImageButton pictureOptions;
        SquareImageView picture;
        TextView pictureCaption;
        TextView pictureDate;
        LinearLayout pictureCaptionEditWrapper;
        EditText pictureCaptionEdit;
        ImageButton captionEditConfirm;
        ImageButton captionEditCancel;



        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.profilePictureCV);
            userName = (TextView)itemView.findViewById(R.id.profilePictureUserName);
            userAvatar = (MLRoundedImageView) itemView.findViewById(R.id.profilePictureUserAvatar);
            pictureOptions = (ImageButton) itemView.findViewById(R.id.profilePicturePictureOptions);
            picture = (SquareImageView) itemView.findViewById(R.id.profilePicturePicture);
            pictureCaption = (TextView) itemView.findViewById(R.id.profilePicturePictureComment);
            pictureDate = (TextView) itemView.findViewById(R.id.profilePicturePictureDate);
            pictureCaptionEditWrapper = (LinearLayout) itemView.findViewById(R.id.captionEditWrapper);
            pictureCaptionEdit = (EditText) itemView.findViewById(R.id.profilePicturePictureCommentEdit);
            captionEditConfirm = (ImageButton) itemView.findViewById(R.id.captionEditConfirm);
            captionEditCancel = (ImageButton) itemView.findViewById(R.id.captionEditCancel);

        }
    }

    ProfilePictureListAdapter(Context context, List<ProfilePictureRecyclerView> profilePictureRecyclerView, Boolean editable){
        this.profilePictureRecyclerView = profilePictureRecyclerView;
        this.context = context;
        this.editable = editable;
    }

    @Override
    public int getItemCount() {
        return profilePictureRecyclerView.size();
    }

    @Override
    public ProfilePictureListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_profile_pictures, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProfilePictureListAdapter.ViewHolder holder, final int position) {

        final ProfilePictureListAdapter.ViewHolder holderRef = holder;
        holder.userName.setText(profilePictureRecyclerView.get(position).getUserName());
        holder.pictureCaption.setText(profilePictureRecyclerView.get(position).getPictureCaption());
        holder.pictureDate.setText(profilePictureRecyclerView.get(position).getDate());
        if(!profilePictureRecyclerView.get(position).getAvatarLink().matches("")){
            Picasso.with(context).load(profilePictureRecyclerView.get(position).getAvatarLink()).fit().centerInside().into(holder.userAvatar);

        }

        Picasso.with(context).load(profilePictureRecyclerView.get(position).getPictureLink()).fit().centerInside().into(holder.picture);

        if(editable){

            holder.pictureOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, holderRef.pictureOptions, Gravity.RIGHT);
                    popup.getMenuInflater().inflate(R.menu.user_picture_options, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {


                            if(item.getTitle().toString().matches("Edit Caption")){

                                holderRef.pictureCaption.setVisibility(View.GONE);
                                holderRef.pictureDate.setVisibility(View.GONE);
                                holderRef.pictureCaptionEditWrapper.setVisibility(View.VISIBLE);
                                holderRef.pictureCaptionEdit.setText(holderRef.pictureCaption.getText().toString());
                                holderRef.pictureCaptionEdit.requestFocus();

                                holderRef.captionEditConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        holderRef.pictureCaptionEditWrapper.setVisibility(View.GONE);
                                        holderRef.pictureCaption.setText(holderRef.pictureCaptionEdit.getText().toString());
                                        holderRef.pictureCaption.setVisibility(View.VISIBLE);
                                        holderRef.pictureDate.setVisibility(View.VISIBLE);
                                        ((ProfileActivity) context).updatePicture(profilePictureRecyclerView.get(position).getPictureID(), holderRef.pictureCaptionEdit.getText().toString());
                                        holderRef.pictureCaptionEdit.setText("");
                                    }
                                });

                                holderRef.captionEditCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        holderRef.pictureCaptionEditWrapper.setVisibility(View.GONE);
                                        holderRef.pictureCaption.setVisibility(View.VISIBLE);
                                        holderRef.pictureDate.setVisibility(View.VISIBLE);
                                        holderRef.pictureCaptionEdit.setText("");
                                    }
                                });


                            }


                            if(item.getTitle().toString().matches("Delete Picture")){
                                ((ProfileActivity) context).deletePicture(profilePictureRecyclerView.get(position).getPictureID());
                            }

                            return true;
                        }
                    });

                    popup.show();
                }
            });

        }
        else{
            if(holder.pictureOptions != null && holder.pictureOptions.getParent() != null){
                ((ViewGroup)holder.pictureOptions.getParent()).removeView(holder.pictureOptions);
            }

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
