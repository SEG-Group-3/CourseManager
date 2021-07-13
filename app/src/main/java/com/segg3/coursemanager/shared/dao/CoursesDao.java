package com.segg3.coursemanager.shared.dao;

import androidx.lifecycle.LiveData;

import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.CourseHours;
import com.segg3.coursemanager.shared.models.User;
import com.segg3.coursemanager.shared.utils.TaskCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    public void unAssignInstructor(String courseCode) {
        Course c = get(courseCode);
        c.instructor = "";
        c.capacity = -1;
        c.description = "";
        c.courseHours = new ArrayList<>();
        editCourse(c.code, c);
    }

    public List<Course> getInstructorCourses(String userName) {
        List<Course> filtered = new ArrayList<>();
        for (Course c : cache.getValue().values()) {
            if (c.instructor.equals(userName)) {
                filtered.add(c);
            }
        }
        return filtered;
    }


    public List<Course> searchCourse(String query) {
        List<Course> filtered = new ArrayList<>();
        query = query.toLowerCase();
        Collection<Course> unfiltered = cache.getValue().values();
        for (Course c : unfiltered)
            if (c.name.toLowerCase().contains(query) || c.code.toLowerCase().contains(query))
                filtered.add(c);

        return filtered;
    }

    public boolean assignInstructor(String userName, String code) {
        User u = UsersDao.getInstance().get(userName);
        Course c = get(code);
        if (u.type.toLowerCase().equals("instructor") && c.instructor.equals("")) {
            c.instructor = u.userName;
            c.courseHours = new ArrayList<>();
            c.capacity = -1;
            c.description = "";
            c.registeredStudents = 0;
            put(c);
            return true;
        } else {
            return false;
        }
    }

    public List<Course> getStudentCourses(String studentName) {
        List<Course> filtered = new ArrayList<>();
        for (Course c : Objects.requireNonNull(cache.getValue()).values()) {
            if (c.enrolled.contains(studentName)) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    public boolean joinCourse(String userName, String code) {
        User u = UsersDao.getInstance().get(userName);
        Course c = get(code);
        List<Course> enrolled = getStudentCourses(userName);
        if (u.type.toLowerCase().equals("student") && c.registeredStudents < c.capacity) {
            //checks if user is already enrolled
            for (Course course : enrolled) {
                if (course.code.equals(code)) {
                    return false;
                }
            }

            //checks for time conflict
            CourseHours courseHour;
            CourseHours enrolledCourseHour;
            for (String courseHourRaw : c.courseHours) {
                courseHour = new CourseHours(courseHourRaw);
                for (Course course : enrolled) {
                    for (String enrolledCourseHourRaw : course.courseHours) {
                        enrolledCourseHour = new CourseHours(enrolledCourseHourRaw);
                        if (courseHour.compareTo(enrolledCourseHour) == 0) {
                            return false;
                        }
                    }
                }
            }

            c.enrolled.add(u.userName);
            c.registeredStudents++;
            put(c);
            return true;
        } else {
            return false;
        }
    }

    public boolean leaveCourse(String userName, String code) {
        User u = UsersDao.getInstance().get(userName);
        Course c = get(code);

        if (c.enrolled.contains(u.userName)) {
            c.enrolled.remove(u.userName);
            c.registeredStudents--;
            put(c);
            return true;
        } else {
            return false;
        }
    }
}
