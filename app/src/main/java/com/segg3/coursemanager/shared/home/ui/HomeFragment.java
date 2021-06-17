package com.segg3.coursemanager.shared.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentHomeBinding;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.viewmodels.AuthViewModel;
import com.segg3.coursemanager.shared.viewmodels.UsersViewModel;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    AuthViewModel auth;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        UsersViewModel users = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);
        User u = auth.getUser().getValue();
        if (u != null) {
            switch (u.type.toLowerCase()) {
                case "student":
                    binding.roleImage.setImageDrawable(AppCompatResources.getDrawable(binding.roleImage.getContext(), R.drawable.ic_student));
                    break;
                case "instructor":
                    binding.roleImage.setImageDrawable(AppCompatResources.getDrawable(binding.roleImage.getContext(), R.drawable.ic_instructor));
                    break;
                case "admin":
                    binding.roleImage.setImageDrawable(AppCompatResources.getDrawable(binding.roleImage.getContext(), R.drawable.ic_admin));
                    break;
                default:
                    break;
            }
            binding.userNameField.setText(u.userName);
            binding.userTypeField.setText(u.type);
        }
    }
}

