package com.segg3.coursemanager.instructor.courses.ui;

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

import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.administrator.courses.ui.EditCourseFragment;
import com.segg3.coursemanager.databinding.FragmentListViewBinding;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.adapters.CourseListAdapter;
import com.segg3.coursemanager.shared.fragments.HomeFragment;
import com.segg3.coursemanager.shared.viewmodels.AuthViewModel;
import com.segg3.coursemanager.shared.viewmodels.CoursesViewModel;

import java.util.ArrayList;
import java.util.List;

public class CourseViewFragment extends Fragment {
    CourseListAdapter courseListAdapter;
    RecyclerView recyclerView;
    private AuthViewModel auth;
    FragmentListViewBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentListViewBinding.inflate(inflater);
        View v=binding.getRoot();
        recyclerView=binding.courseRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        auth = new ViewModelProvider(MainActivity.instance).get(AuthViewModel.class);
        binding.floatingActionButton.setVisibility(View.GONE);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                UIUtils.swipeFragmentLeft(getParentFragmentManager(), new HomeFragment());
            }
        });


        // Set Initial State
        List<Course> courses=CoursesDao.getInstance().searchCourse("");
        courseListAdapter = new CourseListAdapter(courses, this::onCourseClicked);
        recyclerView.setAdapter(courseListAdapter);
        CourseViewFragment fragment=this;
        // Update UI on change
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Course> courses=CoursesDao.getInstance().searchCourse(newText);
                courseListAdapter = new CourseListAdapter(courses, fragment::onCourseClicked);
                recyclerView.setAdapter(courseListAdapter);

                return false;
            }
        });

        UIUtils.setToolbarTitle(getActivity(),  getString(R.string.courses));
        return v;
    }

    public void onAddClicked(View v) {

    }

    public void onCourseClicked(View v) {
        // Finds the selected item
//        int position = recyclerView.getChildLayoutPosition(v);
//        Course clicked = coursesViewModel.getCourses().getValue().get(position);
//
//
//        if(clicked.instructor.equals("")){
//            UIUtils.createYesNoMenu("Enter course", "Do you want to assign yourself to this course?",
//                    getContext(),
//                    (dialog, which) -> {
//                         if (CoursesDao.getInstance().assignInstructor(auth.getUser().getValue().userName, clicked.code)){
//                             UIUtils.createToast(getContext(), "You have been assigned to this course");
//                         } else{
//                             UIUtils.createToast(getContext(), "An error has occurred");
//                         }
//                    });
//        } else{
//            UIUtils.createToast(getContext(), "This course is already taken!");
//        }
//
//        // Setup Fragment arguments
////        Fragment edit_course_frag = new EditCourseFragment();
////        Bundle args = new Bundle();
////        args.putInt("position", position);
////        edit_course_frag.setArguments(args);
////        UIUtils.swipeFragmentRight(getParentFragmentManager(), edit_course_frag);

    }
}
