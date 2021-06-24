package com.segg3.coursemanager;

public class Instructor extends User{

    public Instructor(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    public Course[] searchCourses(String name, String feild)
    {
        throw new UnsupportedOperationException();
    }

    public void setCapacity(Course course, int capacity)
    {
        throw new UnsupportedOperationException();
    }

    public void setCourseHours (Course course,  CourseHours date)
    {
        throw new UnsupportedOperationException();
    }

    public void setDesc(Course course, String desc)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType() {
        return "Instructor";
    }
}