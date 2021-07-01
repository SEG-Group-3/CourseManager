package com.segg3.coursemanager.shared.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Instructor extends User{


    //Attribute names for database
    public static final String COURSENAME_KEY = "name";
    public static final String COURSECODE_KEY = "code";

    public static final String CAPACITY_KEY = "capacity";
    public static final String COURSEHOURS_KEY = "courseHours";
    public static final String DECS_KEY = "description";
    public static final String INSTRUCTOR_KEY = "instructor";

    private static final CollectionReference courseDB = FirebaseFirestore.getInstance().collection("Courses");

    private HashMap<String, Course> courses = new HashMap<String, Course>();

    /**
     * Get an array courses based on a filter
     * @param filter String that is used to filter names
     * @param field firebase document field that is used for filter
     * @return course
     */
    public Course[] searchCourses(String filter, String field)
    {
        switch (field)
        {
            case "name":
                return searchByName(filter);
            case "code":
                return searchByCode(filter);
            default:
                throw new IllegalArgumentException("Illegal field");
        }
    }

    private Course[] searchByName(String name)
    {
        List<Course> tmpCourses = new ArrayList<Course>();

        for(Course c : courses.values())
        {
            if(c.name.toLowerCase().contains(name.toLowerCase()))
            {
                tmpCourses.add(c);
            }
        }

        return (Course[]) tmpCourses.toArray();
    }

    private Course[] searchByCode(String code)
    {
        List<Course> tmpCourses = new ArrayList<Course>();

        for(Course c : courses.values())
        {
            if(c.code.toLowerCase().contains(code.toLowerCase()))
            {
                tmpCourses.add(c);
            }
        }

        return (Course[]) tmpCourses.toArray();
    }

    /**
     * Set capacity for a course
     * @param course course that will be edited
     * @param capacity new CourseHour to update date
     */
    public void setCapacity(Course course, int capacity)
    {
        DocumentReference doc = courseDB.document(course.code);

        doc.update(String.valueOf(course.capacity), capacity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Course capacity was successfully updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course capacity was not updated.");
            }
        });
    }

    public void setCapacity(String courseID, int capacity)
    {
        DocumentReference doc = courseDB.document(courseID);

        doc.update(CAPACITY_KEY, capacity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Course capacity was successfully updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course capacity was not updated.");
            }
        });
    }

    public void setCapacity(Course course) {
        setCapacity(course.getId(), course.capacity);
    }

    /**
     * Set Course hours of a course
     * @param course course that will edited
     * @param date new CourseHour to update date
     */
    public void setCourseHours (Course course,  CourseHours date)
    {
        DocumentReference doc = courseDB.document(course.code);

        doc.update(String.valueOf(course.courseHours), date).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Course hours have been updated.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course hours could not be updated.");
            }
        });

    }

    public void setCourseHours(String courseID, CourseHours date) {
        DocumentReference doc = courseDB.document(courseID);

        doc.update(COURSEHOURS_KEY, date).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Course hours have been updated.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course hours could not be updated.");
            }
        });
    }

    public void setCourseHours(Course course) {
        //setCourseHours(course.getId(), course.courseHours);
    }

    /**
     * Set description of a course
     * @param course course that will edited
     * @param desc String that will replace old desc
     */
    public void setDesc(Course course, String desc)
    {
        DocumentReference doc = courseDB.document(course.code);

        doc.update(course.description, desc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message", "Description successfully added to course.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Description was not added to course.");
            }
        });
    }

    public void setDesc(String courseID, String desc)
    {
        DocumentReference doc = courseDB.document(courseID);

        doc.update(DECS_KEY, desc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message", "Description successfully added to course.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Description was not added to course.");
            }
        });
    }

    public void setDesc(Course course) {
        setDesc(course.getId(), course.description);
    }

    public void setInstructor(Course course, Instructor newInstructor) {
        DocumentReference doc = courseDB.document(course.code);

        doc.update(String.valueOf(course.instructor), newInstructor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Instructor successfully assigned to course.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Instructor was not assigned to course.");
            }
        });
    }

    public void setInstructor(String courseID, Instructor newInstructor) {
        DocumentReference doc = courseDB.document(courseID);

        doc.update(INSTRUCTOR_KEY, newInstructor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Instructor successfully assigned to course.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Instructor was not assigned to course.");
            }
        });
    }

//    public void setInstructor(Course course) {
//        setInstructor(course.getId(), course.instructor);
//    }

}