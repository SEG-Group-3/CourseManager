package com.segg3.coursemanager.shared.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.models.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoursesViewModel extends ViewModel {
    private LiveData<List<Course>> courses;
    private final CoursesDao dao = CoursesDao.getInstance();

    public LiveData<List<Course>> getCourses() {
        if (courses == null) {
            courses = Transformations.switchMap(dao.getCourses(), this::loadCourses);
        }
        return courses;
    }


    public LiveData<List<Course>> loadCourses(HashMap<String, Course> coursesMap) {
        MutableLiveData liveData = new MutableLiveData();
        liveData.setValue(new ArrayList<>(coursesMap.values()));
        return liveData;
    }

}