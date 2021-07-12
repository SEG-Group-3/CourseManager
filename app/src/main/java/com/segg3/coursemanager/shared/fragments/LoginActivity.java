package com.segg3.coursemanager.shared.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.ActivityLoginBinding;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setups the screen to activity_login.xml
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.passwordInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.passwordInput));
        binding.usernameInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.usernameInput));

        auth = new ViewModelProvider(MainActivity.instance).get(AuthViewModel.class);

        binding.signUpButton.setOnClickListener((v -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent, options.toBundle());
        }));

        binding.logInButton.setOnClickListener(v -> {
            // If invalid username or password, shake!

            TextInputLayout[] emptyCheckedFields = {binding.passwordInput, binding.usernameInput};
            boolean ok = true;
            for (TextInputLayout field : emptyCheckedFields) {
                if (UIUtils.getFieldText(field) == null || UIUtils.getFieldText(field).isEmpty()) {
                    field.setError(getString(R.string.error_empty_field));
                    field.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                    ok = false;
                }
            }

            if (!ok)
                return;

            binding.logInButton.setEnabled(false);

            // Call authentication code authenticate Here!!!
            if (auth.login(UIUtils.getFieldText(binding.usernameInput), UIUtils.getFieldText(binding.passwordInput))) {
                UIUtils.createToast(getApplicationContext(), "Welcome!");
                MainActivity.instance.createUserMenu(auth.getUser().getValue());
                finish();
            } else
                UIUtils.createToast(getApplicationContext(), "The user name or password is incorrect");

            binding.logInButton.setEnabled(true);
        });
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}