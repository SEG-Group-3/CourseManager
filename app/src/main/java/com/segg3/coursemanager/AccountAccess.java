package com.segg3.coursemanager;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AccountAccess extends DataBaseManager{

    private static CollectionReference db = FirebaseFirestore.getInstance().collection("Users");

    private final String PASSWORD_KEY = "password";
    private final String USERNAME_KEY = "userName";
    private final String NAME_KEY = "name";
    private final String EMAIL_KEY = "email";
    private final String TYPE_KEY = "type";

    private User user;

    private Map<String, Object> userDataCache = new HashMap<String, Object>();

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
                                onSuccessListener.onSuccess(user);

                                return;
                            }
                        }
                        else
                        {
                            onFailureListener.onFailure(new Exception("Failed Login"));
                        }
                    } else {
                        onFailureListener.onFailure(new Exception("Failed Login"));
                        Log.d("FIRE", "Error getting documents: ", task.getException());
                    }
                }
            }
        )
        .addOnFailureListener
        (
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    onFailureListener.onFailure(e);
                }
            }
        );
        return null;
    }

    public void logOutUser()
    {
        //todo check if user is active
        user = null;
        onSuccessListener.onSuccess(user);
    }

    public User getUser()
    {
        return user;
    }

    public void createAccount(String userName, String password, String type)
    {
        switch (type)
        {
            case "admin":
            case "student":
            case "instructor":
                userDataCache.put(TYPE_KEY, type);
                break;
            default:
                throw new IllegalArgumentException("invalid account type");
        }

        userDataCache.put(USERNAME_KEY, userName);
        userDataCache.put(PASSWORD_KEY, password);

        db.whereEqualTo(USERNAME_KEY, userName).whereEqualTo(PASSWORD_KEY, password).get()
        .addOnCompleteListener
        (
            new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        if (task.getResult().size() == 0)
                        {
                            db.add(userDataCache)
                            .addOnSuccessListener
                            (
                                new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        loginUser((String) userDataCache.get(PASSWORD_KEY), (String) userDataCache.get(USERNAME_KEY));
                                        userDataCache.clear();
                                    }
                                }
                            );
                        }
                    }
                }
            }
        )
        .addOnFailureListener
        (
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    onFailureListener.onFailure(e);
                }
            }
        );
    }

    public void editAccount(String key, String newVal)
    {
        switch (key)
        {
            case USERNAME_KEY:
            case PASSWORD_KEY:
            case TYPE_KEY:
            case NAME_KEY:
            case EMAIL_KEY:
                userDataCache.clear();
                userDataCache.put("key", key);
                userDataCache.put(key, newVal);
                break;
            default:
                throw new IllegalArgumentException("Invalid key");
        }

        if(user != null)
        {
            db.document(user.getUserID()).get()
            .addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists())
                            {
                                if(document.contains((String) userDataCache.get("key")))
                                {
                                    db.document(user.getUserID()).update((String) userDataCache.get("key"),(String) userDataCache.get(userDataCache.get("key")));
                                }
                                else
                                {
                                    userDataCache.remove("key");
                                    db.document(user.getUserID()).set(userDataCache.get(userDataCache.get("key")));
                                }
                                onSuccessListener.onSuccess(user);
                            }
                        }
                    }
                }
            )
            .addOnFailureListener
            (
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        onFailureListener.onFailure(e);
                    }
                }
            );
        }
    }
}
