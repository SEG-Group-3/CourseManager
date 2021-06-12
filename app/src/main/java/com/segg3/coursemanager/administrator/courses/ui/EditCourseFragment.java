package com.segg3.coursemanager.administrator.courses.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentEditCourseBinding;
import com.segg3.coursemanager.shared.UIUtils;

import org.jetbrains.annotations.NotNull;

public class EditCourseFragment extends Fragment {
    FragmentEditCourseBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_edit_course, container, false);
        binding = FragmentEditCourseBinding.bind(view);

        binding.cancelCourseEditButton.setOnClickListener(this::onCancelEdit);
        binding.applyCourseEditButton.setOnClickListener(this::onApplyEdit);
        setHasOptionsMenu(true);
        UIUtils.setToolbarTitle(getActivity(), "Edit Course");
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        UIUtils.createYesNoMenu("Delete Item", "Do you really want to delete this item?", getActivity(), (dialog, which) -> {
            // Delete user here
            UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
            UIUtils.createToast(getActivity().getApplicationContext(), "Course deleted");
        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_course_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void onCancelEdit(View v){
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
    }

    private void onApplyEdit(View v){
        // TODO Input validation
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
        // TODO Update database
    }
}