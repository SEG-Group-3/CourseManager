package com.segg3.coursemanager;

import android.renderscript.RSInvalidStateException;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public abstract class DataBaseManager
{
    private static CollectionReference db;
    protected static DataBaseManager instance;

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

    protected String get(QueryDocumentSnapshot document, String key)
    {
        if(document.contains(key))
        {
            return document.get(key, String.class);
        }
        return "";
    }

    abstract void readDataBase();
}