package com.segg3.coursemanager.administrator.courses.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.fragments.ListFragmentTemplate;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AdminCourseViewFragment extends ListFragmentTemplate<Course, CourseListAdapter> {
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
        List<Course> filtered = CoursesDao.getInstance().searchCourse(query.toLowerCase());
        return new CourseListAdapter(filtered);
    }

    @Override
    public boolean onItemSwiped(@NonNull Course item) {
        CoursesDao.getInstance().deleteCourse(item.code);
        return false;
    }

    @Override
    public void onAddClicked(View v) {
        Fragment adminEditCourseFragment = new AdminEditCourseFragment();
        Bundle args = new Bundle();
        adminEditCourseFragment.setArguments(args);
        UIUtils.swipeFragmentRight(getParentFragmentManager(), adminEditCourseFragment);
    }

    @Override
    public void onItemClicked(@NonNull Course item) {
        Fragment adminEditCourseFragment = new AdminEditCourseFragment();
        Bundle args = new Bundle();
        args.putString("code", item.code);
        adminEditCourseFragment.setArguments(args);
        UIUtils.swipeFragmentRight(getParentFragmentManager(), adminEditCourseFragment);
    }
}
