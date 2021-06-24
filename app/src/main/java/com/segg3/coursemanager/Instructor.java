package com.segg3.coursemanager;

public class Instructor extends User{


    //Attribute names for database
    public static final String COURSENAME_KEY = "name";
    public static final String COURSECODE_KEY = "code";

    public static final String CAPACITY_KEY = "capacity";
    public static final String COURSEHOURS_KEY = "courseHours";
    public static final String DECS_KEY = "description";
    public static final String INSTRUCTOR_KEY = "instructor";

    public Instructor(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }


    /**
     * Get an array courses based on a filter
     * @param name String that is used to filter names
     * @param feild firebase document feild that is used for filter
     * @return
     */
    public Course[] searchCourses(String name, String feild)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Set capacity for a course
     * @param course course that will be edited
     * @param capacity new CourseHour to update date
     */
    public void setCapacity(Course course, int capacity)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Set Course hours of a course
     * @param course course that will edited
     * @param date new CourseHour to update date
     */
    public void setCourseHours (Course course,  CourseHours date)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Set description of a course
     * @param course course that will edited
     * @param desc String that will replace old desc
     */
    public void setDesc(Course course, String desc)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType() {
        return "Instructor";
    }
}