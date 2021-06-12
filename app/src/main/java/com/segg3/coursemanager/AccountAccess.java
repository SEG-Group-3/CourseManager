package com.segg3.coursemanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AccountAccess extends DataBaseManager{

    private static CollectionReference db = FirebaseFirestore.getInstance().collection("Users");

    private final String PASSWORD_KEY = "password";
    private final String USERNAME_KEY = "userName";
    private final String NAME_KEY = "name";
    private final String EMAIL_KEY = "email";
    private final String TYPE_KEY = "type";

    private User user;


    public static DataBaseManager getInstance()
    {
        if (instance == null){
            instance = new AccountAccess();
        }
        return instance;
    }

    private AccountAccess()
    {
        //super();
    }

    public void readDataBase()
    {
        throw new UnsupportedOperationException();
    }

    public User loginUser(String password, String userName)
    {
        //todo update active users
        db.whereEqualTo(PASSWORD_KEY, password).whereEqualTo(USERNAME_KEY, userName).get()
        .addOnCompleteListener
        (
            new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        Iterator<QueryDocumentSnapshot> i = task.getResult().iterator();

                        if(i.hasNext())
                        {
                            QueryDocumentSnapshot document = i.next();
                            if(document.contains(TYPE_KEY))
                            {
                                switch (document.get(TYPE_KEY, String.class))
                                {
                                    case "admin":
                                        user = new Admin
                                            (
                                                document.getId(),
                                                get(document, NAME_KEY),
                                                get(document, EMAIL_KEY),
                                                get(document, USERNAME_KEY),
                                                null
                                            );
                                        break;
                                    case "student":
                                        user = new Student
                                            (
                                                document.getId(),
                                                get(document, NAME_KEY),
                                                get(document, EMAIL_KEY),
                                                get(document, USERNAME_KEY),
                                                null
                                            );
                                        break;
                                    case "instructor":
                                        user = new Instructor
                                            (
                                                document.getId(),
                                                get(document, NAME_KEY),
                                                get(document, EMAIL_KEY),
                                                get(document, USERNAME_KEY),
                                                null
                                            );
                                        break;
                                }
                            }

                        }
                    } else {
                        Log.d("FIRE", "Error getting documents: ", task.getException());
                    }
                }
            }
        );
        return null;
    }

    public boolean logOutUser()
    {
        //todo check if user is active
        user = null;
    }

    public User getUser()
    {
        return user;
    }

    public static boolean createAccount()
    {
        throw new UnsupportedOperationException();
    }
}
