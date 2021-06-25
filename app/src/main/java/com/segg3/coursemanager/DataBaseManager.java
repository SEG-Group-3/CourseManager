package com.segg3.coursemanager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public abstract class DataBaseManager
{
    private static CollectionReference db;

    protected static DataBaseManager instance;

    public interface DataBaseManagerListener
    {
        default public void onFailure(Exception e)
        {

        }

        default public void onProgress(String message, Object data)
        {

        }

        default public void onSuccess(Object result)
        {

        }
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

    /*
    public void setListener(DataBaseManagerListener listener)
    {
        this.listener = listener;
    }
*/

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