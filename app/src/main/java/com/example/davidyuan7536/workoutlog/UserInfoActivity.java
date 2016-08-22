package com.example.davidyuan7536.workoutlog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    Context context;
    Toolbar toolbar;

    FirebaseUser user;
    private String uID;
    private DatabaseReference mDatabase;

    MLRoundedImageView userAvatar;

    String name;
    String userName;
    String quote;
    String email;
    String number;

    FirebaseChecker firebaseChecker;
    DatabaseReference userNameRef;
    DatabaseReference numberRef;
    DatabaseReference emailRef;

    String setName;
    String setUserName;
    String setQuote;
    String setEmail;
    String setNumber;

    boolean checkingUserName;
    boolean userNameInUse;
    boolean checkingEmail;
    boolean emailInUse;
    boolean checkingNumber;
    boolean numberInUse;

    boolean stopChecker;


    private void KillActivity(){
        finish();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("username", userName);
        intent.putExtra("userquote", quote);
        setResult(Activity.RESULT_OK, intent);
        KillActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                boolean bname = false;
                boolean busername = false;
                boolean buserquote = false;
                boolean buseremail = false;
                boolean busernumber = false;

                userNameRef = mDatabase.child("username").child(setUserName);
                numberRef = mDatabase.child("number").child(setNumber);


                if(!setName.equals("") && !setUserName.equals("")){

                    if(!setEmail.equals(email)){
                        if(!isValidEmail(setEmail)){
                            Toast.makeText(UserInfoActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }

                    if(!setName.equals(name)){
                        bname = true;
                    }

                    if(!setQuote.equals(quote)){
                        buserquote = true;
                    }


                    if(!setUserName.equals(userName)){
                        if(!setNumber.equals(number)){
                            if(!setEmail.equals(email) ){
                                buseremail = true;
                                busername = true;
                                busernumber = true;
                                String emailFormated[] = setEmail.split("\\.");
                                emailRef = mDatabase.child("email").child(emailFormated[0]);
                                firebaseChecker = new FirebaseChecker(userNameRef, numberRef, emailRef);
                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                                allLoad.addOnCompleteListener(UserInfoActivity.this, new AllOnCompleteListener(bname, busername, buserquote, buseremail, busernumber));
                            }
                            else{
                                busername = true;
                                busernumber = true;
                                firebaseChecker = new FirebaseChecker(userNameRef, numberRef);
                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                                allLoad.addOnCompleteListener(UserInfoActivity.this, new AllOnCompleteListener(bname, busername, buserquote, buseremail, busernumber));
                            }

                        }
                        else{
                            if(!setEmail.equals(email) ){
                                buseremail = true;
                                busername = true;
                                String emailFormated[] = setEmail.split("\\.");
                                emailRef = mDatabase.child("email").child(emailFormated[0]);
                                firebaseChecker = new FirebaseChecker(userNameRef, emailRef);
                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                                allLoad.addOnCompleteListener(UserInfoActivity.this, new AllOnCompleteListener(bname, busername, buserquote, buseremail, busernumber));

                            }
                            else{
                                busername = true;
                                firebaseChecker = new FirebaseChecker(userNameRef);
                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                                allLoad.addOnCompleteListener(UserInfoActivity.this, new AllOnCompleteListener(bname, busername, buserquote, buseremail, busernumber));
                            }

                        }
                    }
                    else{
                        if(!setNumber.equals(number)){
                            if(!setEmail.equals(email) ){
                                buseremail = true;
                                busernumber = true;
                                String emailFormated[] = setEmail.split("\\.");
                                emailRef = mDatabase.child("email").child(emailFormated[0]);
                                firebaseChecker = new FirebaseChecker(numberRef, emailRef);
                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                                allLoad.addOnCompleteListener(UserInfoActivity.this, new AllOnCompleteListener(bname, busername, buserquote, buseremail, busernumber));

                            }
                            else{
                                busernumber = true;
                                firebaseChecker = new FirebaseChecker(numberRef);
                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                                allLoad.addOnCompleteListener(UserInfoActivity.this, new AllOnCompleteListener(bname, busername, buserquote, buseremail, busernumber));
                            }

                        }
                        else{
                            if(!setEmail.equals(email) ){
                                buseremail = true;
                                String emailFormated[] = setEmail.split("\\.");
                                emailRef = mDatabase.child("email").child(emailFormated[0]);
                                firebaseChecker = new FirebaseChecker(emailRef);
                                final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                                allLoad.addOnCompleteListener(UserInfoActivity.this, new AllOnCompleteListener(bname, busername, buserquote, buseremail, busernumber));
                            }
                            else{
                                if(bname){
                                    saveNameUserInfo("name", setName);
                                    name = setName;
                                }

                                if(buserquote){
                                    saveUserInfo("quote", setQuote);
                                    quote = setQuote;
                                }

                                Toast.makeText(UserInfoActivity.this, "User info saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }



                    return true;

                }
                else{
                    if(setName.equals("")){
                        Toast.makeText(UserInfoActivity.this, "Name must be filled", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    else{
                        Toast.makeText(UserInfoActivity.this, "User name must be filled", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                }
            case R.id.cancel:

                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("username", userName);
                intent.putExtra("userquote", quote);
                setResult(Activity.RESULT_OK, intent);
                KillActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(stopChecker){
            firebaseChecker.stop();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        stopChecker = false;

        checkingUserName = false;
        userNameInUse = false;
        checkingEmail = false;
        emailInUse = false;
        checkingNumber = false;
        numberInUse = false;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profile");

        mDatabase = FirebaseDatabase.getInstance().getReference();


        user = FirebaseAuth.getInstance().getCurrentUser();
        uID = user.getUid();


        userAvatar = (MLRoundedImageView) findViewById(R.id.userAvatar);

        ValueEventListener userAvatarListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String link = dataSnapshot.getValue(String.class);
                if(link == null){

                }
                else{
                    Picasso.with(context).load(link).fit().centerInside().into(userAvatar);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").child(uID).child("avatar").addValueEventListener(userAvatarListener);





        final EditText name = (EditText) findViewById(R.id.name);
        getValue("name", name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().matches("")){
                    name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_face_red_24dp, 0, 0, 0);
                }
                else{
                    name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_face_black_24dp, 0, 0, 0);
                }

                setName = editable.toString();
            }
        });



        final EditText userName = (EditText) findViewById(R.id.userName);
        getValue("username", userName);
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().matches("")){
                    userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_red_24dp, 0, 0, 0);
                }
                else{
                    checkingUserName = true;

                    mDatabase.child("username").child(editable.toString()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String result = dataSnapshot.getValue(String.class);
                            if(result == null){
                                userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
                                userNameInUse = false;
                                checkingUserName = false;
                                return;
                            }
                            else if(result.equals(uID)){
                                userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
                                userNameInUse = false;
                                checkingUserName = false;
                                return;
                            }
                            else{
                                userName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_red_24dp, 0, 0, 0);
                                userNameInUse = true;
                                checkingUserName = false;
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                setUserName = editable.toString();
            }
        });


        final EditText quote = (EditText) findViewById(R.id.userQuote);
        getValue("quote", quote);
        quote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                setQuote = editable.toString();
            }
        });



        final EditText email = (EditText) findViewById(R.id.userEmail);
        getValue("email", email);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(!isValidEmail(editable.toString())){
                    email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_red_24dp, 0, 0, 0);
                }
                else{

                    String formated;
                    if(editable.toString().contains(".")){
                        String temp[]= editable.toString().split("\\.");
                        formated = temp[0];
                    }
                    else{
                        formated = editable.toString();
                    }
                    mDatabase.child("email").child(formated).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String result = dataSnapshot.getValue(String.class);
                            if(result == null){
                                email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_black_24dp, 0, 0, 0);
                                return;
                            }
                            else if(result.equals(uID)){
                                email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_black_24dp, 0, 0, 0);
                                return;
                            }
                            else{
                                email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_red_24dp, 0, 0, 0);
                                return;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

                setEmail = editable.toString();
            }
        });


        final  EditText number= (EditText) findViewById(R.id.userNumber);
        getValue("number", number);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {



                mDatabase.child("number").child(editable.toString()).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String result = dataSnapshot.getValue(String.class);
                        if(result == null){
                            number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stay_primary_portrait_black_24dp, 0, 0, 0);
                            return;
                        }
                        else if(result.equals(uID)){
                            number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stay_primary_portrait_black_24dp, 0, 0, 0);
                            return;
                        }
                        else{
                            number.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stay_primary_portrait_red_24dp, 0, 0, 0);
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                setNumber = editable.toString();
            }
        });




    }

    private void getValue(final String key, final EditText et){

        ValueEventListener nameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(key.equals("email")){
                    FirebaseEmail result = dataSnapshot.getValue(FirebaseEmail.class);
                    if (result == null) {

                        email = "";
                        setEmail = "";

                        return;
                    }
                    else{

                        et.setText(result.getProvider()+ "." + result.getExtension());
                        email = result.getProvider()+ "." + result.getExtension();
                        setEmail =result.getProvider()+ "." + result.getExtension();
                        return;
                    }
                }
                else{
                    String result = dataSnapshot.getValue(String.class);
                    if (result == null) {

                        if (key.equals("name")) {
                            name = "";
                            setName = "";

                        }
                        else if (key.equals("username")) {
                            userName = "";
                            setUserName = "";

                        }
                        else if(key.equals("quote")){
                            quote = "";
                            setQuote = "";
                        }
                        else if(key.equals("number")){
                            number = "";
                            setNumber = "";
                        }

                        return;
                    }
                    else{
                        et.setText(result);


                        if (key.equals("name")) {
                            name = result;
                            setName = result;

                        }
                        else if (key.equals("username")) {
                            userName = result;
                            setUserName = result;

                        }
                        else if(key.equals("quote")){
                            quote = result;
                            setQuote = result;
                        }
                        else if(key.equals("number")){
                            number = result;
                            setNumber = result;
                        }
                        return;

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.child("users").child(uID).child(key).addListenerForSingleValueEvent(nameListener);
    }

    private void saveUserInfo(String key, String value){
        mDatabase.child("users").child(uID).child(key).setValue(value);
    }

    private void saveUniqueUserInfo(String oldKey, String key, String value){
        mDatabase.child("users").child(uID).child(key).setValue(value);
        mDatabase.child(key).child(oldKey).removeValue();
        mDatabase.child(key).child(value).setValue(uID);
    }

    private void saveNameUserInfo(String key, String value){
        mDatabase.child("users").child(uID).child(key).setValue(value);
        mDatabase.child(key).child(uID).setValue(value);
    }

    private void saveEmailUserInfo(String oldKey, String key, String provider, String extension){

        mDatabase.child("users").child(uID).child(key).child("provider").setValue(provider);
        mDatabase.child("users").child(uID).child(key).child("extension").setValue(extension);

        mDatabase.child(key).child(oldKey).removeValue();
        mDatabase.child(key).child(provider).setValue(uID);
    }


    public class FirebaseChecker {

        private final HashSet<DatabaseReference> refs = new HashSet<>();
        private final HashMap<DatabaseReference, DataSnapshot> snaps = new HashMap<>();
        private final HashMap<DatabaseReference, ValueEventListener> listeners = new HashMap<>();

        public FirebaseChecker(final DatabaseReference... refs) {
            for (final DatabaseReference ref : refs) {
                add(ref);
            }
            stopChecker = true;
        }

        public void add(final DatabaseReference ref) {
            refs.add(ref);
        }

        public Task<Map<DatabaseReference, DataSnapshot>> start() {

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

        boolean bname = false;
        boolean busername  = false;
        boolean buserquote = false;
        boolean buseremail = false;
        boolean busernumber = false;

        public AllOnCompleteListener(boolean bname, boolean busername, boolean buserquote, boolean buseremail, boolean busernumber) {
            this.bname = bname;
            this.busername = busername;
            this.buserquote = buserquote;
            this.buseremail = buseremail;
            this.busernumber = busernumber;
        }

        @Override
        public void onComplete(@NonNull Task<Map<DatabaseReference, DataSnapshot>> task) {
            if (task.isSuccessful()) {
                final Map<DatabaseReference, DataSnapshot> result = task.getResult();

                if(busername){
                    DataSnapshot dataSnapshotUserName = result.get(userNameRef);
                    String userId = dataSnapshotUserName.getValue(String.class);
                    if(userId == null){

                    }
                    else if(userId == uID){

                    }
                    else{
                        Toast.makeText(UserInfoActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(busernumber){
                    DataSnapshot dataSnapshotNumber = result.get(numberRef);

                    String userId = dataSnapshotNumber.getValue(String.class);
                    if(userId == null){

                    }
                    else if(userId == uID){

                    }
                    else{
                        Toast.makeText(UserInfoActivity.this, "Number already in use", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(buseremail){
                    DataSnapshot dataSnapshotEmail = result.get(emailRef);
                    String userId = dataSnapshotEmail.getValue(String.class);
                    if(userId == null){

                    }
                    else if(userId == uID){

                    }
                    else{
                        Toast.makeText(UserInfoActivity.this, "Email already in use", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(bname){
                    saveNameUserInfo("name", setName);
                    name = setName;
                }
                if(busername){
                    saveUniqueUserInfo(userName, "username", setUserName);
                    userName = setUserName;
                }
                if(buserquote){
                    saveUserInfo("quote", setQuote);
                    quote = setQuote;
                }
                if(buseremail){
                    String provider[] = setEmail.split("\\.");
                    String oldProvider[] = email.split("\\.");
                    saveEmailUserInfo(oldProvider[0], "email", provider[0], provider[1]);
                    email = setEmail;
                }
                if(busernumber){
                    saveUniqueUserInfo(number, "number", setNumber);
                    number = setNumber;
                }
                Toast.makeText(UserInfoActivity.this, "User info saved", Toast.LENGTH_SHORT).show();

            }
            else {
                Log.d("WorkoutLog", task.getException().toString());
                Toast.makeText(UserInfoActivity.this, "Could not save user info, please try again later", Toast.LENGTH_SHORT).show();

            }

        }
    }


    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
