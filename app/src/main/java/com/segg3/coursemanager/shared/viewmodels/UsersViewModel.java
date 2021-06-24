package com.segg3.coursemanager.shared.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.segg3.coursemanager.User;
import com.segg3.coursemanager.shared.dao.UsersDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersViewModel extends ViewModel {
    private LiveData<List<User>> users;
    private final UsersDao dao = UsersDao.getInstance();

    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = Transformations.switchMap(dao.getUsers(), this::loadUsers);
        }
        return users;
    }

    private LiveData<List<User>> loadUsers(HashMap<String, User> usersMap) {
        MutableLiveData liveData = new MutableLiveData();
        liveData.setValue(new ArrayList<>(usersMap.values()));
        return liveData;
    }
}