package com.segg3.coursemanager;

class Student extends User{


    public Student(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    @Override
    public String getType() {
        return "Student";
    }
}