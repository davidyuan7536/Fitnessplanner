package com.example.davidyuan7536.workoutlog;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.design.widget.TabLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity implements LogFragment.DatePickerFragment.OnLogDatePickerDatePicked, HomeFragment.DatePickerFragment.OnHomePickerDatePicked{


    Context context;
    Toolbar toolbar;
    String alertUserName;

    ViewPagerAdapter adapter;

    private DatabaseReference mDatabase;
    FirebaseUser user;
    private String uID;

    AlertDialog.Builder alert;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_chrome_reader_mode_black_24dp,
            R.drawable.ic_favorite_border_black_24dp,
            R.drawable.ic_content_paste_black_24dp
    };


    List<HomeFeedRecycleViewUser> homeFeedFollowingList;





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchBox:
                Intent userSearchIntent = new Intent(HomeActivity.this, UserSearchActivity.class);
                Bundle b1 = new Bundle();
                userSearchIntent.putExtras(b1);
                startActivityForResult(userSearchIntent, 20);

                return true;

            case R.id.userProfile:
                Intent userProfileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                Bundle b2 = new Bundle();
                b2.putString("userID", uID);
                userProfileIntent.putExtras(b2);
                startActivityForResult(userProfileIntent, 10);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            Intent launchNextActivity = new Intent(HomeActivity.this, MainActivity.class);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(launchNextActivity);
            finish();
            return;
        }


        setContentView(R.layout.activity_home);


        context = this;
        alertUserName = "";

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fitness Planner");



        mDatabase = FirebaseDatabase.getInstance().getReference();


        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();

        Intent i = getIntent();
        if(i.getStringExtra("updateUser") != null){
            updateUserInfo();
        }




        alert = new AlertDialog.Builder(this);
        alert.setTitle("Please Set a Display Name");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.setCancelable(false);



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(HomeActivity.this, R.color.tabSelected);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(HomeActivity.this, R.color.tabUnselected);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        setupTabIcons();


        ValueEventListener userNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                if(userName == null){
                    alert.setView(null);


                    float density = getResources().getDisplayMetrics().density;
                    final int paddingPixel10 = (int)(10 * density);
                    final int paddingPixel20 = (int)(20 * density);

                    LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    final LinearLayout ll = new LinearLayout(context);
                    ll.setLayoutParams(linearLayoutParams);
                    ll.setPadding(paddingPixel20, paddingPixel10, paddingPixel20, paddingPixel10);
                    final EditText input = (EditText) View.inflate(context, R.layout.material_factory_edit_text, null);
                    input.setLayoutParams(linearLayoutParams);
                    input.setPadding(paddingPixel10, paddingPixel10, paddingPixel10, paddingPixel10);
                    ll.addView(input);
                    alert.setView(ll);

                    final AlertDialog dialog = alert.create();
                    dialog.show();


                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {

                            if(input.getText().toString().length() < 3){
                                Toast.makeText(context, "Display Name must be at least 3 characters long", Toast.LENGTH_SHORT).show();
                            }
                            else if(!isAlphaNumeric(input.getText().toString()) || input.getText().toString().contains(" ")){
                                Toast.makeText(context, "Display Name must be alphanumeric", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Query un = mDatabase.child("username").child(input.getText().toString());
                                un.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String userID = dataSnapshot.getValue(String.class);
                                        if(userID == null){
                                            setUserName(input.getText().toString());
                                            dialog.dismiss();
                                            return;
                                        }
                                        else{
                                            Toast.makeText(context, "Display name taken", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {


                                    }
                                });
                            }
                        }
                    });


                    input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            alertUserName = editable.toString();
                        }
                    });

                }
                else{
                    Log.d("WorkoutLog", "User name: " + userName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").child(uID).child("username").addListenerForSingleValueEvent(userNameListener);


        homeFeedFollowingList = new ArrayList<HomeFeedRecycleViewUser>();

//        intializeLogFeed();
    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        int tabIconColor = ContextCompat.getColor(HomeActivity.this, R.color.tabSelected);
        int tabIconColorOut = ContextCompat.getColor(HomeActivity.this, R.color.tabUnselected);

        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(tabIconColorOut, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(tabIconColorOut, PorterDuff.Mode.SRC_IN);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new NotificationFragment(), "Notification");
        adapter.addFragment(new RootFragLog(), "Log");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        private Fragment mCurrentFragment;
        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
        private Fragment logFrag;
        private final FragmentManager mFragmentManager;


        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentManager = fragmentManager;
        }



        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }






    }

    public void intializeLogFeed(){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        String leadingZeroMonth = String.format("%02d", month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String leadingZeroDay = String.format("%02d", day);

        final String today = ""+year+leadingZeroMonth+leadingZeroDay;

        mDatabase.child("users").child(uID).child("logdates").child(today).child("workoutid").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> workoutRetreiver = (Map<String, Object>)dataSnapshot.getValue();
                if(workoutRetreiver == null){
                    return;
                }

                for(Object value: workoutRetreiver.values()) {
                    final String workoutID = value.toString();
                    Query workoutQuery = mDatabase.child("workouts").child(uID).child("workoutid").child(workoutID);
                    workoutQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> workoutTitle = (Map<String, Object>) dataSnapshot.getValue();

                            LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
                            logFragRef.initializeAddWorkout(workoutTitle.get("title").toString(), workoutID, today);
                            Log.d("WorkoutLog", "Adding Workout: " + workoutTitle.get("title").toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Query exerciseQuery = mDatabase.child("exercises").child(uID).child("workoutid").child(workoutID).child("exerciseid");
                    exerciseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot exerciseSnapshot : dataSnapshot.getChildren()) {
                                FirebaseExercises exercise = exerciseSnapshot.getValue(FirebaseExercises.class);
                                LogFragment  logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
                                logFragRef.addExerciseToWorkout(exercise.getName(), exercise.getSets(), workoutID, exerciseSnapshot.getKey());


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {

            if(resultCode == Activity.RESULT_OK){


                int year=data.getIntExtra("year", 0);
                int month = data.getIntExtra("month", 0);
                int day = data.getIntExtra("day", 0);

                if(year != 0 && month != 0 && day !=0 ){
                    onDatePicked(year, month, day, "log");
                }
                viewPager.setCurrentItem(2);


            }
        }
    }

    public String saveExercise(String exerciseName, List<String> exerciseSets, String workoutID){

        String key = mDatabase.child("exercises").child(workoutID).child("exerciseid").push().getKey();

        mDatabase.child("exercises").child(workoutID).child("exerciseid").child(key).child("name").setValue(exerciseName);
        mDatabase.child("exercises").child(workoutID).child("exerciseid").child(key).child("sets").setValue(exerciseSets);

        return key;
    }

    public void updateExercise(String exerciseName, List<String> exerciseSets, String workoutID, String exerciseID){

        mDatabase.child("exercises").child(workoutID).child("exerciseid").child(exerciseID).child("name").setValue(exerciseName);
        mDatabase.child("exercises").child(workoutID).child("exerciseid").child(exerciseID).child("sets").setValue(exerciseSets);

    }

    public void deleteExercise(String workoutID, String exerciseID){
        mDatabase.child("exercises").child(workoutID).child("exerciseid").child(exerciseID).removeValue();
    }

    public boolean deleteWorkout(String workoutID, String date){


        mDatabase.child("workouts").child(workoutID).removeValue();
        mDatabase.child("users").child(uID).child("logdates").child(date).child("workoutid").child(workoutID).removeValue();
        mDatabase.child("exercises").child(workoutID).removeValue();


        return true;
    }

    public String saveWorkout(String workoutTitle, String date){

        String key = mDatabase.child("workouts").push().getKey();
        mDatabase.child("workouts").child(key).child("title").setValue(workoutTitle);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        mDatabase.child("workouts").child(key).child("timestamp").setValue(ts);
        mDatabase.child("users").child(uID).child("logdates").child(date).child("workoutid").child(key).setValue(key);
        return key;
    }

    public void updateWorkout(String workoutID, String workoutTitle){

        mDatabase.child("workouts").child(workoutID).child("title").setValue(workoutTitle);
    }


    public void onDatePicked(int year, int month, int day, String fragment){

        if(fragment.matches("home")){



            final HomeFragment homeFragment = (HomeFragment)adapter.getCurrentFragment();
            homeFragment.setDate(year, month,day);


            Toast.makeText(HomeActivity.this, "Date changed from home frag", Toast.LENGTH_SHORT).show();

            String leadingZeroMonth = String.format("%02d", month);
            String leadingZeroDay = String.format("%02d", day);
            final String today = ""+year+leadingZeroMonth+leadingZeroDay;



            mDatabase.child("users").child(uID).child("following").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> followingRetreiver = (Map<String, Object>)dataSnapshot.getValue();
                    if(followingRetreiver == null){
                        Log.d("WorkoutLog", "no following");
                        homeFragment.stopLoadingAnimation();
                        return;
                    }


                    for(Object following: followingRetreiver.values()) {

                        final String followingID = following.toString();

                        Query userQuery = mDatabase.child("users").child(followingID);
                        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final FirebaseUsersHomeFrag user = dataSnapshot.getValue(FirebaseUsersHomeFrag.class);

                                if(user != null){

                                    Query workoutQuery = mDatabase.child("users").child(followingID).child("logdates").child(today).child("workoutid").orderByKey();
                                    workoutQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Map<String, Object> workoutRetreiver = (Map<String, Object>)dataSnapshot.getValue();
                                            if(workoutRetreiver == null){
                                                Log.d("WorkoutLog", "user has no workouts");
                                                homeFragment.stopLoadingAnimation();
                                                return;
                                            }

                                            for(Object value: workoutRetreiver.values()) {

                                                Log.d("WorkoutLog", "user has workout" + value.toString());
                                                final String workoutID = value.toString();

                                                DatabaseReference workoutRef = mDatabase.child("workouts").child(workoutID);
                                                DatabaseReference exerciseRef = mDatabase.child("exercises").child(workoutID).child("exerciseid");

                                                FirebaseMultiQuery firebaseMultiQuery = new FirebaseMultiQuery(workoutRef, exerciseRef);
                                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQuery.start();

                                                if(user.getAvatar() == null){
                                                    allLoad.addOnCompleteListener(HomeActivity.this, new AllOnCompleteListener(workoutRef, exerciseRef, followingID, user.getUsername(), "", workoutID));
                                                }

                                                allLoad.addOnCompleteListener(HomeActivity.this, new AllOnCompleteListener(workoutRef, exerciseRef, followingID, user.getUsername(), user.getAvatar(), workoutID));

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else{
                                    homeFragment.stopLoadingAnimation();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else{

            LogFragment  logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
            logFragRef.setDate(year, month, day);
            String leadingZeroMonth = String.format("%02d", month);
            String leadingZeroDay = String.format("%02d", day);
            final String today = ""+year+leadingZeroMonth+leadingZeroDay;


            mDatabase.child("users").child(uID).child("logdates").child(today).child("workoutid").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> workoutRetreiver = (Map<String, Object>)dataSnapshot.getValue();
                    if(workoutRetreiver == null){
                        LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
                        logFragRef.initializeEmptyWorkout();
                        return;
                    }

                    int iteration = 0;
                    for(Object value: workoutRetreiver.values()) {
                        final boolean reset;
                        if(iteration == 0){
                            reset = true;
                        }
                        else{
                            reset = false;
                        }
                        iteration++;

                        final String workoutID = value.toString();
                        Query workoutQuery = mDatabase.child("workouts").child(workoutID);
                        workoutQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                FirebaseWorkouts workoutTitle =  dataSnapshot.getValue(FirebaseWorkouts.class);
                                LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
                                logFragRef.initializeAddWorkout2(workoutTitle.getTitle(), workoutID, today, reset);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Query exerciseQuery = mDatabase.child("exercises").child(workoutID).child("exerciseid");
                        exerciseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot exerciseSnapshot : dataSnapshot.getChildren()) {
                                    FirebaseExercises exercise = exerciseSnapshot.getValue(FirebaseExercises.class);
                                    LogFragment  logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
                                    logFragRef.addExerciseToWorkout(exercise.getName(), exercise.getSets(), workoutID, exerciseSnapshot.getKey());
                                    Log.d("WorkoutLog", "Adding Exercise: " +exercise.getName());

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });


        }
    }

    public void onExerciseSaved(String exerciseName, List<String> exerciseSets, String workoutID, String uniqueID){

        LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
        logFragRef.addExerciseToWorkout(exerciseName, exerciseSets, workoutID, uniqueID);

    }

    public void onExerciseUpdated(String exerciseName, List<String> exerciseSets, String workoutID, String exerciseID){

        LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
        logFragRef.updateExercise(exerciseName, exerciseSets, workoutID, exerciseID);

    }

    public void onExerciseDeleted(String workoutID, String exerciseID){
        LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
        logFragRef.deleteExercise(workoutID, exerciseID);

    }

    public void onDeleteWorkout(int position){
        LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
        logFragRef.deleteWorkout(position);
    }

    public void onUpdateWorkout(String workoutID, String workoutTitle){
        LogFragment logFragRef = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.root_frag_log);
        logFragRef.updateWorkout(workoutID, workoutTitle);

    }

    public void setUserName(String userName){

        mDatabase.child("users").child(uID).child("username").setValue(userName);
        mDatabase.child("username").child(userName).setValue(uID);

    }

    public boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }

    private void updateUserInfo(){
        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String number = intent.getStringExtra("number");
        String quote = intent.getStringExtra("quote");

        if(name != null){
            mDatabase.child("users").child(uID).child("name").setValue(name);
            mDatabase.child("name").child(uID).setValue(name);
        }
        if(username != null){
            mDatabase.child("users").child(uID).child("username").setValue(username);
            mDatabase.child("username").child(username).setValue(uID);
        }
        if(email != null){
            String emailSplit[] = email.split("\\.");
            mDatabase.child("users").child(uID).child("email").child("provider").setValue(emailSplit[0]);
            mDatabase.child("users").child(uID).child("email").child("extension").setValue(emailSplit[1]);
            mDatabase.child("email").child(emailSplit[0]).setValue(uID);
        }
        if(number != null){
            mDatabase.child("users").child(uID).child("number").setValue(number);
            mDatabase.child("number").child(number).setValue(uID);
        }
        if(quote != null){
            mDatabase.child("users").child(uID).child("quote").setValue(quote);
        }


    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }



    public class FirebaseMultiQuery {

        private final HashSet<DatabaseReference> refs = new HashSet<>();
        private final HashMap<DatabaseReference, DataSnapshot> snaps = new HashMap<>();
        private final HashMap<DatabaseReference, ValueEventListener> listeners = new HashMap<>();

        public FirebaseMultiQuery(final DatabaseReference... refs) {
            for (final DatabaseReference ref : refs) {
                add(ref);
            }
        }

        public void add(final DatabaseReference ref) {
            refs.add(ref);
        }

        public Task<Map<DatabaseReference, DataSnapshot>> start() {
            // Create a Task<DataSnapsot> to trigger in response to each database listener.
            //
            final ArrayList<Task<DataSnapshot>> tasks = new ArrayList<>(refs.size());
            for (final DatabaseReference ref : refs) {
                final TaskCompletionSource<DataSnapshot> source = new TaskCompletionSource<>();
                final ValueEventListener listener = new MyValueEventListener(ref, source);
                ref.addListenerForSingleValueEvent(listener);
                listeners.put(ref, listener);
                tasks.add(source.getTask());
            }

            // Return a single Task that triggers when all queries are complete.  It contains
            // a map of all original DatabaseReferences originally given here to their resulting
            // DataSnapshot.
            //
            return Tasks.whenAll(tasks).continueWith(new Continuation<Void, Map<DatabaseReference, DataSnapshot>>() {
                @Override
                public Map<DatabaseReference, DataSnapshot> then(@NonNull Task<Void> task) throws Exception {
                    task.getResult();
                    return new HashMap<>(snaps);
                }
            });
        }

        public void stop() {
            for (final Map.Entry<DatabaseReference, ValueEventListener> entry : listeners.entrySet()) {
                entry.getKey().removeEventListener(entry.getValue());
            }
            snaps.clear();
            listeners.clear();
        }

        private class MyValueEventListener implements ValueEventListener {
            private final DatabaseReference ref;
            private final TaskCompletionSource<DataSnapshot> taskSource;

            public MyValueEventListener(DatabaseReference ref, TaskCompletionSource<DataSnapshot> taskSource) {
                this.ref = ref;
                this.taskSource = taskSource;

            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snaps.put(ref, dataSnapshot);
                taskSource.setResult(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                taskSource.setException(databaseError.toException());
            }
        }

    }


    private class AllOnCompleteListener implements OnCompleteListener<Map<DatabaseReference, DataSnapshot>> {

        private DatabaseReference workoutRef;
        private DatabaseReference exerciseRef;

        private String userid;
        private String username;
        private String useravatar;
        private String workoutid;


        public AllOnCompleteListener(DatabaseReference workoutRef, DatabaseReference exerciseRef, String userid, String username, String useravatar, String workoutid) {
            this.workoutRef = workoutRef;
            this.exerciseRef = exerciseRef;
            this.userid = userid;
            this.username = username;
            this.useravatar = useravatar;
            this.workoutid = workoutid;
        }

        @Override
        public void onComplete(@NonNull Task<Map<DatabaseReference, DataSnapshot>> task) {
            if (task.isSuccessful()) {

                final Map<DatabaseReference, DataSnapshot> result = task.getResult();

                DataSnapshot dataSnapshotWorkout = result.get(workoutRef);
                DataSnapshot dataSnapshotExercise = result.get(exerciseRef);
                HomeFeedRecycleViewUser temp;

                FirebaseWorkouts fbWorkout = dataSnapshotWorkout.getValue(FirebaseWorkouts.class);
                if(fbWorkout != null){

                    temp = new HomeFeedRecycleViewUser(userid, username, useravatar, workoutid, fbWorkout.getTitle(), fbWorkout.getTimestamp());
                }
                else{
                    return;
                }




                for (DataSnapshot exerciseSnapshot : dataSnapshotExercise.getChildren()) {
                    FirebaseExercises fbExercise = exerciseSnapshot.getValue(FirebaseExercises.class);
                    if(fbExercise!=null){
                        temp.setExerciseName(fbExercise.getName());
                        temp.setSingleExerciseDescription(fbExercise.getSets());
                    }

                }

                Log.d("WorkoutLog", temp.toString());
                HomeFragment homeFragment = (HomeFragment)adapter.getCurrentFragment();
                homeFragment.addToInitializer(temp);



            }
            else {
                Log.d("WorkoutLog", task.getException().toString());

            }

        }
    }
}
