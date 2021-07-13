package com.segg3.coursemanager.shared.filter;

import com.segg3.coursemanager.shared.models.Course;

import java.util.List;

public class nameFilter extends Filter<Course> {
    @Override
    public List<Course> search(Object query) {
        throw new UnsupportedOperationException();
    }
}
