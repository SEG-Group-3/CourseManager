package com.segg3.coursemanager;

public class Admin extends User {

    public Admin(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    @Override
    public String getType() {
        return "Admin";
    }
}