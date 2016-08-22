package com.example.davidyuan7536.workoutlog;

/**
 * Created by davidyuan7536 on 8/16/2016.
 */
public class UserSearchRecyclerView{

    private String name;
    private String username;
    private String avatar;
    private String uID;
    private boolean following;

    public UserSearchRecyclerView() {
    }

    public UserSearchRecyclerView(String uID, String name, String username, boolean following) {
        this.name = name;
        this.username = username;
        this.avatar = "";
        this.uID = uID;
        this.following = following;
    }

    public UserSearchRecyclerView(String uID, String name, String username, String avatar, boolean following) {
        this.name = name;
        this.username = username;
        this.avatar = avatar;
        this.uID = uID;
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getuID(){
        return uID;
    }

    public boolean getFollowing(){
        return  following;
    }

    public void setFollowing(boolean following){
        this.following = following;
    }
}
