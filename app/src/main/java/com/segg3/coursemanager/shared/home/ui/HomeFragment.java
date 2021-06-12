package com.segg3.coursemanager.shared.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.AccountAccess;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.User;
import com.segg3.coursemanager.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        User u = ((AccountAccess)AccountAccess.getInstance()).getUser();
        if (u != null ){
            binding.userNameField.setText(u.getUsername());
            binding.userTypeField.setText(u.getType());
        }
    }
}
