package com.segg3.coursemanager;

import com.segg3.coursemanager.shared.dao.CoursesDao;
import com.segg3.coursemanager.shared.dao.UsersDao;
import com.segg3.coursemanager.shared.models.Course;
import com.segg3.coursemanager.shared.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class CoursesDaoTest {
    private Course c;
    private CoursesDao dao;
    private final String CODE = "3210";
    private final String NAME = "TestCourse";
    private final String DESCRIPTION = "TestCourseDescription";
    private final int CAPACITY = 999;
    private final String INSTRUCTOR = "Foo";
    private final String CHOUR = "321";

    private UsersDao userDao;
    private final String testPassword = "password";
    private final String[] userTypes = new String[]{"Admin", "Student", "Instructor"};
    private final String testUserNameSufix = "TEST";

   @Before
    public void setUp() throws InterruptedException {
        dao = CoursesDao.getInstance();
        userDao = UsersDao.getInstance();

        c = new Course();
        c.code = CODE;
        c.name = NAME;
        c.description = DESCRIPTION;
        c.capacity = CAPACITY;
        c.instructor = INSTRUCTOR;
        c.courseHours = new ArrayList<>();
        c.courseHours.add(CHOUR);

       Thread.sleep(1000);
        dao.addCourse(c);
       Thread.sleep(1000);

       for(int i1 = 0; i1 < userTypes.length; i1++)
       {
           setUpTestUsers(userTypes[i1]);
           Thread.sleep(1000);
       }
    }

    private void setUpTestUsers(String type)
    {
        User tmp = new User();

        tmp.type = type;
        tmp.password = testPassword;
        tmp.userName = type+testUserNameSufix;

        userDao.addUser(tmp);

    }

    @Test
    public void testPropertyCorrectness()  {
        assertEquals(dao.getCourse(CODE).code, CODE);
        assertEquals(dao.getCourse(CODE).name, NAME);
        assertEquals(dao.getCourse(CODE).description, DESCRIPTION);
        assertEquals(dao.getCourse(CODE).capacity, CAPACITY);
        assertEquals(dao.getCourse(CODE).instructor, INSTRUCTOR);
        assertEquals(dao.getCourse(CODE).courseHours.get(0), CHOUR);
    }

    @Test
    public void studentJoinClass()
    {
        for(int i1 = 0; i1 < userTypes.length; i1++)
        {
            assertEquals(dao.joinCourse(userTypes[i1] + testUserNameSufix, CODE), userTypes[i1].equals("Instructor"));
        }
    }

    @Test
    public void studentLeaveClass()
    {

    }

    @Test
    public void assignInstructor()
    {

    }

    @Test
    public void unAssignInstructor()
    {

    }

    @After
    public void cleanUp() throws InterruptedException {
        Thread.sleep(1000); // Give some time for DB to update
        CoursesDao.getInstance().deleteCourse(CODE);
        Thread.sleep(500);

        for(int i1 = 0; i1 < userTypes.length; i1++)
        {
            removeTestUser(userTypes[i1]);
        }
    }

    private void removeTestUser(String type)
    {
        userDao.deleteUser(type+testUserNameSufix);
    }
}
