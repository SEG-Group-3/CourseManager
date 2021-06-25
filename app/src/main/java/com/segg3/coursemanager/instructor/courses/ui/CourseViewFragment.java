package com.segg3.coursemanager.instructor.courses.ui;

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
import com.segg3.coursemanager.administrator.courses.ui.EditCourseFragment;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.fragments.HomeFragment;
import com.segg3.coursemanager.shared.viewmodels.AuthViewModel;
import com.segg3.coursemanager.shared.viewmodels.CoursesViewModel;

public class CourseViewFragment extends Fragment {
    CourseListAdapter courseListAdapter;
    CoursesViewModel coursesViewModel;
    RecyclerView recyclerView;
    private AuthViewModel auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list_view, container, false);
        recyclerView= v.findViewById(R.id.course_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        auth = new ViewModelProvider(MainActivity.instance).get(AuthViewModel.class);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new HomeFragment());
            }
        });

        coursesViewModel = new ViewModelProvider(requireActivity()).get(CoursesViewModel.class);

        // Set Initial State
        courseListAdapter = new CourseListAdapter(coursesViewModel.getCourses().getValue(), this::onCourseClicked);
        recyclerView.setAdapter(courseListAdapter);
        v.findViewById(R.id.floatingActionButton).setOnClickListener(this::onAddClicked);
        // Update UI on change
        coursesViewModel.getCourses().observe(getViewLifecycleOwner(), courses -> {
            courseListAdapter = new CourseListAdapter(courses, this::onCourseClicked);
            recyclerView.setAdapter(courseListAdapter);
        });

        UIUtils.setToolbarTitle(getActivity(),  getString(R.string.courses));
        return v;
    }

    public void onAddClicked(View v) {

    }

    public void onCourseClicked(View v){
        // Finds the selected item
        int position = recyclerView.getChildLayoutPosition(v);
        Course clicked = coursesViewModel.getCourses().getValue().get(position);


        if(clicked.instructor.equals("")){
            UIUtils.createYesNoMenu("Enter course", "Do you want to assign yourself to this course?",
                    getContext(),
                    (dialog, which) -> {
                         if (CoursesDao.getInstance().assignInstructor(auth.getUser().getValue().userName, clicked.code)){
                             UIUtils.createToast(getContext(), "You have been assigned to this course");
                         } else{
                             UIUtils.createToast(getContext(), "An error has occurred");
                         }
                    });
        } else{
            UIUtils.createToast(getContext(), "This course is already taken!");
        }

        // Setup Fragment arguments
//        Fragment edit_course_frag = new EditCourseFragment();
//        Bundle args = new Bundle();
//        args.putInt("position", position);
//        edit_course_frag.setArguments(args);
//        UIUtils.swipeFragmentRight(getParentFragmentManager(), edit_course_frag);
    }
}
