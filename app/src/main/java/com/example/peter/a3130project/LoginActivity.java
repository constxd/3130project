/* Note, some portions of code are adapted from https://firebase.google.com/docs/auth/android/password-auth */
package com.example.peter.a3130project;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



/*

 Main entry point for sign-in
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText et_password;
    private EditText et_email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        et_password = (EditText) findViewById(R.id.et_password);

        //TODO: change this
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        et_email = findViewById(R.id.et_email);

    }

    public void loginButtonClick(View view) {
	/* Function loginButtonClick
	   Called on email_signin_button
	   ---------
	   Initiates login.
	 */
        attemptLogin();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user) {
        /** Function: updateUI
         *
         Parameters:

         @param FirebaseUser user:
         the current session, or null if there is none

         Usage:
         Decides whether to switch intent to main activity
         */
        if (user == null) {
            setContentView(R.layout.activity_login);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Function: precheckLogin
     * ------------
     * Parameters:
     *
     * @param email:    String
     * @param password: String
     *                  <p>
     *                  Description:
     * @return true if email and passwords are properly formatted. False otherwise
     */

    private boolean precheckLogin(String email, String password) {
        //TODO: implement
        return true;
    }



/**
 *Takes the email and password stores in fields and attempts to login 
 *
 */
    private void attemptLogin() {

        // Reset errors.
        et_email.setError(null);
        et_password.setError(null);

        // Store values at the time of the login attempt.
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

   
        if (!(precheckLogin(email,password))) {
	    //Do something

        } else {
	    //TODO: refactor this to avoid lambda function
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("B", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("A", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }


                        }
			});

	}

	/* Get the currentuser, if logged in and retry */
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }



}

