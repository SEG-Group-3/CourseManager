package com.segg3.coursemanager.shared.models;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.segg3.coursemanager.Admin;
import com.segg3.coursemanager.Instructor;
import com.segg3.coursemanager.Student;
import com.segg3.coursemanager.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsersViewModel extends ViewModel {
    private MutableLiveData<HashMap<String, User>> users_impl;
    private MutableLiveData<List<User>> users;
    private static final CollectionReference userDB = FirebaseFirestore.getInstance().collection("Users");

    private static final String PASSWORD_KEY = "password";
    private static final String TYPE_KEY = "type";
    private static final String USERNAME_KEY = "userName";

    public MutableLiveData<List<User>> getUsers(){
        if (users_impl == null){
            users_impl = new MutableLiveData<>();
            users_impl.setValue(new HashMap<>());
            users = new MutableLiveData<>();
            users.setValue(new ArrayList<>());
            loadUsers();
        }
        return users;
    }

    public void loadUsers(){
        userDB.addSnapshotListener((snapshots, e) -> {
            if (e != null)
                return;
            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                QueryDocumentSnapshot doc = dc.getDocument();
                User userTemp = deserializeUser(doc);
                if (userTemp == null){
                    Log.w("FIRE", "Object: " + doc.getId() + " doesn't match user signature");
                    continue;
                }
                switch (dc.getType()) {
                    case ADDED:
                    case MODIFIED:
                        users_impl.getValue().put(doc.getId(), userTemp);
                        break;
                    case REMOVED:
                        users_impl.getValue().remove(doc.getId());
                        break;
                    default:
                        break;
                }
            }
            users.setValue(new ArrayList<>(users_impl.getValue().values()));
        });
    }

    public void deleteUser(int position) {
        userDB.document(users.getValue().get(position).getUserID()).delete();
    }

    private HashMap<String, Object> serializeUser(String name, String password, String type){
        HashMap<String, Object> serialized = new HashMap<>();
        serialized.put(USERNAME_KEY, name);
        serialized.put(PASSWORD_KEY, password);
        serialized.put(TYPE_KEY, type);
        return serialized;
    }

    private User deserializeUser(QueryDocumentSnapshot doc){
        User user = null;
        switch (doc.get(TYPE_KEY, String.class).toLowerCase()){
            case "admin":
                user = new Admin(doc.getId(), doc.get(USERNAME_KEY, String.class), "", doc.get(USERNAME_KEY, String.class), "");
                break;
            case "instructor":
                user = new Instructor(doc.getId(), doc.get(USERNAME_KEY, String.class), "", doc.get(USERNAME_KEY, String.class), "");
                break;
            case "student":
                user = new Student(doc.getId(), doc.get(USERNAME_KEY, String.class), "", doc.get(USERNAME_KEY, String.class), "");
                break;
            default:
                break;
        }
        return user;
    }

    public void addUser(String name, String password, String type) {
        userDB.add(serializeUser(name, password, type));
    }

    public void editUser(int position, String name, String password, String type) {
        HashMap<String, Object> serialized = serializeUser(name, password, type);
        if (password == null || password.isEmpty())
            serialized.remove(PASSWORD_KEY); // Keep the password
        if (type == null || type.isEmpty())
            serialized.remove(TYPE_KEY); // Keep the user type

        String id = users.getValue().get(position).getUserID();
        userDB.document(id).update(serialized);
    }

}