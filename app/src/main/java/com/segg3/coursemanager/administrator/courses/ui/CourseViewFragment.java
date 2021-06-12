package com.segg3.coursemanager.administrator.courses.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.Course;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.courses.CourseListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CourseViewFragment extends Fragment {
    CourseListAdapter listAdapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_admin_list_view, container, false);
        recyclerView= v.findViewById(R.id.course_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        List<Course> courses= new ArrayList<>();
        Date now= Calendar.getInstance().getTime();
        courses.add(new Course("Math","MAT1222", now,"ABkCD"));
        courses.add(new Course("Chemistry","CHM1222", now,"AlBCD"));
        courses.add(new Course("Physics","PHY1222", now,"ABpCD"));
        courses.add(new Course("History","HIS1234", now,"ff"));
        courses.add(new Course("English","END1256", now,"asd"));
        listAdapter=new CourseListAdapter(courses,this::onCourseClicked);
        recyclerView.setAdapter(listAdapter);
        UIUtils.setToolbarTitle(getActivity(),  getString(R.string.courses));
        return v;
    }
    public void onCourseClicked(View v){
        UIUtils.swipeFragmentRight(getParentFragmentManager(), new EditCourseFragment());
    }
}
