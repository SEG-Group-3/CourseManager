package com.segg3.coursemanager.auth.register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.ActivityRegisterBinding;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.models.UsersViewModel;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private String selectedRole = null;
    private UsersViewModel users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.closeButton.setOnClickListener(v -> finish());


        TextInputLayout[] emptyCheckedFields = {binding.usernameInput, binding.nameInput,
                binding.passwordInput, binding.emailInput};

        for (TextInputLayout field :
                emptyCheckedFields) {
            Objects.requireNonNull(field.getEditText()).addTextChangedListener(UIUtils.createTextErrorRemover(field));
        }

        users = new ViewModelProvider(this).get(UsersViewModel.class);
        users.getUsers();

        String[] items = {"Student", "Instructor"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, items);
        binding.accountTypeDropdown.setAdapter(adapter);

        binding.accountTypeDropdown.setOnItemClickListener((parent, arg1, position, arg3) -> {
            binding.accountTypeMenu.setErrorEnabled(false);
            selectedRole = (String) parent.getItemAtPosition(position);
        });

        binding.signUpButton.setOnClickListener(v -> {
            // If invalid username or password, shake!
            boolean ok = true;
            for (TextInputLayout field :
                    emptyCheckedFields) {
                if (getFieldText(field).isEmpty()) {
                    field.setError(getString(R.string.error_empty_field));
                    field.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                    ok = false;
                }
            }

            if (selectedRole == null) {
                ok = false;
                binding.accountTypeMenu.setError(getString(R.string.error_empty_selection));
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(getFieldText(binding.emailInput)).matches()) {
                ok = false;
                binding.emailInput.setError(getString(R.string.invalid_email));
            }


            if (!ok)
                return;

            binding.signUpButton.setEnabled(false);
            // Create an account Here!!!
            boolean added = users.addUser(
                    getFieldText(binding.usernameInput),
                    getFieldText(binding.passwordInput),
                    getFieldText(binding.accountTypeMenu));
            if (added) {

                UIUtils.createToast(getApplicationContext(), "Account Created!");

                users.loginUser(getFieldText(binding.usernameInput), getFieldText(binding.passwordInput)).
                        onFalse(() -> UIUtils.createToast(getApplicationContext(), "Something really bad happened"));

                Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainActivity);
            } else {

                // Error adding user
                UIUtils.createToast(getApplicationContext(), "User already exists!");
                binding.usernameInput.setError("Username already exists");
            }
            binding.signUpButton.setEnabled(true);

        });

    }


    String getFieldText(TextInputLayout inputLayout) {
        return Objects.requireNonNull(inputLayout.getEditText()).getText().toString();
    }


}