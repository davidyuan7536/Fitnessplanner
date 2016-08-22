package com.example.davidyuan7536.workoutlog;

import java.util.List;

/**
 * Created by davidyuan7536 on 8/10/2016.
 */
public class FirebasePictures {

    private String caption;
    private String date;
    private String order;
    private String key;
    private String link;


    public FirebasePictures() {
    }

    public String getCaption() {
        return caption;
    }

    public String getOrder() {
        return order;
    }

    public String getDate() {
        return date;
    }

    public String getKey(){
        return key;
    }

    public String getLink(){
        return link;
    }

    @Override
    public String toString() {
        return "FirebasePictures{" +
                "caption='" + caption + '\'' +
                ", date='" + date + '\'' +
                ", order='" + order + '\'' +
                ", key='" + key + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
