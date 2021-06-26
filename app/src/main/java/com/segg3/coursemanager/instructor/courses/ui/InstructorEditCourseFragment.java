package com.segg3.coursemanager.instructor.courses.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentInstructorEditCourseBinding;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

public class InstructorEditCourseFragment extends Fragment {
    FragmentInstructorEditCourseBinding binding;
    Course beingEdited;
    int position = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstructorEditCourseBinding.inflate(inflater);
        View view=binding.getRoot();

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
            }
        });

        binding.cancelButton.setOnClickListener(this::onCancelEdit);
        binding.applyButton.setOnClickListener(this::onApplyEdit);

        binding.courseDescriptionInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.courseDescriptionInput));
        binding.courseCapacityInput.getEditText().addTextChangedListener(UIUtils.createTextErrorRemover(binding.courseCapacityInput));

        setHasOptionsMenu(true);
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.edit_course));
        String courseCode= getArguments().getString("code");
        beingEdited=CoursesDao.getInstance().getCourse(courseCode);
        binding.courseCapacityInput.getEditText().setText(String.valueOf(beingEdited.capacity));
        binding.courseDescriptionInput.getEditText().setText(beingEdited.description);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        UIUtils.createYesNoMenu("Unassign from course", "Do you really want to unassign this course?", getActivity(), (dialog, which) -> {
            CoursesDao.getInstance().unAssignInstructor(beingEdited.code);
            UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
        });
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




    private void onCancelEdit(View v){
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
    }

    private void onApplyEdit(View v){
//        String name = binding.courseNameInput.getEditText().getText().toString();
//        String code = binding.courseCodeInput.getEditText().getText().toString();
//
//        TextInputLayout[] emptyCheckedFields = {binding.courseNameInput, binding.courseCodeInput};
//        boolean ok = true;
//        for (TextInputLayout field:
//                emptyCheckedFields ) {
//            if (field.getEditText().getText().toString().isEmpty()){
//                field.setError(getString(R.string.error_empty_field));
//                field.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake));
//                ok = false;
//            }
//        }
//
//        if (!ok)
//            return;
//
//        // TODO create new course object to update
//        if (position != -1){
//            // ## Implement Here
//            // CoursesDao.getInstance().editCourse()
//            // ##  Old function for reference
//            // coursesViewModel.editCourse(position, name, code);  // Edit existing item
//        }
//        else{
//            // ## Implement Here
//            // CoursesDao.getInstance().addCourse()
//            // ##  Old function for reference
//            // coursesViewModel.addCourse(name, code); // Add new item
//        }
//
//
//        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new CourseViewFragment());
    }
}