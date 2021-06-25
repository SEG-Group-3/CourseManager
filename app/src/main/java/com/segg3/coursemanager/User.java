package com.segg3.coursemanager;

import com.segg3.coursemanager.shared.dao.DataObject;

import org.jetbrains.annotations.NotNull;

public abstract class User implements DataObject {


    @Override
    public String getPrimaryKey() {
        return username;
    }

    public String userID;
    public String name;
    public String email;
    public String username;
    public String loginToken;

    public User(){

    }

    public User(String userID, String name, String email, String username, String loginToken) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.username = username;
        this.loginToken = loginToken;
    }

    public abstract String getType();


    //Getters:

    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getLoginToken() {
        return loginToken;
    }


    @NotNull
    public String toString()
    {
        return "userID:" + userID + "\tname:" + name + "\temail:" + email + "\tusername:";
    }
}