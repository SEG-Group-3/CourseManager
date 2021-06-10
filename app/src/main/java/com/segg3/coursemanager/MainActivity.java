package com.segg3.coursemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.segg3.coursemanager.auth.login.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onStart() {
        super.onStart();

        // If credentials were not stored go to login screen
        ActivityOptions options = ActivityOptions.makeBasic();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent, options.toBundle());
    }
}