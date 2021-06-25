package com.segg3.coursemanager.shared.home.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.AccountAccess;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.User;
import com.segg3.coursemanager.databinding.FragmentHomeBinding;

import com.segg3.coursemanager.shared.UIUtils;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        UIUtils.setToolbarTitle(getActivity(), getString(R.string.home));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        User u = ((AccountAccess) AccountAccess.getInstance()).getUser();
        if (u != null) {
            if (u.type.equals("Student")) {
                binding.imageView3.setImageDrawable(
                        AppCompatResources.getDrawable(binding.imageView3.getContext(), R.drawable.ic_student));
            } else if (u.type.equals("Instructor")) {
                binding.imageView3.setImageDrawable(
                        AppCompatResources.getDrawable(binding.imageView3.getContext(), R.drawable.ic_instructor));
            } else {
                binding.imageView3.setImageDrawable(
                        AppCompatResources.getDrawable(binding.imageView3.getContext(), R.drawable.ic_admin));
            }
            binding.userNameField.setText(u.userName);
            binding.userTypeField.setText(u.type);
        }
    }
}
