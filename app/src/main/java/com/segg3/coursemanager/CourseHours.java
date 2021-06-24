package com.segg3.coursemanager;

import java.time.DayOfWeek;
import java.util.Date;

import kotlin.collections.IntIterator;

public class CourseHours {

    public final DayOfWeek weekDay;

    public final Date start;

    public final float durations;

    public CourseHours(DayOfWeek weekDay, Date start, float durations)
    {
        this.weekDay = weekDay;
        this.start = start;
        this. durations = durations;
    }

    public CourseHours(String CourseHoursRaw)
    {
        String[] tmpParam = CourseHoursRaw.split("|");

        this.weekDay = DayOfWeek.valueOf(tmpParam[0]);

        String[] tmpDate = tmpParam[1].split(":");

        Integer hour = new Integer(tmpDate[0]);
        Integer min = new Integer(tmpDate[1]);

        this.start = new Date(0,0,0, hour.intValue(), min.intValue());

        Float duration = new Float(tmpParam[2]);

        this.durations = duration.floatValue();
    }
}
