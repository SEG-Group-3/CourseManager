package com.segg3.coursemanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import android.nfc.Tag;
import android.util.DebugUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

public class Admin extends User{

    public Admin(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    //Attribute names for database
    public static final String COURSENAME_KEY = "name";
    public static final String COURSECODE_KEY = "code";

    // For reading database
    private Consumer<List<Course>> listener = null;
    private List<Course> listOfCourses = new ArrayList<>();

    //Database references for adding/deleting/editing courses/users
    private static final CollectionReference courseDB = FirebaseFirestore.getInstance().collection("Courses");
    private static final CollectionReference userDB = FirebaseFirestore.getInstance().collection("Users");

    // for adding course
    HashMap<String, Object> courseDatabase = new HashMap<>();

    @Override
    public String getType()
    {
        return "Admin";
    }


    public void setListener(Consumer<List<Course>> listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public void createCourse(Course course) {
        courseDatabase.put(COURSENAME_KEY, course.name);
        courseDatabase.put(COURSECODE_KEY, course.code);
        courseDB.add(courseDatabase).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Message:", "Course was created.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course could not be created.");
            }
        });
    }

    public void deleteCourse(String delCourseID) {
        courseDB.document(delCourseID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Course was deleted.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course could not be deleted.");
            }
        });
    }

    public void deleteCourse(Course course) {
        deleteCourse(course.getId());
    }

    /*
    Parameters:
    course -> course to be edited.
    newName -> new course name.
    newCode -> new course code.
     */
    public void editCourse(Course course, String newName, String newCode) {
        DocumentReference doc = courseDB.document(course.code);

        doc.update(course.code, newCode, course.name, newName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Course edit successful.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course edit unsuccessful.");
            }
        });
    }


    //Edit course that draws from course ID rather than a course object itself.
    public void editCourse(String courseID, String newName, String newCode) {
        DocumentReference doc = courseDB.document(courseID);

        doc.update(COURSECODE_KEY, newCode, COURSENAME_KEY, newName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "Course edit successful.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "Course edit unsuccessful.");
            }
        });
    }

    // Edit course method that draws from the specific course that grabs actually ID from previous method
    public void editCourse(Course course) {
        editCourse(course.getId(), course.name, course.code);
    }

    private void readCourseDatabase() {
        courseDB.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Course courseTmp = new Course(document.get(COURSENAME_KEY, String.class), document.get(COURSECODE_KEY, String.class), null, document.getId());
                        listOfCourses.add(courseTmp);
                    }
                    listener.accept(listOfCourses);
                    Log.d("Message:", toString());
                } else {
                    Log.w("Message:", "Error getting courses", task.getException());
                }
            }
        });
    }

    public void deleteUser(String userId) {

        userDB.document(userId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "User successfully deleted.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "User could not be deleted.");
            }
        });
    }

    public void deleteUser(User delUser) {

        userDB.document(delUser.getUserID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Message:", "User successfully deleted.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.w("Message:", "User could not be deleted.");
            }
        });
    }

}