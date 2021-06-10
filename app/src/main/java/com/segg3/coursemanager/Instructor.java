package com.segg3.coursemanager;

class Instructor extends User{

    public Instructor(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    @Override
    public String getType() {
        return "Instructor";
    }
}