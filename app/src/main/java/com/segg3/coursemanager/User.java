package com.segg3.coursemanager;

public abstract class User {
    protected String userID;
    public String name;
    protected String email;
    protected String username;
    protected String loginToken;

    public User(String userID, String name, String email, String username, String loginToken) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.username = username;
        this.loginToken = loginToken;
    }

    public abstract String getType();


    //Setters:

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


    public String toString()
    {
        return "userID:" + userID + "\tname:" + name + "\temail:" + email + "\tusername:";
    }
}