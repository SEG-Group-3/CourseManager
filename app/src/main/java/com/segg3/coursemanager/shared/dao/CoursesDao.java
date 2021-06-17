package com.segg3.coursemanager.shared.dao;

import androidx.lifecycle.LiveData;

import com.segg3.coursemanager.shared.TaskCallback;
import com.segg3.coursemanager.shared.models.Course;

import java.util.HashMap;

public class CoursesDao extends DataAccessObject<Course> {
    private static CoursesDao instance;

    private CoursesDao() {
        super("Courses", Course.class);
    }

    public static CoursesDao getInstance() {
        if (instance == null)
            instance = new CoursesDao();
        return instance;
    }

    public LiveData<HashMap<String, Course>> getCourses() {
        return this.cache;
    }

    public Course getCourse(String courseCode) {
        return get(courseCode);
    }


    public TaskCallback<?> deleteCourse(String code) {
        return this.delete(code);
    }

    public TaskCallback<?> addCourse(Course course) {
        return this.add(course);
    }

    public TaskCallback<?> editCourse(String oldCode, Course course) {
        TaskCallback callback = new TaskCallback();
        //If old userName is invalid just add a new one
        if (this.get(oldCode) == null) {
            return addCourse(course);
        }

        // If the userName (the key) is the same just update it
        if (oldCode.equals(course.code) || course.code == null)
            return put(course); // Overrides last user only with non-null fields

        // We probably want to change our name and fields so we check if we can change userNames
        if (this.get(course.code) != null) {
            callback.lazyError(new Exception("Course code is already taken!"));
            return callback;
        }

        // Merge the old values with the new one, delete old entry, and add a new one
        Course merged = merge(get(oldCode), course);
        delete(oldCode);

        return put(merged);
    }

}
