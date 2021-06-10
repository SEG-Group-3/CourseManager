package com.segg3.coursemanager.auth.login.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.auth.register.ui.RegisterActivity;
import com.segg3.coursemanager.databinding.ActivityLoginBinding;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setups the screen to activity_login.xml
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Objects.requireNonNull(binding.usernameInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                binding.usernameInput.setErrorEnabled(false);
                }
            });

        Objects.requireNonNull(binding.passwordInput.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                binding.passwordInput.setErrorEnabled(false);
            }
        });


        binding.signUpButton.setOnClickListener((v -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent, options.toBundle());
        }));

        binding.logInButton.setOnClickListener(v -> {
            // If invalid username or password, shake!

            TextInputLayout[] emptyCheckedFields = {binding.passwordInput, binding.usernameInput};
            boolean ok = true;
            for (TextInputLayout field:
                    emptyCheckedFields ) {
                if (getFieldText(field).isEmpty()){
                    field.setError(getString(R.string.error_empty_field));
                    field.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                    ok = false;
                }
            }

            if (!ok)
                return;


            // Call authentication code authenticate Here!!!


            // if (authentication passes)
            // go to the "main logged in user" activity
            // else
            {
                Toast.makeText(getApplicationContext(),
                        R.string.login_failed,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    String getFieldText(TextInputLayout inputLayout){
        return  Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}