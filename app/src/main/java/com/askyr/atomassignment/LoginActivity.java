package com.askyr.atomassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private Button buttonGoogle;
    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 101;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();             // Referencing the FirebaseAuth Object for this instance

        buttonGoogle = findViewById(R.id.btnGoogle);    // referencing the button UI Component with id=btnGoogle

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);          // retrieving the default values for sharedPreferences

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);     // extracting the value of key:isLoggedIn through sharedPreference if present else set to false(by default)
        String username = sharedPreferences.getString("username", null);            // extracting the value of key:username through sharedPreference if present else set to null(by default)

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if(isLoggedIn) {
            // already signed in via Google account
            signIn();
        }
        else if(username != null) {
            // already signed in as a Guest
            goToHomeActivity();
        }
    }

    // Function to save the isLoggedIn value in sharedPreferences
    private void savePref(boolean value) {
        sharedPreferences.edit().putBoolean("isLoggedIn", value).apply();
    }

    // Function initializing the Google account based authentication process through Firebase
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LoginActivity.this,user.getEmail(),Toast.LENGTH_SHORT).show();   // Showing email used to log-in as toast message

                    savePref(true);     // Saving the isLoggedIn value as true in sharedPreferences
                    //updateUI(user);
                    goToHomeActivity();
                }
                else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(LoginActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();    // Showing log-in failure as toast message

                    savePref(false);    // Saving the isLoggedIn value as true in sharedPreferences
                    //updateUI(null);
                }
            }
        });
    }

    // Redirecting to RegistrationActivity
    private void goToRegistrationActivity() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class); // Switching to registration screen activity
        startActivity(intent);
        finish();   // destroying LoginActivity
    }

    // Redirecting to HomeActivity
    private void goToHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class); // Switching to home screen activity
        startActivity(intent);
        finish();   // destroying LoginActivity
    }

    // Redirecting to HomeActivity
    private void updateUI(FirebaseUser user) {
        Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();   // destroying LoginActivity()
    }

    // onClickListener for id=btnGoogle
    public void continueWithGoogle(View view) {
        signIn();
    }

    // onClickListener for id=btnGuest
    public void continueAsGuest(View view) {
        goToRegistrationActivity();
    }
}
