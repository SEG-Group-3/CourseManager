package com.segg3.coursemanager.shared.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.User;

public class AuthViewModel extends ViewModel {
    private MutableLiveData<User> user;


    public void logout() {
        user.setValue(null);
    }


    public LiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public boolean login(String name, String password) {
        logout();

        User u = UsersDao.getInstance().getUser(name);
        if (u == null)
            return false;
        boolean loggedIn = u.password.equals(password);
        if (loggedIn)
            user.setValue(u);
        return loggedIn;

    }
}
