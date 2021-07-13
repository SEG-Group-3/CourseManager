package com.segg3.coursemanager.shared.dao;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.segg3.coursemanager.shared.utils.TaskCallback;

import java.security.KeyException;
import java.util.HashMap;
import java.util.Map;


public class DataAccessObject<T extends DataObject> {
    protected final HashMap<String, String> keyToIdMap = new HashMap<>();
    private final CollectionReference collectionRef;
    private final Class<T> valueType;
    protected MutableLiveData<HashMap<String, T>> cache;

    public DataAccessObject(String collectionName, Class<T> valueType) {
        collectionRef = FirebaseFirestore.getInstance().collection(collectionName);
        cache = new MutableLiveData<>(new HashMap<>());

        this.valueType = valueType;
        // Synchronizes the DAO cache with the database
        collectionRef.addSnapshotListener((snapshots, e) -> {
            if (e != null)
                return;
            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                QueryDocumentSnapshot doc = dc.getDocument();

                T newObject;
                try {
                    newObject = doc.toObject(valueType);
                    newObject.id = doc.getId();
                } catch (Exception ignore) {
                    Log.d("DAO", "DataAccessObject: Failed to deserialize " + doc.getId() + " ignoring.");
                    continue;
                }

                switch (dc.getType()) {
                    case ADDED:
                        // Try to remove a key if repetitions are found
                        if (keyToIdMap.containsKey(newObject.getPrimaryKey())) {
                            delete(newObject.getPrimaryKey());
                            Log.d("DAO", "DataAccessObject: Found repeated key " + doc.getId() +
                                    " while building the cache.");
                            break;
                        }
                    case MODIFIED:

                        keyToIdMap.put(newObject.getPrimaryKey(), doc.getId());
                        cache.getValue().put(newObject.getPrimaryKey(), newObject);
                        cache.setValue(cache.getValue());
                        break;
                    case REMOVED:
                        cache.getValue().remove(newObject.getPrimaryKey());
                        cache.setValue(cache.getValue());
                        keyToIdMap.remove(newObject.getPrimaryKey());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // The methods get, add, and delete should be
    // abstracted by an inherited class
    protected T get(String key) {
        return cache.getValue().get(key);
    }

    protected TaskCallback<T> add(T object) {
        TaskCallback<T> callback = new TaskCallback<>();
        if (cache.getValue().containsKey(object.getPrimaryKey())) {
            callback.lazyError(new KeyException("Key is already present"));
        } else {
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.convertValue(object, HashMap.class);

            collectionRef.add(map).addOnCompleteListener(task -> {
                if (!task.isSuccessful())
                    callback.lazyError(task.getException());
                else
                    callback.accept(null);
            });
        }

        return callback;
    }

    protected TaskCallback<T> put(T object) {
        TaskCallback<T> callback = new TaskCallback<>();
        if (!cache.getValue().containsKey(object.getPrimaryKey())) {
            return add(object);
        }
        String docId = keyToIdMap.get(object.getPrimaryKey());

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(object, HashMap.class);
        collectionRef.document(docId).update(map).addOnCompleteListener(task -> {
            // Get matching field
            if (!task.isSuccessful())
                callback.lazyError(task.getException());
            else {
                callback.accept(null);
            }
        });

        return callback;
    }

    protected TaskCallback<Boolean> delete(String key) {
        TaskCallback<Boolean> callback = new TaskCallback<>();

        String id = keyToIdMap.get(key);
        if (id == null) {
            callback.lazyError(new Exception("Key does not maps to any id"));
            return callback;
        }
        collectionRef.document(id).delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("DAO", "delete: Delete item with key " + key);
                callback.accept(true);
            } else {
                callback.error(task.getException());
            }
        });

        return callback;
    }


    /**
     * Overrides the bottom value with the ones on the top and returns a new instance of T
     *
     * @param bottom
     * @param top
     * @return A merged T type from bottom and type
     */
    public T merge(T bottom, T top) {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> firstMap = mapper.convertValue(bottom, HashMap.class);
        Map<String, Object> secondMap = mapper.convertValue(top, HashMap.class);

        for (Map.Entry<String, Object> entry : secondMap.entrySet()) {

            if (entry.getValue() != null)
                firstMap.put(entry.getKey(), entry.getValue());
        }
        return mapper.convertValue(firstMap, valueType);
    }


}
