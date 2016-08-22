package com.example.davidyuan7536.workoutlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.server.converter.StringToIntConverter;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class UserSearchActivity extends AppCompatActivity {

    Context context;
    Toolbar toolbar;

    private DatabaseReference mDatabase;
    FirebaseUser user;
    private String uID;

    private DatabaseReference usernameRef;
    private DatabaseReference numberRef;
    private DatabaseReference emailRef;

    private FirebaseMultiQuery firebaseMultiQuery;
    FirebaseMultiQuery firebaseMultiQueryInner1;
    FirebaseMultiQuery firebaseMultiQueryInner2;
    FirebaseMultiQuery firebaseMultiQueryInner3;

    private boolean searching;
    private String lastSearch;
    private String finalSearch;

    EditText searchBox;

    RecyclerView rv;
    private List<UserSearchRecyclerView> initalizer;
    UserSearchAdapter adapter;

    int positionOfCurrentlyViewedProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        context = this;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rv = (RecyclerView) findViewById(R.id.searchRv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        initalizer = new ArrayList<UserSearchRecyclerView>();
        adapter = new UserSearchAdapter(context, initalizer);
        rv.setAdapter(adapter);

        searching = false;
        lastSearch = "";
        finalSearch = "";
        searchBox = (EditText) findViewById(R.id.search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(initalizer.size() != 0){
                    initalizer = new ArrayList<UserSearchRecyclerView>();
                    adapter = new UserSearchAdapter(context, initalizer);
                    rv.setAdapter(adapter);
                }


                if(editable.toString().matches("")){
                    return;
                }

                if(editable.toString().contains("$") || editable.toString().contains("#") || editable.toString().contains("[") || editable.toString().contains("]")  || editable.toString().contains("(") || editable.toString().contains(")") || editable.toString().contains("/") || editable.toString().contains("\\")){
                    Toast.makeText(UserSearchActivity.this, "Search must not contain special characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(searching){
                    finalSearch = editable.toString();
                    return;
                }

                boolean searchEmailOnly = false;

                if(editable.toString().contains(".")){
                    searchEmailOnly = true;
                    usernameRef = mDatabase;
                    numberRef = mDatabase;

                }
                else{
                    usernameRef = mDatabase.child("username").child(editable.toString());
                    numberRef = mDatabase.child("number").child(editable.toString());

                }

                lastSearch = editable.toString();
                finalSearch = editable.toString();

                searching = true;

//                initalizer = new ArrayList<UserSearchRecyclerView>();
//                adapter.notifyDataSetChanged();




                String emailParser;
                String emailParserArray[];
                if(editable.toString().contains(".")){
                    emailParserArray = editable.toString().split("\\.");
                    emailParser = emailParserArray[0];
                }
                else{
                    emailParser = editable.toString();
                }
                emailRef = mDatabase.child("email").child(emailParser);



                if(searchEmailOnly){
                    Log.d("WorkoutLog", "currently searching email only; " + editable.toString());
                    firebaseMultiQuery = new FirebaseMultiQuery(emailRef);
                    final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQuery.start();
                    allLoad.addOnCompleteListener(UserSearchActivity.this, new AllOnCompleteListener());
                }
                else{
                    Log.d("WorkoutLog", "currently searching; " + editable.toString());
                    firebaseMultiQuery = new FirebaseMultiQuery(usernameRef, numberRef, emailRef);
                    final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQuery.start();
                    allLoad.addOnCompleteListener(UserSearchActivity.this, new AllOnCompleteListener());
                }

            }
        });




    }




    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseMultiQuery != null){
            firebaseMultiQuery.stop();
        }
    }

    private void searchLastString(String lastString){

        if(initalizer.size() != 0){
            initalizer = new ArrayList<UserSearchRecyclerView>();
            adapter = new UserSearchAdapter(context, initalizer);
            rv.setAdapter(adapter);
        }

        lastSearch = lastString;

        boolean searchEmailOnly = false;

        if(lastString.contains(".")){
            searchEmailOnly = true;
            usernameRef = mDatabase;
            numberRef = mDatabase;

        }
        else{
            usernameRef = mDatabase.child("username").child(lastString);
            numberRef = mDatabase.child("number").child(lastString);

        }


        searching = true;

//        initalizer = new ArrayList<UserSearchRecyclerView>();
//        adapter.notifyDataSetChanged();



        String emailParser;
        String emailParserArray[];
        if(lastString.contains(".")){
            emailParserArray = lastString.split("\\.");
            emailParser = emailParserArray[0];
        }
        else{
            emailParser = lastString;
        }
        emailRef = mDatabase.child("email").child(emailParser);



        if(searchEmailOnly){
            Log.d("WorkoutLog", "currently searching email only; " + lastString);
            firebaseMultiQuery = new FirebaseMultiQuery(emailRef);
            final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQuery.start();
            allLoad.addOnCompleteListener(UserSearchActivity.this, new AllOnCompleteListener());
        }
        else{
            Log.d("WorkoutLog", "currently searching; " + lastString);
            firebaseMultiQuery = new FirebaseMultiQuery(usernameRef, numberRef, emailRef);
            final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQuery.start();
            allLoad.addOnCompleteListener(UserSearchActivity.this, new AllOnCompleteListener());
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

                DataSnapshot dataSnapshotUserName = result.get(usernameRef);
                String userNameID;
                if(dataSnapshotUserName == null){
                    userNameID = null;
                }
                else{
                    userNameID = dataSnapshotUserName.getValue(String.class);
                }


                DataSnapshot dataSnapshotEmail = result.get(emailRef);
                String userEmailID = dataSnapshotEmail.getValue(String.class);

                DataSnapshot dataSnapshotNumber = result.get(numberRef);
                String userNumberID;
                if(dataSnapshotNumber == null){
                    userNumberID = null;
                }
                else{
                    userNumberID = dataSnapshotNumber.getValue(String.class);
                }

                if(userNameID == null && userNumberID == null && userEmailID == null){


                    searching = false;
                    if(!lastSearch.matches(finalSearch)){
                        searchLastString(finalSearch);
                    }
                    return;
                }

                if(userNameID != null){

                    Log.d("WorkoutLog", "Found by username. ID: " + userNameID);
                    DatabaseReference innerAvatarRef = mDatabase.child("users").child(userNameID).child("avatar");
                    DatabaseReference innerUsernameRef = mDatabase.child("users").child(userNameID).child("username");
                    DatabaseReference innerNameRef = mDatabase.child("users").child(userNameID).child("name");

                    firebaseMultiQueryInner1 = new FirebaseMultiQuery(innerAvatarRef, innerNameRef, innerUsernameRef);
                    final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQueryInner1.start();
                    allLoad.addOnCompleteListener(UserSearchActivity.this, new AllOnInnerCompleteListener(firebaseMultiQueryInner1, userNameID, innerAvatarRef, innerNameRef, innerUsernameRef));

                }



                if(userEmailID != null){

                    Log.d("WorkoutLog", "Found by email. ID: " + userEmailID);
                    DatabaseReference innerAvatarRef = mDatabase.child("users").child(userEmailID).child("avatar");
                    DatabaseReference innerUsernameRef = mDatabase.child("users").child(userEmailID).child("username");
                    DatabaseReference innerNameRef = mDatabase.child("users").child(userEmailID).child("name");

                    firebaseMultiQueryInner2 = new FirebaseMultiQuery(innerAvatarRef, innerNameRef, innerUsernameRef);
                    final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQueryInner2.start();
                    allLoad.addOnCompleteListener(UserSearchActivity.this, new AllOnInnerCompleteListener(firebaseMultiQueryInner2, userEmailID, innerAvatarRef, innerNameRef, innerUsernameRef));


                }

                if(userNumberID != null){

                    Log.d("WorkoutLog", "Found by number. ID: " + userNumberID);
                    DatabaseReference innerAvatarRef = mDatabase.child("users").child(userNumberID).child("avatar");
                    DatabaseReference innerUsernameRef = mDatabase.child("users").child(userNumberID).child("username");
                    DatabaseReference innerNameRef = mDatabase.child("users").child(userNumberID).child("name");

                    firebaseMultiQueryInner3 = new FirebaseMultiQuery(innerAvatarRef, innerNameRef, innerUsernameRef);
                    final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseMultiQueryInner3.start();
                    allLoad.addOnCompleteListener(UserSearchActivity.this, new AllOnInnerCompleteListener(firebaseMultiQueryInner3, userNumberID, innerAvatarRef, innerNameRef, innerUsernameRef));


                }




            }
            else {

                if(!lastSearch.matches(finalSearch)){
                    searchLastString(finalSearch);
                }
                Log.d("WorkoutLog", task.getException().toString());
                // log the error or whatever you need to do
            }

        }
    }





    private class AllOnInnerCompleteListener implements OnCompleteListener<Map<DatabaseReference, DataSnapshot>> {

        DatabaseReference avatarRef;
        DatabaseReference usernameRef;
        DatabaseReference nameRef;
        String id;
        FirebaseMultiQuery fbmq;


        public AllOnInnerCompleteListener(FirebaseMultiQuery fbmq, String id, DatabaseReference avatarRef, DatabaseReference nameRef, DatabaseReference usernameRef) {
            this.fbmq = fbmq;
            this.id = id;
            this.avatarRef = avatarRef;
            this.nameRef = nameRef;
            this.usernameRef = usernameRef;
        }


        @Override
        public void onComplete(@NonNull Task<Map<DatabaseReference, DataSnapshot>> task) {
            if (task.isSuccessful()) {
                final Map<DatabaseReference, DataSnapshot> result = task.getResult();

                DataSnapshot avatar = result.get(avatarRef);
                DataSnapshot name = result.get(nameRef);
                DataSnapshot username = result.get(usernameRef);


                if(avatar.getValue(String.class) != null){
                    if(name.getValue(String.class)!=null && username.getValue(String.class)!=null){

                        final String nameFinal = name.getValue(String.class);
                        final String usernameFinal = username.getValue(String.class);
                        final String avatarFinal = avatar.getValue(String.class);

                        mDatabase.child("users").child(uID).child("following").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                UserSearchRecyclerView temp;
                                if(dataSnapshot.getValue(String.class) == null){

                                     temp= new UserSearchRecyclerView(id, nameFinal, usernameFinal, avatarFinal, false);
                                }

                                else{

                                    temp = new UserSearchRecyclerView(id, nameFinal, usernameFinal, avatarFinal, true);

                                }

                                initalizer.add(temp);
                                adapter.notifyDataSetChanged();
                                searching = false;
                                fbmq.stop();
                                if(!lastSearch.matches(finalSearch)){
                                    searchLastString(finalSearch);
                                }
                                return;

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        return;
                    }

                    else{


                        searching = false;
                        fbmq.stop();
                        if(!lastSearch.matches(finalSearch)){
                            searchLastString(finalSearch);
                        }
                        return;
                    }
                }
                else{
                    if(name.getValue(String.class)!=null && username.getValue(String.class)!=null){

                        final String nameFinal = name.getValue(String.class);
                        final String usernameFinal = username.getValue(String.class);
                        final String avatarFinal = avatar.getValue(String.class);

                        mDatabase.child("users").child(uID).child("following").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                UserSearchRecyclerView temp;
                                if(dataSnapshot.getValue(String.class) == null){

                                    temp= new UserSearchRecyclerView(id, nameFinal, usernameFinal, false);
                                }

                                else{

                                    temp = new UserSearchRecyclerView(id, nameFinal, usernameFinal, true);

                                }

                                initalizer.add(temp);
                                adapter.notifyDataSetChanged();
                                searching = false;
                                fbmq.stop();
                                if(!lastSearch.matches(finalSearch)){
                                    searchLastString(finalSearch);
                                }
                                return;

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        return;
                    }
                    else{

                        searching = false;
                        fbmq.stop();
                        if(!lastSearch.matches(finalSearch)){
                            searchLastString(finalSearch);
                        }
                        return;
                    }
                }
            }
            else {


                searching = false;
                fbmq.stop();
                Log.d("WorkoutLog", task.getException().toString());
                if(!lastSearch.matches(finalSearch)){
                    searchLastString(finalSearch);
                }
                return;


            }
        }
    }

    public void unfollowUser(String userID){
        mDatabase.child("users").child(uID).child("following").child(userID).removeValue();
        mDatabase.child("users").child(userID).child("followers").child(uID).removeValue();
    }

    public void followUser(String userID){
        mDatabase.child("users").child(uID).child("following").child(userID).setValue(userID);
        mDatabase.child("users").child(userID).child("followers").child(uID).setValue(uID);
    }

    public void viewUserProfile(String userID, Boolean following, int position){
        positionOfCurrentlyViewedProfile = position;
        Intent userProfileIntent = new Intent(context, ProfileActivity.class);
        Bundle b2 = new Bundle();
        b2.putString("userID", userID);
        b2.putBoolean("following", following);
        userProfileIntent.putExtras(b2);
        startActivityForResult(userProfileIntent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Boolean result=data.getBooleanExtra("following", false);
                initalizer.get(positionOfCurrentlyViewedProfile).setFollowing(result);
                adapter.notifyDataSetChanged();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

}
