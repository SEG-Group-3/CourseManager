package com.segg3.coursemanager;

import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.CourseHours;
import com.segg3.coursemanager.shared.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
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
       TimeUnit.SECONDS.sleep(5);
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
    public void testPropertyCorrectness()  {


       /*
        assertEquals(courseDao.getCourse(CODE).code, CODE);
        assertEquals(courseDao.getCourse(CODE).name, NAME);
        assertEquals(courseDao.getCourse(CODE).description, DESCRIPTION);
        assertEquals(courseDao.getCourse(CODE).capacity, CAPACITY);
        assertEquals(courseDao.getCourse(CODE).instructor, INSTRUCTOR);
        assertEquals(courseDao.getCourse(CODE).courseHours.get(0), CHOUR);*/
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
        assertTrue(courseDao.joinCourse(student.userName, code2));
        assertFalse(courseDao.joinCourse(student.userName, code3));

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
    public void assignInstructorUnassignInstructor() throws InterruptedException {
        String code = CODEPREFIX + 0;
        assertTrue(courseDao.assignInstructor(userTypes[2] + testUserNameSufix, code));

        TimeUnit.SECONDS.sleep(1);
        courseDao.unAssignInstructor(code);

        assertEquals("", courseDao.getCourse(code).instructor);
    }



    @After
    public void cleanUp() throws InterruptedException {
       TimeUnit.SECONDS.sleep(1);
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
