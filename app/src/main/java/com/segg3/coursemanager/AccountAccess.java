package com.segg3.coursemanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.segg3.coursemanager.shared.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
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

    public User loginUser(String password, String userName, DataBaseManagerListener callback)
    {
        //todo update active users
        db.whereEqualTo(PASSWORD_KEY, password).whereEqualTo(USERNAME_KEY, userName).get()
        .addOnCompleteListener
        (
                task -> {
                    if (task.isSuccessful()) {

                        Iterator<QueryDocumentSnapshot> i = task.getResult().iterator();

                        if(i.hasNext())
                        {
                            QueryDocumentSnapshot document = i.next();
                            user = new User();

                            // try
                            user.type = document.get(TYPE_KEY, String.class).toLowerCase();
                            user.userName = get(document, USERNAME_KEY);
                            user.password = get(document, "password");
                            // catch
                            // Log.w("FIRE", "Unknown user type found for document " + document.getId());
                            // callback.onFailure(new Exception("Failed Login due to server error"));

                            callback.onSuccess(user);
                        }
                        else
                        {
                            callback.onFailure(new Exception("Failed Login"));
                        }
                    } else {
                        callback.onFailure(new Exception("Failed Login"));
                        Log.d("FIRE", "Error getting documents: ", task.getException());
                    }
                }
        )
        .addOnFailureListener
        (
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    callback.onFailure(e);
                }
            }
        );
        return null;
    }

    public void logOutUser(DataBaseManagerListener callback)
    {
        //todo check if user is active
        user = null;
        callback.onSuccess(user);
    }

    public User getUser()
    {
        return user;
    }

    public void createAccount(String userName, String password, String type, DataBaseManagerListener callback)
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
                                        loginUser((String) userDataCache.get(PASSWORD_KEY), (String) userDataCache.get(USERNAME_KEY), callback);
                                        userDataCache.clear();
                                    }
                                }
                            );
                        }
                        else
                        {
                            callback.onFailure(new Exception("account must have unique user name"));
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
                    callback.onFailure(e);
                }
            }
        );
    }

    public void editAccount(String key, String newVal, DataBaseManagerListener callback)
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
            db.document(user.id).get()
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
                                    db.document(user.id).update((String) userDataCache.get("key"),(String) userDataCache.get(userDataCache.get("key")));
                                }
                                else
                                {
                                    userDataCache.remove("key");
                                    db.document(user.id).set(userDataCache.get(userDataCache.get("key")));
                                }
                                callback.onSuccess(user);
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
                        callback.onFailure(e);
                    }
                }
            );
        }
    }
}
