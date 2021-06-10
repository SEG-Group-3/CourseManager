package com.segg3.coursemanager;

import java.util.Date;
import java.util.Map;

class Admin extends User{


    public Admin(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    @Override
    public String getType() {
        return "Admin";
    }
    public void createCourse(String courseCode, String courseName) {

    }

    public void deleteCourse(Course course) {

    }

    public void editCourse(Course course) {

    }

    public void deleteUser(User user) {

    }


}