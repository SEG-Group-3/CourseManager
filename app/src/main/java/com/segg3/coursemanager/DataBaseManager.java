package com.segg3.coursemanager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.concurrent.Executor;

public abstract class DataBaseManager
{
    private static CollectionReference db;

    protected static DataBaseManager instance;

    protected OnFailureListener onFailureListener;
    protected OnSuccessListener onSuccessListener;


    public static DataBaseManager getInstance()
    {
        throw new IllegalStateException();
    }

    protected DataBaseManager()
    {
    /*    DataBaseManager.db.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }
            readDataBase();
        });
        readDataBase();*/
    }

    /*
    public void setListener(DataBaseManagerListener listener)
    {
        this.listener = listener;
    }
*/

    public DataBaseManager addOnFailureListener(OnFailureListener listener)
    {
        onFailureListener = listener;
        return this;
    }

    public DataBaseManager addOnSuccessListener(OnSuccessListener listener)
    {
        onSuccessListener = listener;
        return this;
    }

    protected String get(QueryDocumentSnapshot document, String key)
    {
        if(document.contains(key))
        {
            return document.get(key, String.class);
        }
        return null;
    }

    abstract void readDataBase();
}