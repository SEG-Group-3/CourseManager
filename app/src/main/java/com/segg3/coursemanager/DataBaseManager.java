package com.segg3.coursemanager;

import android.renderscript.RSInvalidStateException;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public abstract class DataBaseManager
{
    private static CollectionReference db;
    protected static DataBaseManager instance;

    protected DataBaseManagerListener listener;

    public interface DataBaseManagerListener
    {
        public void onFinish(String message);
        public void onFinish(String message, Object data);
    }


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

    public void setListener(DataBaseManagerListener listener)
    {
        this.listener = listener;
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