package com.segg3.coursemanager.administrator.users.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentEditUserBinding;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.viewmodels.UsersViewModel;

import org.jetbrains.annotations.NotNull;

public class EditUserFragment extends Fragment {
    FragmentEditUserBinding binding;
    UsersViewModel usersViewModel;

    int position = -1;
    User beingEdited;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
        binding = FragmentEditUserBinding.bind(view);
        usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new UsersViewFragment());
            }
        });

        binding.cancelButton.setOnClickListener(this::onCancelEdit);
        binding.applyButton.setOnClickListener(this::onApplyEdit);


        String[] items = {"Student", "Instructor", "Admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, items);


        binding.accountTypeDropdown.setOnItemClickListener(
                (parent, arg1, position, arg3) -> {
                    binding.accountTypeMenu.setErrorEnabled(false);
                });

        binding.inputUsername.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.inputUsername));
        binding.inputPassword.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.inputPassword));

        setHasOptionsMenu(true);
        UIUtils.setToolbarTitle(getActivity(), "Edit User");

        position = getArguments().getInt("position", -1);
        if (position != -1) {
            // Editing existing item
            beingEdited = usersViewModel.getUsers().getValue().get(position);
            binding.inputUsername.getEditText().setText(beingEdited.userName);
            binding.inputPassword.getEditText().setText(beingEdited.password);
            binding.accountTypeDropdown.setText(beingEdited.type);
            binding.uidText.setText(beingEdited.id);
        }
        binding.accountTypeDropdown.setAdapter(adapter);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        UIUtils.createYesNoMenu("Delete Item", "Do you really want to delete this user?", getActivity(), (dialog, which) -> {
            // Delete user here
            UsersDao.getInstance().deleteUser(beingEdited.userName).
                    onSuccess(o ->
                            UIUtils.createToast(getActivity().getApplicationContext(), "User deleted")).
                    onFailure(e ->
                            UIUtils.createToast(getActivity().getApplicationContext(), "Nothing to be deleted"));

            UIUtils.swipeFragmentLeft(getParentFragmentManager(), new UsersViewFragment());
        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void onCancelEdit(View v) {
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new UsersViewFragment());
    }

    private void onApplyEdit(View v) {

        TextInputLayout[] emptyCheckedFields = {binding.inputUsername, binding.inputPassword};

        boolean ok = true;
        for (TextInputLayout field :
                emptyCheckedFields) {
            if (field.getEditText() == null || field.getEditText().getText().toString().isEmpty()) {
                field.setError(getString(R.string.error_empty_field));
                field.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
                ok = false;
            }
        }

        if (binding.accountTypeDropdown.getText().toString().isEmpty()){
            binding.accountTypeMenu.setError(getString(R.string.error_empty_field));
            binding.accountTypeMenu.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
            ok = false;
        }

        if (!ok)
            return;

        String name = binding.inputUsername.getEditText().getText().toString();
        String password = binding.inputPassword.getEditText().getText().toString();

        User user = new User();
        user.userName = name;
        user.password = password;
        user.type = binding.accountTypeDropdown.getText().toString();

        if (position != -1){
            UsersDao.getInstance().editUser(beingEdited.userName, user).
                    onSuccess(o ->
                            UIUtils.createToast(getActivity().getApplicationContext(), "User Edited")).
                    onFailure(e ->
                            UIUtils.createToast(getActivity().getApplicationContext(), "Error editing user"));
        } else{
            UsersDao.getInstance().addUser(user).
                    onSuccess(o ->
                            UIUtils.createToast(getActivity().getApplicationContext(), "User Added")).
                    onFailure(e ->
                            UIUtils.createToast(getActivity().getApplicationContext(), "An user with the same name already exists!"));
        }


        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new UsersViewFragment());
    }
}