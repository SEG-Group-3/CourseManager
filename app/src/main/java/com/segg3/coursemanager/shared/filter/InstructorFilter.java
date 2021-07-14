package com.segg3.coursemanager.shared.filter;

import com.segg3.coursemanager.shared.models.Course;

import java.util.ArrayList;
import java.util.List;

public class InstructorFilter extends Filter<Course> {
    @Override
    public List<Course> search(Object query, List<Course> unfiltered)
    {
        List<Course> tmp = new ArrayList<>();

        if(!(query instanceof Boolean) || query == null)
        {
            throw new IllegalArgumentException();
        }

        Boolean activeInstructor = (Boolean) query;

        Course c;

        for(int i1 = 0; i1 < unfiltered.size(); i1++)
        {
            c = unfiltered.get(i1);

            if(c.instructor.equals("") == !activeInstructor.booleanValue())
            {
                tmp.add(c);
            }
        }
        return tmp;
    }


}
