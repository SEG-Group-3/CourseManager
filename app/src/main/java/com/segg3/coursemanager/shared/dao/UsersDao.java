package com.segg3.coursemanager.shared.dao;

import androidx.lifecycle.LiveData;

import com.segg3.coursemanager.shared.filter.CodeFilter;
import com.segg3.coursemanager.shared.filter.NameFilter;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.TaskCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class UsersDao extends DataAccessObject<User> {
    private static UsersDao instance;

    private UsersDao() {
        super("Users", User.class);
    }

    public static UsersDao getInstance() {
        if (instance == null)
            instance = new UsersDao();
        return instance;
    }


    public List<User> searchUsers(String query) {
        List<User> filtered = new ArrayList<>();
        query = query.toLowerCase();
        for (User u : cache.getValue().values())
            if (u.userName.toLowerCase().contains(query) || u.type.toLowerCase().contains(query) || u.id.toLowerCase().contains(query))
                filtered.add(u);

        return filtered;
    }

    public LiveData<HashMap<String, User>> getUsers() {
        return this.cache;
    }

    public User getUser(String userName) {
        return get(userName);
    }

    public TaskCallback<Boolean> deleteUser(String userName) {
        return this.delete(userName);
    }

    public TaskCallback<User> addUser(User user) {
        return this.add(user);
    }

    public TaskCallback<User> editUser(String oldUserName, User user) {
        TaskCallback<User> callback = new TaskCallback<>();

        // If old userName is invalid just add a new one
        if (this.get(oldUserName) == null) {
            return addUser(user);
        }

        // If the userName (the key) is the same just update it
        if (oldUserName.equals(user.userName) || user.userName == null)
            return put(user); // Overrides last user only with non-null fields

        // We probably want to change our name and fields so we check if we can change userNames
        if (this.get(user.userName) != null) {
            callback.lazyError(new Exception("Username is already taken!"));
            return callback;
        }

        // Merge the old values with the new one, delete old entry, and add a new one
        User merged = merge(get(oldUserName), user);
        delete(oldUserName);

        return put(merged);
    }

}
