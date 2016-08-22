package com.example.davidyuan7536.workoutlog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.core.Context;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.davidyuan7536.workoutlog.PasswordValidator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    Intent i;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DatabaseReference mDatabase;

    FirebaseChecker firebaseChecker;
    DatabaseReference userNameRef;
    DatabaseReference numberRef;
    DatabaseReference emailRef;


    private EditText nameField;
    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText numberField;
    private EditText quoteField;

    Button signup;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        i = new Intent(SignupActivity.this, HomeActivity.class);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        this.mAuth = FirebaseAuth.getInstance();

        this.nameField = (EditText) findViewById(R.id.name);
        this.usernameField = (EditText)findViewById(R.id.username);
        this.emailField = (EditText) findViewById(R.id.email);
        this.passwordField = (EditText)findViewById(R.id.password);
        this.confirmPasswordField = (EditText) findViewById(R.id.passwordConfirm);
        this.numberField = (EditText)findViewById(R.id.number);
        this.quoteField = (EditText)findViewById(R.id.quote);

        this.signup= (Button)findViewById(R.id.signupBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nameField.getText().toString().matches("")){
                    Toast.makeText(SignupActivity.this, "Name field must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(usernameField.getText().toString().trim().matches("")){
                    Toast.makeText(SignupActivity.this, "Username field must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(!usernameField.getText().toString().trim().matches("[A-Za-z0-9]+")){
                    Toast.makeText(SignupActivity.this, "Username must be alphanumeric", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(!passwordField.getText().toString().matches(confirmPasswordField.getText().toString())){
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if(isValidEmail(email)){
                    PasswordValidator valid = new PasswordValidator();
                    if(valid.validate(password)){
                        userNameRef = mDatabase.child("username").child(usernameField.getText().toString().trim());

                        String emailFormated[] = emailField.getText().toString().split("\\.");
                        emailRef = mDatabase.child("email").child(emailFormated[0]);

                        if(!numberField.getText().toString().matches("")){

                            progressDialog = new ProgressDialog(SignupActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Creating User...");
                            progressDialog.show();

                            numberRef = mDatabase.child("number").child(numberField.getText().toString());
                            firebaseChecker = new FirebaseChecker(userNameRef, numberRef, emailRef);
                            final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                            allLoad.addOnCompleteListener(SignupActivity.this, new AllOnCompleteListener(true));
                        }
                        else{

                            progressDialog = new ProgressDialog(SignupActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Creating User...");
                            progressDialog.show();

                            numberRef = mDatabase.child("number").child(numberField.getText().toString());
                            firebaseChecker = new FirebaseChecker(userNameRef, numberRef, emailRef);
                            final Task<Map<DatabaseReference, DataSnapshot>> allLoad = firebaseChecker.start();
                            allLoad.addOnCompleteListener(SignupActivity.this, new AllOnCompleteListener(false));
                        }


                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Password must be 6-20 characters long and contain at least one letter and one number", Toast.LENGTH_LONG).show();
                        return;
                    }


                }
                else{
                    Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });




//        this.emailField = ((EditText)findViewById(R.id.signupEmailField));
//        this.passwordField = ((EditText)findViewById(R.id.signupPasswordField));
//        this.signupBtn = ((Button)findViewById(R.id.signupSignupBtn));
//
//        signupBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String email = emailField.getText().toString();
//                String password = passwordField.getText().toString();
//                if(isValidEmail(email)){
//                    PasswordValidator valid = new PasswordValidator();
//                    if(valid.validate(password)){
//                        createUser(email,password);
//                    }
//                    else{
//                        Toast.makeText(SignupActivity.this, "Password must be 6-20 characters long and contain at least one letter and one number", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//
//                }
//                else{
//                    Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//
//
//
//            }
//        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    if(i == null){
                        i = new Intent(SignupActivity.this, HomeActivity.class);
                    }

                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    startActivity(i);
                    KillActivity();

                } else {

                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignupActivity.this, "Failed to create account: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public class FirebaseChecker {

        private final HashSet<DatabaseReference> refs = new HashSet<>();
        private final HashMap<DatabaseReference, DataSnapshot> snaps = new HashMap<>();
        private final HashMap<DatabaseReference, ValueEventListener> listeners = new HashMap<>();

        public FirebaseChecker(final DatabaseReference... refs) {
            for (final DatabaseReference ref : refs) {
                add(ref);
            }
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


        boolean busernumber = false;

        public AllOnCompleteListener(boolean busernumber) {

            this.busernumber = busernumber;
        }

        @Override
        public void onComplete(@NonNull Task<Map<DatabaseReference, DataSnapshot>> task) {
            if (task.isSuccessful()) {
                final Map<DatabaseReference, DataSnapshot> result = task.getResult();

                DataSnapshot dataSnapshotUserName = result.get(userNameRef);
                String userId = dataSnapshotUserName.getValue(String.class);
                if(userId == null){

                }
                else{
                    Toast.makeText(SignupActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                DataSnapshot dataSnapshotEmail = result.get(emailRef);
                userId = dataSnapshotEmail.getValue(String.class);
                if(userId == null){

                }
                else{
                    Toast.makeText(SignupActivity.this, "Email already in use", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if(busernumber){
                    DataSnapshot dataSnapshotNumber = result.get(numberRef);
                    userId = dataSnapshotNumber.getValue(String.class);
                    if(userId == null){

                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Number already in use", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }
                }


                if(busernumber){
                    i.putExtra("number", numberField.getText().toString().trim());
                }
                if(!quoteField.getText().toString().matches("")){
                    i.putExtra("quote", quoteField.getText().toString());
                }
                i.putExtra("updateUser", "true");
                i.putExtra("username", usernameField.getText().toString().trim());
                i.putExtra("name" , nameField.getText().toString().trim());
                i.putExtra("email", emailField.getText().toString().trim());




                createUser(emailField.getText().toString().trim(), passwordField.getText().toString());

            }
            else {
                Log.d("WorkoutLog", task.getException().toString());
                Toast.makeText(SignupActivity.this, "Could not create account, please try againlater", Toast.LENGTH_SHORT).show();

            }

        }
    }

    private void KillActivity(){
        finish();
    }


}
