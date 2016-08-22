package com.example.davidyuan7536.workoutlog;

/**
 * Created by davidyuan7536 on 8/15/2016.
 */
public class FirebaseEmail {

    private String provider;
    private String extension;

    FirebaseEmail(){

    }

    public String getProvider() {
        return provider;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public String toString() {
        return "FirebaseEmail{" +
                "provider='" + provider + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
