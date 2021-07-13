package com.segg3.coursemanager.student.courses.ui;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.fragments.ListFragmentTemplate;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class StudentCourseViewFragment extends ListFragmentTemplate<Course, CourseListAdapter> {

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.courses));


        List<Course> courseL = new ArrayList<>(CoursesDao.getInstance().getCourses().getValue().values());
        setItems(new CourseListAdapter(courseL));
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
        return false;
    }

    @Override
    public void onAddClicked(View v) {
        UIUtils.createToast(getContext(), "You cannot add courses!");
    }

    @Override
    public void onItemClicked(@NonNull Course item) {
        //Finds the selected item

        Fragment frag = new StudentInspectCourseViewFragment();

        Bundle args = new Bundle();
        args.putString("code", item.code);
        args.putBoolean("viewingAll", true);
        frag.setArguments(args);

        UIUtils.swipeFragmentRight(getParentFragmentManager(), frag);
    }
}

