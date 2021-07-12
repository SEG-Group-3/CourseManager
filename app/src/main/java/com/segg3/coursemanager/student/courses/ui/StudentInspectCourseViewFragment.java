package com.segg3.coursemanager.student.courses.ui;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.MainActivity;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.databinding.DialogCourseHourBinding;
import com.segg3.coursemanager.databinding.FragmentStudentInspectCourseBinding;
import com.segg3.coursemanager.instructor.courses.ui.MyCourseViewFragment;
import com.segg3.coursemanager.shared.adapters.CourseHoursListAdapter;
import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.CourseHours;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.UIUtils;

import org.apache.tools.ant.Main;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StudentInspectCourseViewFragment extends Fragment {
    FragmentStudentInspectCourseBinding binding;
    Course beingEdited;

    CourseHoursListAdapter courseListAdapter;
    RecyclerView recyclerView;
    List<CourseHours> mutCourseHours;
    boolean enrolling = true;
    boolean viewingAll = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentInspectCourseBinding.inflate(inflater);
        View view = binding.getRoot();

        User currentUser = MainActivity.instance.auth.getUser().getValue();

        binding.cancelButton.setOnClickListener(this::onCancel);

        setHasOptionsMenu(true);
        UIUtils.setToolbarTitle(getActivity(), "Course");
        String courseCode = getArguments().getString("code");
        viewingAll = getArguments().getBoolean("viewingAll", false);
        beingEdited = CoursesDao.getInstance().getCourse(courseCode);
        List<Course> alreadyEnrolled = CoursesDao.getInstance().getStudentCourses(MainActivity.instance.auth.getUser().getValue().userName);
        enrolling = !alreadyEnrolled.stream().anyMatch(course -> course.code.equals(courseCode));



        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onCancel();
            }
        });

        // Make button behaviour weather we are enrolling or dropping a course
        if (enrolling){
            binding.applyButton.setText("Enroll");
            binding.applyButton.setOnClickListener(v -> {
                UIUtils.createYesNoMenu("Enrol course","Do you want to enrol "+ beingEdited.name,getContext(),(dialog, which) -> {
                    if(CoursesDao.getInstance().joinCourse(currentUser.userName, beingEdited.code)){
                        UIUtils.createToast(getContext(), "You have joined " + beingEdited.name);
                    }
                    else{
                        UIUtils.createToast(getContext(), "An error has occurred!");
                    }

                    UIUtils.swipeFragmentLeft(getParentFragmentManager(), new StudentCourseViewFragment());
                });
            });

        } else{
            binding.applyButton.setText("Drop");
            binding.applyButton.setOnClickListener(v -> {
                UIUtils.createYesNoMenu("Drop course","Do you want to drop " + beingEdited.name,getContext(),(dialog, which) -> {
                    if(CoursesDao.getInstance().leaveCourse(currentUser.userName, beingEdited.code)){
                        UIUtils.createToast(getContext(), "You have dropped " + beingEdited.name);
                    }
                    else{
                        UIUtils.createToast(getContext(), "An error has occurred!");
                    }
                    UIUtils.swipeFragmentLeft(getParentFragmentManager(), new StudentMyCourseViewFragment());
                });
            });

        }



        // Show course hours
        recyclerView = binding.courseHoursRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        mutCourseHours = new ArrayList<>();
        for (String c : beingEdited.courseHours) {
            mutCourseHours.add(new CourseHours(c));
        }

        courseListAdapter = new CourseHoursListAdapter(mutCourseHours, null);
        recyclerView.setAdapter(courseListAdapter);


        // Show course information

        binding.courseTitle.setText(beingEdited.name);
        binding.courseDescription.setText(beingEdited.description);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void onCancel(View v) {
        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new MyCourseViewFragment());
    }

    private void onCancel(){
        if(viewingAll)
            UIUtils.swipeFragmentLeft(getParentFragmentManager(), new StudentCourseViewFragment());
        else
            UIUtils.swipeFragmentLeft(getParentFragmentManager(), new StudentMyCourseViewFragment());
    }

}