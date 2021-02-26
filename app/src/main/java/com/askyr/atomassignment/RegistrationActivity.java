package com.askyr.atomassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class RegistrationActivity extends AppCompatActivity {

    private EditText edtUsername;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtUsername = findViewById(R.id.edtPersonName);     // Referencing the editText UI component with id=edtPersonName

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);    // retrieving the default values for sharedPreferences

        String username = sharedPreferences.getString("username", null);      // extracting the value of key:username through sharedPreference if present else set to null(by default)
    }

    // Saving value of key:username through sharedPreferences
    private void savePref(String name) {
        sharedPreferences.edit().putString("username", name).apply();
    }

    // Redirecting to HomeActivity
    private void goToHomeActivity() {
        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class); // Switching to registration screen activity
        startActivity(intent);
        finish();   // Destroying the RegistrationActivity on pressing "continue" button so user can't come back to username registration screen
    }

    public void continueWithUsername(View view) {
        String usernameValue = edtUsername.getText().toString();
        String message;

        if(usernameValue.isEmpty()) {
            // empty string value in editText with id=edtPersonName

            message = "Please enter a valid username!";
            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();

            savePref(usernameValue);
        }
        else {
            // string value successfully retrieved from editText with id=edtPersonName

            message = "Redirecting to Home Screen!";

            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
            savePref(usernameValue);

            goToHomeActivity();
        }
    }
}