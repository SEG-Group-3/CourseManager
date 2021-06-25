package com.segg3.coursemanager.administrator.users.ui;

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

import com.segg3.coursemanager.R;
import com.segg3.coursemanager.shared.utils.UIUtils;
import com.segg3.coursemanager.shared.fragments.HomeFragment;
import com.segg3.coursemanager.shared.adapters.UserListAdapter;
import com.segg3.coursemanager.shared.viewmodels.UsersViewModel;

public class UsersViewFragment extends Fragment {
    UserListAdapter listAdapter;
    RecyclerView recyclerView;

    UserListAdapter userListAdapter;
    UsersViewModel usersViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_view, container, false);
        recyclerView = v.findViewById(R.id.course_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        UIUtils.swipeFragmentLeft(getParentFragmentManager(), new HomeFragment());
                    }
                });

        usersViewModel = new ViewModelProvider(requireActivity()).get(UsersViewModel.class);

        // Set Initial State
        userListAdapter = new UserListAdapter(usersViewModel.getUsers().getValue(), this::onUserClicked);
        recyclerView.setAdapter(userListAdapter);
        v.findViewById(R.id.floatingActionButton).setOnClickListener(this::onAddClicked);
        // Update UI on change
        usersViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            userListAdapter = new UserListAdapter(users, this::onUserClicked);
            recyclerView.setAdapter(userListAdapter);
        });

        UIUtils.setToolbarTitle(getActivity(), getString(R.string.users));

        return v;
    }

    public void onAddClicked(View v) {
        Fragment edit_user_frag = new EditUserFragment();
        Bundle args = new Bundle();
        edit_user_frag.setArguments(args);
        UIUtils.swipeFragmentRight(getParentFragmentManager(), edit_user_frag);
    }

    public void onUserClicked(View v) {
        // Finds the selected item
        int position = recyclerView.getChildLayoutPosition(v);
        // Setup Fragment arguments
        Fragment edit_user_frag = new EditUserFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        edit_user_frag.setArguments(args);
        UIUtils.swipeFragmentRight(getParentFragmentManager(), edit_user_frag);
    }
}