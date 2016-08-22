package com.example.davidyuan7536.workoutlog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Context context;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    ProgressDialog progressDialog;

    Button loginBtn;
    TextView signupBtn;
    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        context = this;
        this.mAuth = FirebaseAuth.getInstance();


        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupBtn = (TextView) findViewById(R.id.signupBtn);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(emailField.getText().toString().equals("")){

                    Toast.makeText(MainActivity.this, "Email field empty", Toast.LENGTH_LONG).show();
                    return;

                }
                else if(passwordField.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Please input your password", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    if(!isValidEmail(emailField.getText().toString().trim())){
                        Toast.makeText(context, "Invalid email", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        PasswordValidator pv = new PasswordValidator();
                        if(pv.validate(passwordField.getText().toString())){
                            progressDialog = new ProgressDialog(MainActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("Authenticating...");
                            progressDialog.show();
                            signInWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString());
                        }
                        else{
                            Toast.makeText(context, "Invalid password", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                }

            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SignupActivity.class);
                startActivity(i);
            }
        });



        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
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

    private void signInWithEmailAndPassword(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    class PasswordValidator{


            private Pattern pattern;
            private Matcher matcher;

            private static final String PASSWORD_PATTERN =
                    "((?=.*\\d)(?=.*[a-z]).{6,20})";

            public PasswordValidator() {
            pattern = Pattern.compile(PASSWORD_PATTERN);
        }

            /**
             * Validate password with regular expression
             * @param password password for validation
             * @return true valid password, false invalid password
             */

        public boolean validate(final String password) {

            matcher = pattern.matcher(password);
            return matcher.matches();

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

    private void KillActivity(){
        finish();
    }

}
