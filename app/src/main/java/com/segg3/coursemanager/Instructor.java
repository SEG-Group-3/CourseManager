package com.segg3.coursemanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class Instructor extends User{

    public Instructor(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    //Attribute names for database
    public static final String COURSENAME_KEY = "name";
    public static final String COURSECODE_KEY = "code";

    public static final String CAPACITY_KEY = "capacity";
    public static final String COURSEHOURS_KEY = "courseHours";
    public static final String DECS_KEY = "description";
    public static final String INSTRUCTOR_KEY = "instructor";

    private static final CollectionReference courseDB = FirebaseFirestore.getInstance().collection("Courses");

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

    public void setInstructor(Course course) {
        setInstructor(course.getId(), course.instructor);
    }

    @Override
    public String getType() {
        return "Instructor";
    }
}