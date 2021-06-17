package com.segg3.coursemanager.auth.register.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.ActivityRegisterBinding;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.User;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private String selectedRole = null;

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
                if (UIUtils.getFieldText(field).isEmpty()) {
                    field.setError(getString(R.string.error_empty_field));
                    field.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                    ok = false;
                }
            }

            if (selectedRole == null) {
                ok = false;
                binding.accountTypeMenu.setError(getString(R.string.error_empty_selection));
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(UIUtils.getFieldText(binding.emailInput)).matches()) {
                ok = false;
                binding.emailInput.setError(getString(R.string.invalid_email));
            }


            if (!ok)
                return;

            binding.signUpButton.setEnabled(false);
            // Create an account Here!!!
            User u = new User();
            u.type = selectedRole;
            u.password = UIUtils.getFieldText(binding.passwordInput);
            u.userName = UIUtils.getFieldText(binding.usernameInput);
            UsersDao.getInstance().addUser(u).onSuccess(o -> {
                UIUtils.createToast(getApplicationContext(), "Account Created!");
                Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainActivity);
            }).onFailure(e -> {
                UIUtils.createToast(getApplicationContext(), "The username is already taken!");
                binding.usernameInput.setError("Username already taken");
            }).onResult((o, e) -> binding.signUpButton.setEnabled(true));


        });
    }


}