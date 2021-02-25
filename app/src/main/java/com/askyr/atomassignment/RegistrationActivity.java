package com.askyr.atomassignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class RegistrationActivity extends AppCompatActivity {

    EditText edtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtUsername = findViewById(R.id.edtPersonName);
        //Toolbar toolbar = findViewById(R.id.toolbar);

    }

    public void goToHomeActivity() {
        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class); // Switching to registration screen activity
        startActivity(intent);
    }

    public void continueWithUsername(View view) {
        String userName = edtUsername.getText().toString();
        String message;

        if(userName.isEmpty()) {
            message = "Please enter a valid username!";
            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
        }
        else {
            message = "Redirecting to Home Screen!";
            Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
            goToHomeActivity();
        }
    }

}