package com.segg3.coursemanager.student.courses.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentListViewBinding;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.fragments.HomeFragment;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.viewmodels.AuthViewModel;
import com.segg3.coursemanager.shared.viewmodels.CoursesViewModel;

import java.util.List;

public class StudentMyCourseViewFragment extends Fragment {
    CourseListAdapter courseListAdapter;
    CoursesViewModel coursesViewModel;
    RecyclerView recyclerView;
    FragmentListViewBinding binding;
    List<Course> courses;
    private AuthViewModel auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater);
        View v = binding.getRoot();
        recyclerView = v.findViewById(R.id.course_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        auth = new ViewModelProvider(MainActivity.instance).get(AuthViewModel.class);
        binding.floatingActionButton.setVisibility(View.GONE);
        binding.searchBar.setVisibility(View.GONE);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new HomeFragment());
            }
        });

        coursesViewModel = new ViewModelProvider(requireActivity()).get(CoursesViewModel.class);

        // Set Initial State
        courses = CoursesDao.getInstance().getStudentCourses(auth.getUser().getValue().userName);
        courseListAdapter = new CourseListAdapter(courses, this::onCourseClicked);
        recyclerView.setAdapter(courseListAdapter);
        // Update UI on change

        coursesViewModel.getCourses().observe(getViewLifecycleOwner(), courseList -> updateCourses());
        updateCourses();
        UIUtils.setToolbarTitle(getActivity(), "My courses");
        return v;
    }

    public void updateCourses() {
        courses = CoursesDao.getInstance().getStudentCourses(auth.getUser().getValue().userName);
        courseListAdapter = new CourseListAdapter(courses, this::onCourseClicked);
        recyclerView.setAdapter(courseListAdapter);
    }

    public void onCourseClicked(View v) {
        // Finds the selected item
        int position = recyclerView.getChildLayoutPosition(v);
        Course clicked = courses.get(position);
        Fragment frag = new StudentInspectCourseViewFragment();

        Bundle args = new Bundle();
        args.putString("code", clicked.code);
        frag.setArguments(args);

        UIUtils.swipeFragmentRight(getParentFragmentManager(), frag);
    }
}


