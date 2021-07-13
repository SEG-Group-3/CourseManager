package com.segg3.coursemanager.administrator.users.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.adapters.UserListAdapter;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.fragments.ListFragmentTemplate;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AdminUsersViewFragment extends ListFragmentTemplate<User, UserListAdapter> {
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.users));

        List<User> usersL = new ArrayList<>(UsersDao.getInstance().getUsers().getValue().values());
        setItems(new UserListAdapter(usersL));


    }

    @Override
    public void onResume() {
        super.onResume();
        UIUtils.setToolbarTitle(getActivity(), getString(R.string.users));
        List<User> usersL = new ArrayList<>(UsersDao.getInstance().getUsers().getValue().values());
        setItems(new UserListAdapter(usersL));
        UsersDao.getInstance().getUsers().observe(getViewLifecycleOwner(), users -> {
            List<User> usersList = new ArrayList<>(users.values());
            setItems(new UserListAdapter(usersList));
        });
    }

    @NonNull
    @Override
    public UserListAdapter filterQuery(@NonNull String query, @NonNull List<User> items) {
        List<User> filtered = new ArrayList<>();
        for (User u : items)
            if (u.userName.toLowerCase().contains(query.toLowerCase()) || u.type.toLowerCase().contains(query.toLowerCase()))
                filtered.add(u);

        return new UserListAdapter(filtered);
    }

    @Override
    public boolean onItemSwiped(@NonNull User item) {
        UsersDao.getInstance().deleteUser(item.userName);
        return false;
    }

    @Override
    public void onAddClicked(View v) {
        Fragment edit_user_frag = new AdminEditUserFragment();
        Bundle args = new Bundle();
        edit_user_frag.setArguments(args);
        UIUtils.swipeFragmentRight(getParentFragmentManager(), edit_user_frag);
    }

    @Override
    public void onItemClicked(@NonNull User item) {
        // Finds the selected item
        // Setup Fragment arguments
        Fragment edit_user_frag = new AdminEditUserFragment();
        Bundle args = new Bundle();
        args.putString("userName", item.userName);
        edit_user_frag.setArguments(args);
        UIUtils.swipeFragmentRight(getParentFragmentManager(), edit_user_frag);
    }
}