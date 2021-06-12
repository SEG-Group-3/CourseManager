package com.segg3.coursemanager.administrator.users.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.Student;
import com.segg3.coursemanager.User;
import com.segg3.coursemanager.shared.users.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

public class UsersViewFragment extends Fragment {
    UserListAdapter listAdapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_admin_course_view, container, false);
        recyclerView= v.findViewById(R.id.course_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        List<User> users= new ArrayList<>();
        users.add(new Student("1","Jeff", "jeff_01@uni.com","just_jeff", ""));
        users.add(new Student("2","Bob", "bob_busta@uni.com","bob_guy_05", ""));
        users.add(new Student("3","Alice", "al1c3@uni.com","wonderland_girl_05", ""));
        users.add(new Student("4","John", "john_f@uni.com","joe_mama_35", ""));
        listAdapter=new UserListAdapter(users,this::onCourseClicked);
        recyclerView.setAdapter(listAdapter);
        return v;
    }
    public void onCourseClicked(View v){

    }
}