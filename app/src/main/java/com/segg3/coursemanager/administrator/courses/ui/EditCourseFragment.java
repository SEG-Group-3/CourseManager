package com.segg3.coursemanager.administrator.courses.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.segg3.coursemanager.Course;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentEditCourseBinding;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.models.CoursesViewModel;

import org.jetbrains.annotations.NotNull;

public class EditCourseFragment extends Fragment {
    FragmentEditCourseBinding binding;
    CoursesViewModel coursesViewModel;
    int position = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_course, container, false);
        binding = FragmentEditCourseBinding.bind(view);
        coursesViewModel = new ViewModelProvider(requireActivity()).get(CoursesViewModel.class);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
            }
        });

        binding.cancelButton.setOnClickListener(this::onCancelEdit);
        binding.applyButton.setOnClickListener(this::onApplyEdit);

        binding.courseCodeInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.courseCodeInput));
        binding.courseNameInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.courseNameInput));

        setHasOptionsMenu(true);
        UIUtils.setToolbarTitle(getActivity(), "Edit Course");

        position = getArguments().getInt("position", -1);
        if (position != -1) {
            // Editing existing item
            Course beingEdited = coursesViewModel.getCourses().getValue().get(position);
            binding.courseNameInput.getEditText().setText(beingEdited.name);
            binding.courseCodeInput.getEditText().setText(beingEdited.code);
            binding.uidText.setText(beingEdited.getId());
        }
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        UIUtils.createYesNoMenu("Delete Item", "Do you really want to delete this course?", getActivity(), (dialog, which) -> {
            // Delete user here
            if (coursesViewModel.deleteCourse(position)) {
                UIUtils.createToast(getActivity().getApplicationContext(), "Course deleted");
            } else {
                UIUtils.createToast(getActivity().getApplicationContext(), "Nothing to be deleted");
            }
            UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
        });
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void onCancelEdit(View v) {
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
    }

    private void onApplyEdit(View v) {
        String name = binding.courseNameInput.getEditText().getText().toString();
        String code = binding.courseCodeInput.getEditText().getText().toString();

        TextInputLayout[] emptyCheckedFields = {binding.courseNameInput, binding.courseCodeInput};
        boolean ok = true;
        for (TextInputLayout field :
                emptyCheckedFields) {
            if (field.getEditText().getText().toString().isEmpty()) {
                field.setError(getString(R.string.error_empty_field));
                field.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
                ok = false;
            }
        }

        if (!ok)
            return;

        if (position != -1)
            if (coursesViewModel.editCourse(position, code, name)) {  // Edit existing item
                UIUtils.createToast(getActivity().getApplicationContext(), "Course Edited");
            } else {
                UIUtils.createToast(getActivity().getApplicationContext(), "Error editing course");
            }
        else {
            if (coursesViewModel.addCourse(code, name)) { // Add new item
                UIUtils.createToast(getActivity().getApplicationContext(), "Course Added");
            } else {
                UIUtils.createToast(getActivity().getApplicationContext(), "A course with the same name already exists!");
            }
        }


        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
    }
}