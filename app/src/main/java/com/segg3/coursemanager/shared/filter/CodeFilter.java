package com.segg3.coursemanager.shared.filter;

import com.segg3.coursemanager.shared.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CodeFilter extends Filter<Course> {
    @Override
    public List<Course> search(Object query, List<Course> unfiltered) {
        if(!(query instanceof String) || query == null)
        {
            throw new IllegalArgumentException();
        }

        String codeQuery = (String) query;

        List<Course> tmp = new ArrayList<>();

        Course c;

        for(int i1 = 0; i1 < unfiltered.size(); i1++)
        {
            c = unfiltered.get(i1);
            if(c.code.toLowerCase().contains(codeQuery.toLowerCase()))
            {
                tmp.add(c);
            }
        }

        return tmp;
    }
}
