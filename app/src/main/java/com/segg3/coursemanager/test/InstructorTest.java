package com.segg3.coursemanager.test;

import com.google.firebase.FirebaseApp;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.Instructor;

import junit.framework.TestCase;

import org.junit.Before;

import static org.junit.Assert.*;

public class InstructorTest extends TestCase {
//
//    private FirebaseApp Firebase;
//
//    @Before
//    public void beforeTest() {
//        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
//        InstrumentationRegistry.getInstrumentation().targetContext;
//
//    }


    public void testCapacity() {

        Course newCourse = new Course();
        newCourse.name = "Software Engineering";
        newCourse.code = "SEG2105";
        newCourse.capacity = 45;
        Course expected = new Course();
        expected.setCapacity(45);

        assertEquals(expected.capacity, newCourse.capacity);
    }
//
//    public void testDescription() {
//        Course newCourse = new Course();
//        newCourse.description = "Welcome to Software Engineering!";
//        Instructor newInstructor = new Instructor();
//        Course expected = new Course();
//        String description = "Welcome to Software Engineering!";
//        newInstructor.setDesc(expected, description);
//        assertEquals(expected.description, newCourse.description);
//    }
}