package com.example.davidyuan7536.workoutlog;

import java.io.Serializable;

/**
 * Created by davidyuan7536 on 7/30/2016.
 */
public class ViewPagerFragmentSwitchListener{

    int currentPage;


    public ViewPagerFragmentSwitchListener(int currentPage) {
        this.currentPage = currentPage;
    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
