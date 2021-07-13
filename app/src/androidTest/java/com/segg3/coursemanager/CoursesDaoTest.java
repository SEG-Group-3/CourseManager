package com.segg3.coursemanager;

import android.util.Log;

import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.CourseHours;
import com.segg3.coursemanager.shared.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CoursesDaoTest {
    private Course c;
    private CoursesDao courseDao;

    private final String CODEPREFIX = "TEST-";
    private final String NAMEPREFIX = "TestCourse";
    private final String DESCRIPTION = "TEST COURSE TEST COURSE. NOT A REAL COURSE";

    private UsersDao userDao;
    private final String testPassword = "password";
    private final String[] userTypes = new String[]{"Admin", "Student", "Instructor"};
    private final String testUserNameSufix = "TEST";

   @Before
    public void setUp() throws InterruptedException {
        courseDao = CoursesDao.getInstance();
        userDao = UsersDao.getInstance();

        for(int i1 = 1; i1 <= 3; i1++)
        {
            setUpTestCourse(i1);
        }


        for(int i1 = 0; i1 < userTypes.length; i1++)
        {
            setUpTestUsers(userTypes[i1]);
        }
        Thread.sleep(5000);
    }
    private void setUpTestCourse(int count) throws InterruptedException {
        Course c = new Course();

        c.code = CODEPREFIX + count;
        c.name = NAMEPREFIX + " - " + count;
        c.description = DESCRIPTION;
        courseDao.addCourse(c);

        TimeUnit.SECONDS.sleep(2);
    }
    private void setUpTestUsers(String type) throws InterruptedException {
        User tmp = new User();

        tmp.type = type;
        tmp.password = testPassword;
        tmp.userName = type+testUserNameSufix;

        userDao.addUser(tmp);
        TimeUnit.SECONDS.sleep(2);

    }

    @Test
    public void testEditCourseProperty() throws InterruptedException {

       int capacity = 420;
       String description = "TEST DESCRIPTION";
       String name = "TEST NAME";
       String instructor = userTypes[2]+testUserNameSufix;


       String code = CODEPREFIX + 1;

       Course c = courseDao.getCourse(code);

       c.capacity = capacity;
       c.description = description;
       c.name = name;
       c.instructor = instructor;

       courseDao.editCourse(code, c);

       TimeUnit.SECONDS.sleep(5);

       c = courseDao.getCourse(code);

       assertEquals(capacity, c.capacity);
       assertEquals(description, c.description);
       assertEquals(name, c.name);
       assertEquals(instructor, c.instructor);
    }

    @Test
    public void testSearchCourse() throws InterruptedException {
        List<Course> query = courseDao.searchCourse(CODEPREFIX);

        Log.d("DEBUG TEST",query.size()+"");
        for(int i1 = 0; i1 < query.size(); i1++)
        {
            Log.d("DEBUG TEST",query.get(i1).code);
        }

        assertEquals(3, query.size());

        query = courseDao.searchCourse(CODEPREFIX+1);

        assertEquals(1, query.size());
        assertEquals(CODEPREFIX+1, query.get(0).code);

        Course c = courseDao.getCourse(CODEPREFIX+2);
        c.name = "TEST NAME TEST NAME";

        courseDao.editCourse(c.code, c);

        TimeUnit.SECONDS.sleep(5);

        query = courseDao.searchCourse(CODEPREFIX+1);

        assertEquals(1, query.size());
        assertEquals("TEST NAME TEST NAME", query.get(0).name);

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testUserJoinCourse() throws InterruptedException {
        String code = CODEPREFIX + 1;

        Course c = courseDao.getCourse(code);
        c.capacity = 1;
        courseDao.editCourse(code, c);

        TimeUnit.SECONDS.sleep(1);

        //join course
        for(int i1 = 0; i1 < userTypes.length; i1++)
        {
            assertEquals(userTypes[i1].equals("Student"), courseDao.joinCourse(userTypes[i1] + testUserNameSufix, code));
        }

        c = courseDao.getCourse(code);

        assertTrue(c.enrolled.contains(userTypes[1] + testUserNameSufix));

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testTimeJoinCourse() throws InterruptedException
    {
        String code1 = CODEPREFIX + 1;
        String code2 = CODEPREFIX + 2;
        String code3 = CODEPREFIX + 3;

        Course course1 = courseDao.getCourse(code1);
        Course course2 = courseDao.getCourse(code2);
        Course course3 = courseDao.getCourse(code3);

        CourseHours courseHours1 = new CourseHours(DayOfWeek.TUESDAY, new CourseHours.Time(1,00),60 * 3);
        CourseHours courseHours2 = new CourseHours(DayOfWeek.MONDAY, new CourseHours.Time(1,00),60 * 3);
        CourseHours courseHours3 = new CourseHours(DayOfWeek.MONDAY, new CourseHours.Time(2,00),60 * 3);

        course1.courseHours.add(courseHours1.toString());
        course1.capacity = 1;
        course2.courseHours.add(courseHours2.toString());
        course2.capacity = 1;
        course3.courseHours.add(courseHours3.toString());
        course3.capacity = 1;

        courseDao.editCourse(code1, course1);
        courseDao.editCourse(code2, course2);
        courseDao.editCourse(code3, course3);

        TimeUnit.SECONDS.sleep(5);

        //starting tests

        User student = userDao.getUser(userTypes[1]+testUserNameSufix);

        assertTrue(courseDao.joinCourse(student.userName, code1));
        course1 = courseDao.getCourse(code1);
        assertTrue(course1.enrolled.contains(userTypes[1] + testUserNameSufix));

        assertTrue(courseDao.joinCourse(student.userName, code2));
        course2 = courseDao.getCourse(code2);
        assertTrue(course2.enrolled.contains(userTypes[1] + testUserNameSufix));

        assertFalse(courseDao.joinCourse(student.userName, code3));
        course3 = courseDao.getCourse(code3);
        assertFalse(course3.enrolled.contains(userTypes[1] + testUserNameSufix));

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void LeaveClassCourse() throws InterruptedException {

        String code = CODEPREFIX + 1;

        Course c = courseDao.getCourse(code);
        c.capacity = 1;
        courseDao.editCourse(code, c);

        TimeUnit.SECONDS.sleep(5);

        //setting up test course with students in it
        courseDao.joinCourse(userTypes[1]+testUserNameSufix, code);
        TimeUnit.SECONDS.sleep(5);

        //testing removing courses
        assertTrue(courseDao.leaveCourse(userTypes[1]+testUserNameSufix, code));

        for(int i1 = 0; i1 < userTypes.length;i1++)
        {
            assertFalse(courseDao.leaveCourse(userTypes[i1]+testUserNameSufix, code));
        }
    }

    @Test
    public void testAssignInstructor() throws InterruptedException {
        String code = CODEPREFIX + 1;
        assertTrue(courseDao.assignInstructor(userTypes[2] + testUserNameSufix, code));

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testUnassignInstructor() throws InterruptedException {
        String code = CODEPREFIX + 1;
        String instructor = userTypes[2] + testUserNameSufix;
        courseDao.assignInstructor(instructor,code);

        TimeUnit.SECONDS.sleep(5);

        courseDao.unAssignInstructor(code);

        assertEquals("", courseDao.getCourse(code).instructor);
    }

    @Test
    public void testGetStudents() throws InterruptedException {
        String code = CODEPREFIX + 1;

        String studentUserName = userTypes[1]+testUserNameSufix;

        Course c = courseDao.getCourse(code);


        c.capacity = 1;

        courseDao.editCourse(code, c);

        courseDao.joinCourse(studentUserName, code);

        TimeUnit.SECONDS.sleep(5);

        c = courseDao.getCourse(code);

        assertEquals(1, c.registeredStudents);
        assertEquals(1, c.enrolled.size());
        assertTrue(c.enrolled.contains(studentUserName));
    }

    @After
    public void cleanUp() {
        for(int i1 = 1; i1 <= 3; i1++) {
            removeTestCourse(i1);
        }

        for(int i1 = 0; i1 < userTypes.length; i1++)
        {
            removeTestUser(userTypes[i1]);
        }
    }

    private void removeTestCourse(int count)
    {
        courseDao.deleteCourse(CODEPREFIX + count);
    }

    private void removeTestUser(String type)
    {
        userDao.deleteUser(type+testUserNameSufix);
    }
}
