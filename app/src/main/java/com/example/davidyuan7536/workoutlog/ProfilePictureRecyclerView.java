package com.example.davidyuan7536.workoutlog;

import android.net.Uri;

import java.util.List;

/**
 * Created by davidyuan7536 on 8/8/2016.
 */
public class ProfilePictureRecyclerView {

    private String userName;
    private String pictureCaption;
    private String pictureID;
    private String date;
    private String link;
    private String avatarLink;


    public ProfilePictureRecyclerView(String userName, String pictureID, String link, String pictureCaption, String date) {
        this.userName = userName;
        this.pictureCaption = pictureCaption;
        this.pictureID = pictureID;
        this.date = date;
        this.link = link;
        this.avatarLink = "";
    }

    public ProfilePictureRecyclerView(String userName, String pictureID, String link, String pictureCaption, String date, String avatarLink) {
        this.userName = userName;
        this.pictureID = pictureID;
        this.link = link;
        this.pictureCaption = pictureCaption;
        this.date = date;
        this.avatarLink = avatarLink;
    }

    public String getPictureCaption() {
        return pictureCaption;
    }

    public String getPictureID() {
        return pictureID;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getPictureLink(){
        return link;
    }

    public String getAvatarLink(){
        return avatarLink;
    }

    public void setPictureCaption(String pictureCaption){
        this.pictureCaption = pictureCaption;
    }

    public void setAvatarLink(String link){
        this.avatarLink = link;
    }

    public void setUserName(String name){
        this.userName = name;
    }
}
