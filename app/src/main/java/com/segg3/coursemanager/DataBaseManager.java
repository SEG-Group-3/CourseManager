package com.segg3.coursemanager;

import android.renderscript.RSInvalidStateException;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;

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
        db.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }
            readDataBase();
        });
        readDataBase();
    }

    abstract void readDataBase();
}