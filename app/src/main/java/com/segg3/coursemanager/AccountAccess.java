package com.segg3.coursemanager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AccountAccess extends DataBaseManager{

    private static CollectionReference db = FirebaseFirestore.getInstance().collection("Users");

    private final String PASSWORD_KEY = "password";
    private final String USERNAME_KEY = "userName";
    private final String TYPE_KEY = "type";

    private List<User> users = new ArrayList<User>();


    public static DataBaseManager getInstance()
    {
        if (instance == null){
            instance = new AccountAccess();
        }
        return instance;
    }

    private AccountAccess()
    {
        super();
    }

    public void readDataBase()
    {

        throw new UnsupportedOperationException();
    }

    public User loginUser(String password, String userName)
    {
        throw new UnsupportedOperationException();
    }

    public boolean logOutUser()
    {
        throw new UnsupportedOperationException();
    }

    public User getUser()
    {
        throw new UnsupportedOperationException();
    }

    public static boolean createAccount()
    {
        throw new UnsupportedOperationException();
    }
}
