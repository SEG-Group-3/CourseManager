package com.segg3.coursemanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.segg3.coursemanager.shared.dao.DataObject;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Course implements DataObject {
    @JsonIgnore
    public String id;

    public String name = "ERROR";
    public String code = "ERR 0000";
    public Date date;
    public Instructor instructor;
    public String description;
    public int capacity;
    public int registeredStudents;
    public CourseHours courseHours;

    public Course(){

        this.id = "";
    }

    public Course(String name, String code, Date date, String id) {
        this.name = name;
        this.code = code;
        this.date = date;
        this.id = id;
    }

    public Course(String id, Instructor instructor, String description, int capacity, int registeredStudents, CourseHours courseHours, String name, String code) {
        this.id = id;
        this.description = description;
        this.instructor = instructor;
        this.capacity = capacity;
        this.registeredStudents = registeredStudents;
        this.courseHours = courseHours;
        this.name = name;
        this.code = code;
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
        return "Course{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", date=" + date +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public String getPrimaryKey() {
        return code;
    }
}