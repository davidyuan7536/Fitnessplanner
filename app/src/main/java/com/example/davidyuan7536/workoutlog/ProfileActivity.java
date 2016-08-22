package com.example.davidyuan7536.workoutlog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements ProfileWorkoutFragment.DatePickerFragment.OnLogDatePickerDatePicked{

    Context context;
    View view;

    private Toolbar toolbar;

    private FirebaseUser user;
    private String uID;
    private String userID;
    private DatabaseReference mDatabase;
    private DatabaseReference workoutRef;
    private DatabaseReference userRef;
    private FirebaseMultiQuery firebaseMultiQuery;

    private Boolean isPersonalProfile;
    private Boolean following;


    String today;


    int currentPicture;


    ViewPagerAdapter adapter;


    TabLayout tabLayout;
    ViewPager viewPager;



    FloatingActionMenu actionMenu;


    private int[] tabIcons = {
            R.drawable.ic_content_paste_black_24dp,
            R.drawable.ic_camera_alt_black_24dp,
    };


    private Uri mCropImageUri;


    private boolean isUserAvatar;


    public void killActivity(){
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.user_options:
                if(isPersonalProfile == null){

                    user = FirebaseAuth.getInstance().getCurrentUser();
                    uID = user.getUid();

                    Intent extra = getIntent();
                    if(extra.getStringExtra("userID").matches(uID)){
                        showPopup(true);
                    }
                    else{
                        showPopup(false);
                    }


                }
                else{
                    showPopup(isPersonalProfile);
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_2);

        context = this;
        view = findViewById(android.R.id.content);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String leadingZeroMonth = String.format("%02d", month);
        String leadingZeroDay = String.format("%02d", day);

        today = "" + year + leadingZeroMonth + leadingZeroDay;


        mDatabase = FirebaseDatabase.getInstance().getReference();


        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();

        Intent extras = getIntent();
        userID = extras.getStringExtra("userID");

        if(userID != null){
            if(userID.matches(uID)){
                isPersonalProfile = true;
            }
            else{
                isPersonalProfile = false;
            }
        }

        following = extras.getBooleanExtra("following", false);



        isUserAvatar = false;

        currentPicture = 0;


        toolbar = (Toolbar) findViewById(R.id.profileToolbar);

        if (toolbar != null) {


            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Floating action menu handler//////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        actionMenu = (FloatingActionMenu) findViewById(R.id.profileActionMenu);

        if(!isPersonalProfile){
            ((ViewGroup)actionMenu.getParent()).removeView(actionMenu);
        }
        else{

            FloatingActionButton addPhoto = (FloatingActionButton) findViewById(R.id.profileActionBtnAddPhoto);
            addPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isUserAvatar = false;
                    onSelectImageClick(view);

                }
            });


            FloatingActionButton addWorkout = (FloatingActionButton) findViewById(R.id.profileActionBtnAddWorkout);

            addWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);


                    editWorkout(year, month, day);

                }
            });


        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////





        tabLayout = (TabLayout) findViewById(R.id.profileTabs);
        viewPager  = (ViewPager) findViewById(R.id.profileViewpager);
//        appbarLayout = (AppBarLayout) findViewById(R.id.profileAppbar);






        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

               @Override
               public void onTabSelected(TabLayout.Tab tab) {
                   super.onTabSelected(tab);
                   int tabIconColor = ContextCompat.getColor(ProfileActivity.this, R.color.tabSelected);
                   tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
               }

               @Override
               public void onTabUnselected(TabLayout.Tab tab) {
                   super.onTabUnselected(tab);
                   int tabIconColor = ContextCompat.getColor(ProfileActivity.this, R.color.tabUnselected);
                   tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
               }

               @Override
               public void onTabReselected(TabLayout.Tab tab) {
                   super.onTabReselected(tab);
               }
           }
        );


        setupTabIcons();



        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        //Firebase getting user info////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////




        if(isPersonalProfile){
            userRef = mDatabase.child("users").child(uID);
            workoutRef =  mDatabase.child("users").child(uID).child("logdates").child(today).child("workoutid");
        }
        else{
            userRef = mDatabase.child("users").child(userID);
            workoutRef =  mDatabase.child("users").child(userID).child("logdates").child(today).child("workoutid");
        }


        firebaseMultiQuery = new FirebaseMultiQuery(userRef, workoutRef);
        final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQuery.start();
        allLoad.addOnCompleteListener(ProfileActivity.this, new AllOnCompleteListener());


//        mDatabase.child("users").child(uID).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final String nameResult = dataSnapshot.getValue(String.class);
//                if (nameResult == null) {
//                    Log.d("WorkoutLog", "Failed to retrieve user name");
//                } else {
//
//                    ProfilePictureFragment pictureFragRef = (ProfilePictureFragment) adapter.getRegisteredFragment(1);
//                    pictureFragRef.setUsername(nameResult);
//
//                    mDatabase.child("users").child(uID).child("quote").addListenerForSingleValueEvent(new ValueEventListener() {
//
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String quoteResult = dataSnapshot.getValue(String.class);
//                            if (quoteResult == null) {
//
//                                ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment) adapter.getRegisteredFragment(0);
//                                workoutFragRef.updateUserInfo(nameResult, "");
//
//                            } else {
//                                ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment) adapter.getRegisteredFragment(0);
//                                workoutFragRef.updateUserInfo(nameResult, quoteResult);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//
//                    });
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//        mDatabase.child("users").child(uID).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String usernameResult = dataSnapshot.getValue(String.class);
//                if(usernameResult == null){
//                    Log.d("WorkoutLog", "Failed to retrieve username");
//                }
//                else{
//                    getSupportActionBar().setTitle(usernameResult);
//                    getSupportActionBar().setDisplayShowTitleEnabled(true);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        ////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////




//
//        ValueEventListener userAvatarListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String link = dataSnapshot.getValue(String.class);
//                if(link == null){
//                    return;
//                }
//                else{
//                    ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
//                    ProfilePictureFragment picFragRef = (ProfilePictureFragment)adapter.getRegisteredFragment(1);
//
//                    if(workoutFragRef == null || picFragRef == null){
//                        setupViewPager(viewPager);
//                        workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
//                        picFragRef = (ProfilePictureFragment)adapter.getRegisteredFragment(1);
//                        workoutFragRef.setUserAvatar(link);
//                        picFragRef.setUserAvatar(link);
//                        picFragRef.updateUserAvatar(link);
//                    }
//                    else{
//                        workoutFragRef.setUserAvatar(link);
//                        picFragRef.setUserAvatar(link);
//                        picFragRef.updateUserAvatar(link);
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        mDatabase.child("users").child(uID).child("avatar").addValueEventListener(userAvatarListener);





//
//        childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//
//
//                if(currentPicture < totalUserPictures){
//
//
//                    final FirebasePictures picture = dataSnapshot.getValue(FirebasePictures.class);
//                    ProfilePictureFragment picFragRef = (ProfilePictureFragment)adapter.getRegisteredFragment(1);
//                    picFragRef.initializeAddPicture(picture);
//
//                }
//
//                else{
//
//                }
//
//                currentPicture++;
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d("WorkoutLog", "onChildChanged:" + dataSnapshot.getKey());
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d("WorkoutLog", "onChildRemoved:" + dataSnapshot.getKey());
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d("WorkoutLog", "onChildMoved:" + dataSnapshot.getKey());
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//
//            }
//        };




//        mDatabase.child("users").child(uID).child("pictures").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                totalUserPictures = dataSnapshot.getChildrenCount();
//                mDatabase.child("users").child(uID).child("pictures").addChildEventListener(childEventListener);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

        int tabIconColor = ContextCompat.getColor(ProfileActivity.this, R.color.tabSelected);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProfileWorkoutFragment(), "Workouts");
        adapter.addFragment(new ProfilePictureFragment(), "Photos");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        private Fragment mCurrentFragment;
        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
        private final FragmentManager mFragmentManager;


        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentManager = fragmentManager;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
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


    public void onDatePicked(int year, int month, int day) {

        ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
        workoutFragRef.setDate(year, month, day);
        String leadingZeroMonth = String.format("%02d", month);
        String leadingZeroDay = String.format("%02d", day);

        final String today = "" + year + leadingZeroMonth + leadingZeroDay;

        DatabaseReference wkRef;
        if(isPersonalProfile){
            wkRef =  mDatabase.child("users").child(uID).child("logdates").child(today).child("workoutid");
        }
        else{
            wkRef = mDatabase.child("users").child(userID).child("logdates").child(today).child("workoutid");
        }
        wkRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> workoutRetreiver = (Map<String, Object>) dataSnapshot.getValue();
                if (workoutRetreiver == null) {

                    ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
                    workoutFragRef.initializeEmptyWorkout();
                    return;
                }

                int iteration = 0;
                for (Object value : workoutRetreiver.values()) {
                    final boolean reset;
                    if (iteration == 0) {
                        reset = true;
                    } else {
                        reset = false;
                    }
                    iteration++;

                    final String workoutID = value.toString();
                    Query workoutQuery = mDatabase.child("workouts").child(workoutID);
                    workoutQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            FirebaseWorkouts workoutTitle = dataSnapshot.getValue(FirebaseWorkouts.class);
                            ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
                            workoutFragRef.addWorkout(workoutTitle.getTitle(), workoutID, today, reset);



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
                                ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
                                workoutFragRef.addExerciseToWorkout(exercise.getName(), exerciseSnapshot.getKey(), exercise.getSets(), workoutID);


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



    public void updatePicture(String pictureID, String caption){
        mDatabase.child("users").child(uID).child("pictures").child(pictureID).child("caption").setValue(caption);
        ProfilePictureFragment picFragRef = (ProfilePictureFragment)adapter.getRegisteredFragment(1);
        picFragRef.updatePicture(pictureID, caption);


    }


    public void deletePicture(String pictureID){
        mDatabase.child("users").child(uID).child("pictures").child(pictureID).removeValue();
        ProfilePictureFragment picFragRef = (ProfilePictureFragment)adapter.getRegisteredFragment(1);
        picFragRef.deletePicture(pictureID);
    }


    public void saveUserAvatar(){
        isUserAvatar = true;
        onSelectImageClick(view);
    }

    public void onPictureSaved(final String caption, final String today, final String date, final String key, final String link){

        mDatabase.child("users").child(uID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUserProfileNameAndAvatar result = dataSnapshot.getValue(FirebaseUserProfileNameAndAvatar.class);
                if(result != null){
                    ProfilePictureFragment picFragRef = (ProfilePictureFragment)adapter.getRegisteredFragment(1);
                    picFragRef.onPictureSaved(result.getUsername(), caption, today, date, key, link, result.getAvatar());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }







    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }


    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity

                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                Intent intent = new Intent(ProfileActivity.this, PictureSaverActivity.class);
                intent.putExtra("isUserAvatar", isUserAvatar);
                intent.putExtra("imageUri", resultUri);
                intent.putExtra("userID", uID);
                startActivityForResult(intent, 1);



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == 1){

            if(resultCode == Activity.RESULT_OK){
                String uploaded = data.getStringExtra("uploaded");

                if(uploaded.equals("avatar")){

                    ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment) adapter.getRegisteredFragment(0);
                    ProfilePictureFragment pictureFragRef = (ProfilePictureFragment) adapter.getRegisteredFragment(1);

                    workoutFragRef.setUserAvatar(data.getStringExtra("link"));
                    pictureFragRef.updateUserAvatar(data.getStringExtra("link"));

                }
                if(uploaded.equals("picture")){

                    actionMenu.toggle(false);

                    onPictureSaved(data.getStringExtra("pictureCaption"), data.getStringExtra("pictureToday"),
                            data.getStringExtra("pictureDate"), data.getStringExtra("pictureID"),
                            data.getStringExtra("pictureLink"));
                }


            }
            else{
                actionMenu.toggle(false);
            }
        }

        if(requestCode == 2){

            if(resultCode == Activity.RESULT_OK) {

                Log.d("WorkoutLog", "Returning from profile info activity");

                String name = data.getStringExtra("name");
                String username = data.getStringExtra("username");
                String userquote = data.getStringExtra("userquote");

                getSupportActionBar().setTitle(username);
                ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
                workoutFragRef.updateUserInfo(name, userquote);

                ProfilePictureFragment pictureFragRef = (ProfilePictureFragment)adapter.getRegisteredFragment(1);
                pictureFragRef.updateUserName(name);

            }
            else{

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {

        if(isUserAvatar){
            CropImage.activity(imageUri).setFixAspectRatio(true).setAllowRotation(true).setFixAspectRatio(true).setAutoZoomEnabled(true)
                    .setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);
        }
        else{

            CropImage.activity(imageUri).setAspectRatio(1,1).setAllowRotation(true).setFixAspectRatio(true).setAutoZoomEnabled(true)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }


    }


    public void editWorkout(int year, int month, int day){

        Intent intent = new Intent();
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }



    public void showPopup(boolean isPersonalProfile){
        View menuItemView = findViewById(R.id.user_options);
        PopupMenu popup = new PopupMenu(context, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        if(isPersonalProfile){
            inflate.inflate(R.menu.profile_popup, popup.getMenu());
        }
        else{

            if(following){
                inflate.inflate(R.menu.profile_popup3, popup.getMenu());
            }
            else{
                inflate.inflate(R.menu.profile_popup2, popup.getMenu());
            }

        }


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {


                if(item.getTitle().toString().matches("Edit User Info")){

                    Intent intent = new Intent(ProfileActivity.this, UserInfoActivity.class);
                    startActivityForResult(intent, 2);

                }


                if(item.getTitle().toString().matches("Logout")){

                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    intent.putExtra("finish", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                if(item.getTitle().toString().matches("Follow User")){

                    mDatabase.child("users").child(uID).child("following").child(userID).setValue(userID);
                    mDatabase.child("users").child(userID).child("followers").child(uID).setValue(uID);
                    following = true;
                    mDatabase.child("users").child(userID).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue(String.class) != null){
                                Toast.makeText(ProfileActivity.this, "You are now following " + dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                if(item.getTitle().toString().matches("Unfollow User")){

                    mDatabase.child("users").child(uID).child("following").child(userID).setValue(userID);
                    mDatabase.child("users").child(userID).child("followers").child(uID).setValue(uID);
                    following = false;
                    mDatabase.child("users").child(userID).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue(String.class) != null){
                                Toast.makeText(ProfileActivity.this, "You have unfollowed " + dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                if(item.getTitle().toString().matches("Report User")){

                }

                return true;
            }
        });


        popup.show();

    }


    public Boolean personalProfile(){
        return isPersonalProfile;
    }

    @Override
    public void onBackPressed() {
        if(isPersonalProfile){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }else{
            Intent returnIntent = new Intent();
            returnIntent.putExtra("following", following);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
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
        @Override
        public void onComplete(@NonNull Task<Map<DatabaseReference, DataSnapshot>> task) {
            if (task.isSuccessful()) {
                final Map<DatabaseReference, DataSnapshot> result = task.getResult();

                ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment) adapter.getRegisteredFragment(0);
                ProfilePictureFragment pictureFragRef = (ProfilePictureFragment) adapter.getRegisteredFragment(1);

                DataSnapshot dataSnapshotUser = result.get(userRef);
                FirebaseUsersProfile user = dataSnapshotUser.getValue(FirebaseUsersProfile.class);
                if(user != null){


                    if(user.getName() == null){
                        Log.d("WorkoutLog", "Could not retrieve user name");
                    }else{
                        workoutFragRef.updateName(user.getName());
                    }



                    if(user.getAvatar() == null){

                    }else{
                        workoutFragRef.setUserAvatar(user.getAvatar());
//                        pictureFragRef.setUserAvatar(user.getAvatar());
//                        pictureFragRef.updateUserAvatar(user.getAvatar());
                    }


                    if(user.getUsername() == null){
                        Log.d("WorkoutLog", "Could not retrieve username");
                    }else{
                        getSupportActionBar().setTitle(user.getUsername());
                        getSupportActionBar().setDisplayShowTitleEnabled(true);
                    }



                    if (user.getQuote() == null) {
                        workoutFragRef.updateUserQuote("");
                    } else {

                        workoutFragRef.updateUserQuote(user.getQuote());
                    }


                    if(user.getPictures() == null){


                    }
                    else{

                        for (Map.Entry<String, FirebasePictures> entry : user.getPictures().entrySet()) {

                            pictureFragRef.initializeAddPicture(user.getUsername(), entry.getValue(), user.getAvatar());

                        }

                    }

                }

                DataSnapshot dataSnapshotWorkouts = result.get(workoutRef);
                Map<String, Object> workoutRetreiver = (Map<String, Object>) dataSnapshotWorkouts.getValue();
                if (workoutRetreiver == null) {

                    workoutFragRef.initializeEmptyWorkout();
                    return;
                }

                int iteration = 0;
                for (Object value : workoutRetreiver.values()) {
                    final boolean reset;
                    if (iteration == 0) {
                        reset = true;
                    } else {
                        reset = false;
                    }
                    iteration++;

                    final String workoutID = value.toString();
                    Query workoutQuery = mDatabase.child("workouts").child(workoutID);
                    workoutQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            FirebaseWorkouts workoutTitle = dataSnapshot.getValue(FirebaseWorkouts.class);
                            ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
                            workoutFragRef.addWorkout(workoutTitle.getTitle(), workoutID, today, reset);

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
                                ProfileWorkoutFragment workoutFragRef = (ProfileWorkoutFragment)adapter.getRegisteredFragment(0);
                                workoutFragRef.addExerciseToWorkout(exercise.getName(), exerciseSnapshot.getKey(), exercise.getSets(), workoutID);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
            else {
                Log.d("WorkoutLog", task.getException().toString());

            }

        }
    }

}