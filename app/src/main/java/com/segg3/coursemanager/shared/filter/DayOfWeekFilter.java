package com.segg3.coursemanager.shared.filter;

import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.CourseHours;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekFilter extends Filter<Course> {
    @Override
    public List<Course> search(Object query, List<Course> unfiltered) {
        if(query == null)
        {
            throw new NullPointerException();
        }

        if(!query.getClass().isArray())
        {
            throw new IllegalArgumentException();
        }

        boolean[] weekQuery = (boolean[]) query;

        if(weekQuery.length != 7)
        {
            throw new IllegalArgumentException();
        }

        List<Course> tmp = new ArrayList<Course>();
        Course c;
        CourseHours courseHours;
        boolean check;
        for(int i1 = 0; i1 < unfiltered.size(); i1++)
        {
            c = unfiltered.get(i1);

            check = true;

            if(c.courseHours.size() == 0)
            {
                check = false;
            }

            for(int i2 = 0; i2 < c.courseHours.size(); i2++)
            {
                courseHours = new CourseHours(c.courseHours.get(i2));

                if(!weekQuery[courseHours.weekDay.getValue() - 1])
                {
                    check = false;
                    break;
                }
            }

            if(check)
            {
                tmp.add(c);
            }
        }

        return tmp;
    }
}
