package com.segg3.coursemanager;

import java.time.DayOfWeek;
import java.util.Date;

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
}
