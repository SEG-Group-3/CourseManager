package com.segg3.coursemanager.shared.models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.segg3.coursemanager.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoursesViewModel extends ViewModel {
    private MutableLiveData<HashMap<String, Course>> courses_impl;
    private MutableLiveData<List<Course>> courses;
    private static final CollectionReference courseDB = FirebaseFirestore.getInstance().collection("Courses");
    public static final String NAME_KEY = "name";
    public static final String CODE_KEY = "code";


    public LiveData<List<Course>> getCourses(){
        if (courses_impl == null){
            courses_impl = new MutableLiveData<>();
            courses_impl.setValue(new HashMap<>());
            courses = new MutableLiveData<>();
            courses.setValue(new ArrayList<>());
            loadCourses();
        }
        return courses;
    }

    public void loadCourses(){
        courseDB.addSnapshotListener((snapshots, e) -> {
            if (e != null)
                return;
            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                QueryDocumentSnapshot doc = dc.getDocument();
                Course courseTmp = deserializeCourse(doc);
                if (courseTmp == null){
                    Log.w("FIRE", "Object: " + doc.getId() + " doesn't match course signature");
                    continue;
                }
                switch (dc.getType()) {
                    case ADDED:
                    case MODIFIED:
                        courses_impl.getValue().put(doc.getId(), courseTmp);
                        break;
                    case REMOVED:
                        courses_impl.getValue().remove(doc.getId());
                        break;
                    default:
                        break;
                }
            }
            courses.setValue(new ArrayList<>(courses_impl.getValue().values()));
        });
    }

    public void deleteCourse(int position) {
        courseDB.document(courses.getValue().get(position).getId()).delete();
    }


    private HashMap<String, Object> serializeCourse(String code, String name){
        HashMap<String, Object> serialized = new HashMap<>();
        serialized.put(NAME_KEY, name);
        serialized.put(CODE_KEY, code);
        return serialized;
    }

    private Course deserializeCourse(QueryDocumentSnapshot doc){
        return new Course(doc.get(NAME_KEY, String.class), doc.get(CODE_KEY, String.class), null, null );
    }

    public void addCourse(String name, String code) {
        courseDB.add(serializeCourse(name,code));
    }

    public void editCourse(int position, String code, String name) {
        HashMap<String, Object> serialized = new HashMap<>();
        serialized.put(NAME_KEY, name);
        serialized.put(CODE_KEY, code);
        courseDB.document(courses.getValue().get(position).getId()).update(serialized);
    }
}