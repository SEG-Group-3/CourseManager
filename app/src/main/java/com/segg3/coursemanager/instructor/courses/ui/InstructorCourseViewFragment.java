package com.segg3.coursemanager.instructor.courses.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.fragments.ListFragmentTemplate;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.viewmodels.AuthViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class InstructorCourseViewFragment extends ListFragmentTemplate<Course, CourseListAdapter> {
    @Nullable
    User currentUser;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.courses));


        List<Course> courseL = new ArrayList<>(CoursesDao.getInstance().getCourses().getValue().values());
        setItems(new CourseListAdapter(courseL));

        currentUser = MainActivity.instance.auth.getUser().getValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Course> coursesL = new ArrayList<>(CoursesDao.getInstance().getCourses().getValue().values());
        setItems(new CourseListAdapter(coursesL));
        CoursesDao.getInstance().getCourses().observe(getViewLifecycleOwner(), courses -> {
            List<Course> coursesList = new ArrayList<>(courses.values());
            setItems(new CourseListAdapter(coursesList));
        });
    }

    @NonNull
    @Override
    public CourseListAdapter filterQuery(@NonNull String query, @NonNull List<Course> items) {
        List<Course> filtered = CoursesDao.getInstance().searchCourse(query);
        return new CourseListAdapter(filtered);
    }

    @Override
    public boolean onItemSwiped(@NonNull Course item) {
        if (item.instructor.equalsIgnoreCase(currentUser.userName)) {
            UIUtils.createToast(getContext(), "You have been unassigned from " + item.code);

            CoursesDao.getInstance().unAssignInstructor(item.code);
        } else {
            UIUtils.createToast(getContext(), "You cannot delete this course");
        }
        return false;
    }

    @Override
    public void onAddClicked(View v) {
        UIUtils.createToast(getContext(), "You cannot add courses!");
    }

    @Override
    public void onItemClicked(@NonNull Course item) {
        AuthViewModel auth = MainActivity.instance.auth;
        if (item.instructor.equals("")) {
            UIUtils.createYesNoMenu("Enter course", "Do you want to assign yourself to this course?",
                    getContext(),
                    (dialog, which) -> {
                        if (CoursesDao.getInstance().assignInstructor(auth.getUser().getValue().userName, item.code)) {
                            UIUtils.createToast(getContext(), "You have been assigned to this course");
                        } else {
                            UIUtils.createToast(getContext(), "An error has occurred");
                        }
                    });
        } else {
            UIUtils.createToast(getContext(), "This course is already taken!");
        }
    }
}
