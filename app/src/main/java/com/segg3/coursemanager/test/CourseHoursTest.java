package com.segg3.coursemanager.test;

import com.segg3.coursemanager.shared.models.CourseHours;

import junit.framework.TestCase;

import org.junit.Test;

import java.time.DayOfWeek;

public class CourseHoursTest extends TestCase {

    @Test
    public void testConstructors()
    {
        CourseHours courseHours1 = new CourseHours(DayOfWeek.FRIDAY, new CourseHours.Time(11,11), 60);

        CourseHours courseHours2 = new CourseHours(courseHours1.toString());

        assertEquals(courseHours1.toString(), courseHours2.toString());
    }
    @Test
    public void testCompareTo() {

        CourseHours courseHours1 = new CourseHours(DayOfWeek.MONDAY, new CourseHours.Time(11,11), 60);

        CourseHours courseHours2 = new CourseHours(DayOfWeek.TUESDAY, new CourseHours.Time(11,11), 60);

        CourseHours courseHours3 = new CourseHours(DayOfWeek.TUESDAY, new CourseHours.Time(12,00), 60);

        //check if courseHours1 is less than courseHours2
        assertEquals(-1, courseHours1.compareTo(courseHours2));

        //check if courseHours1 intersects with courseHours2
        assertEquals(0, courseHours2.compareTo(courseHours3));
        assertEquals(0, courseHours3.compareTo(courseHours2));

        //check if courseHours2 is less than courseHours1
        assertEquals(1, courseHours2.compareTo(courseHours1));
    }

    @Test
    public void testTimeConstructor()
    {
        CourseHours.Time t1,t2;

        t1 = new CourseHours.Time(12,1);
        t2 = new CourseHours.Time(t1.toString());

        assertEquals(t1.toString(),t2.toString());

        t1 = new CourseHours.Time(12,00);
        t2 = new CourseHours.Time(t1.toString());

        assertEquals(t1.toString(),t2.toString());

        t1 = new CourseHours.Time(12,00);
        t2 = new CourseHours.Time("12:00");

        assertEquals(t1.toString(),t2.toString());
    }
}