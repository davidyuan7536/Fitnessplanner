package com.example.davidyuan7536.workoutlog;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.TabLayout;
import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.example.davidyuan7536.workoutlog.HomeFragment;
import com.example.davidyuan7536.workoutlog.LogFragment;
import com.example.davidyuan7536.workoutlog.NotificationFragment;


public class Home extends AppCompatActivity {

    ImageButton homeBtn;
    ImageButton notificationBtn;
    ImageButton profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        homeBtn = (ImageButton) findViewById(R.id.homeActivityButton);
        notificationBtn  = (ImageButton) findViewById(R.id.notificationActivityButton);
        profileBtn = (ImageButton) findViewById(R.id.profileActivityButton);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Notification.class);
                startActivity(i);
                overridePendingTransition(0, 0);

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Profile.class);
                startActivity(i);
                overridePendingTransition(0, 0);

            }
        });

    }
}
