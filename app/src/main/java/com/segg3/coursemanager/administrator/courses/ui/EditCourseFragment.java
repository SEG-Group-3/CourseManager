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
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentEditCourseBinding;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.viewmodels.CoursesViewModel;

import org.jetbrains.annotations.NotNull;

public class EditCourseFragment extends Fragment {
    FragmentEditCourseBinding binding;
    CoursesViewModel coursesViewModel;
    Course beingEdited;
    int position = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_edit_course, container, false);
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
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.edit_course));

        position = getArguments().getInt("position", -1);
        if (position != -1)
        {
            // Editing existing item
            beingEdited =coursesViewModel.getCourses().getValue().get(position);
            binding.courseNameInput.getEditText().setText(beingEdited.name);
            binding.courseCodeInput.getEditText().setText(beingEdited.code);
            binding.uidText.setText(beingEdited.getId());
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        UIUtils.createYesNoMenu("Delete Item", "Do you really want to delete this item?", getActivity(), (dialog, which) -> {
            // Delete user here
            if (position != -1){
                 CoursesDao.getInstance().deleteCourse(beingEdited.code);

                UIUtils.createToast(getActivity().getApplicationContext(), "Course deleted");
            } else{
                UIUtils.createToast(getActivity().getApplicationContext(), "No item to be deleted");
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




    private void onCancelEdit(View v){
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
    }

    private void onApplyEdit(View v){
        String name = binding.courseNameInput.getEditText().getText().toString();
        String code = binding.courseCodeInput.getEditText().getText().toString();

        TextInputLayout[] emptyCheckedFields = {binding.courseNameInput, binding.courseCodeInput};
        boolean ok = true;
        for (TextInputLayout field:
                emptyCheckedFields ) {
            if (field.getEditText().getText().toString().isEmpty()){
                field.setError(getString(R.string.error_empty_field));
                field.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
                ok = false;
            }
        }

        if (!ok)
            return;

        Course c = new Course();
        c.name = name;
        c.code = code;
        if (position != -1){
             CoursesDao.getInstance().editCourse(beingEdited.code, c);
        }
        else{
             CoursesDao.getInstance().addCourse(c);
        }


        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
    }
}