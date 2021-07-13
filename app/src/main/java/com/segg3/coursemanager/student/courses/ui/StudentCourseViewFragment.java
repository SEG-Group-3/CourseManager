package com.segg3.coursemanager.student.courses.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.FragmentListViewBinding;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.fragments.HomeFragment;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.viewmodels.CoursesViewModel;

import java.util.List;

public class StudentCourseViewFragment extends Fragment {
    CourseListAdapter courseListAdapter;
    RecyclerView recyclerView;
    FragmentListViewBinding binding;
    CoursesViewModel coursesViewModel;
    List<Course> allCourses;

    String query = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater);
        View v = binding.getRoot();
        recyclerView = binding.courseRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        coursesViewModel = new ViewModelProvider(requireActivity()).get(CoursesViewModel.class);


        binding.floatingActionButton.setVisibility(View.GONE);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new HomeFragment());
            }
        });


        // Set Initial State
        updateCourses();
        // Update UI on change
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                updateCourses();
                return false;
            }
        });
        coursesViewModel.getCourses().observe(getViewLifecycleOwner(), courses -> updateCourses());
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.courses));
        return v;
    }

    public void updateCourses() {
        allCourses = CoursesDao.getInstance().searchCourse(query);
        courseListAdapter = new CourseListAdapter(allCourses, this::onCourseClicked);
        recyclerView.setAdapter(courseListAdapter);
    }

    public void onCourseClicked(View v) {
        //Finds the selected item
        int position = recyclerView.getChildLayoutPosition(v);
        Course clicked = allCourses.get(position);
        Fragment frag = new StudentInspectCourseViewFragment();

        Bundle args = new Bundle();
        args.putString("code", clicked.code);
        args.putBoolean("viewingAll", true);
        frag.setArguments(args);

        UIUtils.swipeFragmentRight(getParentFragmentManager(), frag);
    }
}

