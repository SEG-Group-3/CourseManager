package com.segg3.coursemanager.administrator.users.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.segg3.coursemanager.Admin;
import com.segg3.coursemanager.Instructor;
import com.segg3.coursemanager.R;
import com.segg3.coursemanager.Student;
import com.segg3.coursemanager.User;
import com.segg3.coursemanager.shared.UIUtils;
import com.segg3.coursemanager.shared.users.UserListAdapter;

import java.util.ArrayList;
import java.util.List;

public class UsersViewFragment extends Fragment {
    UserListAdapter listAdapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_admin_list_view, container, false);
        recyclerView= v.findViewById(R.id.course_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        List<User> users= new ArrayList<>();
        users.add(new Student("123", "Jeff", "jeff@school.com", "names_jeff", null));
        users.add(new Admin("321", "Hackerman", "dude@wtf.com", "superuser", null));
        users.add(new Student("132", "John Bro", "john@bro.com", "bro_bro", null));
        users.add(new Instructor("231", "Alice", "example@mail.com", "alice_name", null));
        users.add(new Student("312", "Felix", "fe@ad.ca", "super_soldier", null));

        listAdapter=new UserListAdapter(users,this::onCourseClicked);
        recyclerView.setAdapter(listAdapter);
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.users));
        return v;
    }
    public void onCourseClicked(View v){
        UIUtils.swipeFragmentRight(getParentFragmentManager(), new EditUserFragment());
    }
}