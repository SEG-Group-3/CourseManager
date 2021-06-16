package com.segg3.coursemanager.auth.login.ui;

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
import com.segg3.coursemanager.User;
import com.segg3.coursemanager.auth.register.ui.RegisterActivity;
import com.segg3.coursemanager.databinding.ActivityLoginBinding;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.models.UsersViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private UsersViewModel users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setups the screen to activity_login.xml
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.passwordInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.passwordInput));
        binding.usernameInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.usernameInput));

        users = new ViewModelProvider(MainActivity.instance).get(UsersViewModel.class);

        binding.signUpButton.setOnClickListener((v -> {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent, options.toBundle());
        }));

        binding.logInButton.setOnClickListener(v -> {
            // If invalid username or password, shake!

            TextInputLayout[] emptyCheckedFields = {binding.passwordInput, binding.usernameInput};
            boolean ok = true;
            for (TextInputLayout field :
                    emptyCheckedFields) {
                if (getFieldText(field).isEmpty()) {
                    field.setError(getString(R.string.error_empty_field));
                    field.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                    ok = false;
                }
            }

            if (!ok)
                return;

            binding.logInButton.setEnabled(false);

            // Call authentication code authenticate Here!!!
            users.loginUser(getFieldText(binding.usernameInput), getFieldText(binding.passwordInput)).
                    onFalse(() -> {
                        UIUtils.createToast(getApplicationContext(), "The user name or password is incorrect");
                    }).
                    onTrue(() -> {
                        User u = users.getLoggedUser();
                        UIUtils.createToast(getApplicationContext(), "Welcome " + u.getUsername() + "!");
                        finish();
                    }).onResult((ignore) -> binding.logInButton.setEnabled(true));
        });
    }


    String getFieldText(TextInputLayout inputLayout) {
        return Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
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