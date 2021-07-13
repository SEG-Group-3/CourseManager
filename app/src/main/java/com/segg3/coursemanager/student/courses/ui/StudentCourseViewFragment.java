package com.segg3.coursemanager.student.courses.ui;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.filter.DayOfWeekFilter;
import com.segg3.coursemanager.shared.fragments.ListFragmentTemplate;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class StudentCourseViewFragment extends ListFragmentTemplate<Course, CourseListAdapter> {
    Menu filterMenu;
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.courses));
        setHasOptionsMenu(true);

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
        boolean[] dayOfWeekQeury = new boolean[7];
        try
        {
            for (int i = 0; i < 7; i++)
            {
                MenuItem dayItem = filterMenu.getItem(i);
                dayOfWeekQeury[i] = dayItem.isChecked();
            }
        }
        catch (Exception e)
        {
            for(int i = 0; i < 7; i++)
            {
                dayOfWeekQeury[i] = true;
            }
        }

        List<Course> filtered = CoursesDao.getInstance().searchCourse(query);

        DayOfWeekFilter dayOfWeekFilter = new DayOfWeekFilter();

        filtered = dayOfWeekFilter.search(dayOfWeekQeury, filtered);

        return new CourseListAdapter(filtered);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        item.setChecked(!item.isChecked());
        forceUpdate();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.course_filter_menu, menu);
        filterMenu = menu;

        super.onCreateOptionsMenu(menu, inflater);
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

