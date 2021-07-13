package com.segg3.coursemanager.shared.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.models.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoursesViewModel extends ViewModel {
    private LiveData<List<Course>> courses;

    public LiveData<List<Course>> getCourses() {
        if (courses == null) {
            courses = Transformations.switchMap(CoursesDao.getInstance().getCourses(), this::loadCourses);
        }
        return courses;
    }


    public LiveData<List<Course>> loadCourses(Map<String, Course> coursesMap) {
        MutableLiveData<List<Course>> liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<>(coursesMap.values()));
        return liveData;
    }

}