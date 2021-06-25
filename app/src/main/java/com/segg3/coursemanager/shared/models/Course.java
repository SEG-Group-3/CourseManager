package com.segg3.coursemanager.shared.models;

import com.segg3.coursemanager.shared.dao.DataObject;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Course extends DataObject {
    public String name = "ERROR";
    public String code = "ERR 0000";
    public Instructor instructor;
    public String description;
    public int capacity;
    public int registeredStudents;
    public CourseHours[] courseHours;

    public Course(){

        this.id = "";
    }

    public Course(String name, String code, Date date, String id) {
        this.name = name;
        this.code = code;
        this.id = id;
    }

    public Course(String id, Instructor instructor, String description, int capacity, int registeredStudents, String[] courseHoursRaw, String name, String code) {
        this.id = id;
        this.description = description;
        this.instructor = instructor;
        this.capacity = capacity;
        this.registeredStudents = registeredStudents;
        this.name = name;
        this.code = code;

        courseHours = new CourseHours[courseHoursRaw.length];

        for(int i1 = 0; i1 < courseHoursRaw.length; i1++)
        {
            courseHours[i1] = new CourseHours(courseHoursRaw[i1]);
        }
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public void setRegisteredStudents(int registeredStudents) {
        this.registeredStudents = registeredStudents;
    }

    //Getters

    public String getId() {
        return this.id;
    }

    @NotNull
    @Override
    public String toString() {

        String tmp = "Course{";

        tmp+="name='" + name + '\'';
        tmp+="\tcode='" + code + '\'';
        tmp+="\tid='" + id + '\'';

        tmp+="\tcourseHours{";

        for(CourseHours c: courseHours)
        {

            tmp+= c.toString();
            if(c != courseHours[courseHours.length - 1])
            {
                tmp+=", ";
            }
        }

        tmp+="}";
        tmp="}";

        return tmp;
    }



    @Override
    public String getPrimaryKey() {
        return code;
    }
}