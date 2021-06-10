package com.segg3.coursemanager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

class Admin extends User{


    public Admin(String userID, String name, String email, String username, String loginToken) {
        super(userID, name, email, username, loginToken);
    }

    public static final String COURSENAME_KEY = "name";
    public static final String COURSECODE_KEY = "Id";


    private static final CollectionReference courseDB = FirebaseFirestore.getInstance().collection("Courses");
    private static final CollectionReference userDB = FirebaseFirestore.getInstance().collection("Users");

    // for adding course
    HashMap<String, Object> courseDatabase = new HashMap<>();

    @Override
    public String getType()
    {
        return "Admin";
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

    public void deleteCourse(Course course) {

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