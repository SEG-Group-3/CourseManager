package com.segg3.coursemanager.administrator.main;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.auth.login.ui.LoginActivity;
import com.segg3.coursemanager.databinding.ActivityRegisterBinding;

public class MainAdministratorActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}