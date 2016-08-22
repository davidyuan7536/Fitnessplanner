package com.example.davidyuan7536.workoutlog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by davidyuan7536 on 8/8/2016.
 */
public class ProfilePictureGridAdapter extends RecyclerView.Adapter<ProfilePictureGridAdapter.ViewHolder>{

    private List<ProfilePictureRecyclerView> profilePictureRecyclerView;
    Context context;
    Boolean editable;
    AlertDialog dlg;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;



        ViewHolder(View itemView) {
            super(itemView);
            picture = (ImageView) itemView.findViewById(R.id.profilePicturePictureGrid);


        }
    }

    ProfilePictureGridAdapter(Context context , List<ProfilePictureRecyclerView> profilePictureRecyclerView, Boolean editable){
        this.profilePictureRecyclerView = profilePictureRecyclerView;
        this.context = context;
        this.editable = editable;

    }

    @Override
    public int getItemCount() {
        return profilePictureRecyclerView.size();
    }

    @Override
    public ProfilePictureGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_profile_picture_grid, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProfilePictureGridAdapter.ViewHolder holder, int position) {



        Picasso.with(context).load(profilePictureRecyclerView.get(position).getPictureLink()).into(holder.picture);




        holder.picture.setOnClickListener(new customOnPictureClick(profilePictureRecyclerView.get(position).getUserName(),
                profilePictureRecyclerView.get(position).getPictureID(),
                profilePictureRecyclerView.get(position).getPictureLink(),
                profilePictureRecyclerView.get(position).getPictureCaption(),
                profilePictureRecyclerView.get(position).getDate(),
                profilePictureRecyclerView.get(position).getAvatarLink()));

    }




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




    public class customOnPictureClick implements View.OnClickListener
    {
        String avatarLink;
        String userName;
        String pictureCaption;
        String pictureLink;
        String pictureDate;
        String pictureID;

        public customOnPictureClick(String userName, String pictureID, String pictureLink, String pictureCaption, String pictureDate, String avatarLink) {
            this.userName = userName;
            this.pictureCaption = pictureCaption;
            this.pictureID = pictureID;
            this.pictureLink = pictureLink;
            this.pictureDate = pictureDate;
            this.avatarLink = avatarLink;
        }

        @Override
        public void onClick(View v)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialoglayout = inflater.inflate(R.layout.profile_picture_grid_alert, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialoglayout);

            MLRoundedImageView avatarPicture = (MLRoundedImageView) dialoglayout.findViewById(R.id.profilePictureUserAvatarGrid);
            if(!avatarLink.matches("")){
                Picasso.with(context).load(avatarLink).fit().centerInside().into(avatarPicture);

            }

            SquareImageView picture = (SquareImageView) dialoglayout.findViewById(R.id.profilePicturePictureGrid);
            Picasso.with(context).load(pictureLink).fit().centerInside().into(picture);

            TextView userNameTV = (TextView) dialoglayout.findViewById(R.id.profilePictureUserNameGrid);
            userNameTV.setText(userName);

            final TextView pictureCaptionTV = (TextView) dialoglayout.findViewById(R.id.profilePicturePictureCommentGrid);
            pictureCaptionTV.setText(pictureCaption);

            final LinearLayout captionEditWrapper = (LinearLayout) dialoglayout.findViewById(R.id.captionEditWrapper);
            final EditText pictureCaptionEdit = (EditText) dialoglayout.findViewById(R.id.profilePicturePictureCommentEditGrid);
            final ImageButton captionEditConfirm = (ImageButton) dialoglayout.findViewById(R.id.captionEditConfirm);
            final ImageButton captionEditCancel = (ImageButton) dialoglayout.findViewById(R.id.captionEditCancel);

            final TextView pictureDateTV = (TextView) dialoglayout.findViewById(R.id.profilePicturePictureDateGrid);
            pictureDateTV.setText(pictureDate);

            final ImageButton pictureOptions = (ImageButton) dialoglayout.findViewById(R.id.profilePicturePictureOptionsGrid);

            if(editable){

                pictureOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PopupMenu popup = new PopupMenu(context, pictureOptions, Gravity.RIGHT);
                        popup.getMenuInflater().inflate(R.menu.user_picture_options, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {


                                if(item.getTitle().toString().matches("Edit Caption")){

                                    pictureCaptionTV.setVisibility(View.GONE);
                                    pictureDateTV.setVisibility(View.GONE);
                                    captionEditWrapper.setVisibility(View.VISIBLE);
                                    pictureCaptionEdit.setText(pictureCaptionTV.getText().toString());
                                    pictureCaptionEdit.requestFocus();

                                    captionEditConfirm.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            captionEditWrapper.setVisibility(View.GONE);
                                            pictureCaptionTV.setText(pictureCaptionEdit.getText().toString());
                                            pictureCaptionTV.setVisibility(View.VISIBLE);
                                            pictureDateTV.setVisibility(View.VISIBLE);
                                            ((ProfileActivity) context).updatePicture(pictureID, pictureCaptionEdit.getText().toString());
                                            pictureCaptionEdit.setText("");
                                        }
                                    });

                                    captionEditCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            captionEditWrapper.setVisibility(View.GONE);
                                            pictureCaptionTV.setVisibility(View.VISIBLE);
                                            pictureDateTV.setVisibility(View.VISIBLE);
                                            pictureCaptionEdit.setText("");
                                        }
                                    });


                                }




                                if(item.getTitle().toString().matches("Delete Picture")){

                                    ((ProfileActivity) context).deletePicture(pictureID);
                                    dlg.dismiss();
                                }

                                return true;
                            }
                        });

                        popup.show(); //showing popup menu
                    }
                });

            }
            else{
                ((ViewGroup)pictureOptions.getParent()).removeView(pictureOptions);
            }



            dlg = builder.show();

        }

    };




}
