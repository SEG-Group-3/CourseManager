package com.segg3.coursemanager;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Course {
    private final String id;
    public String name;
    public String code;
    public Date date;
    public Instructor instructor;
    public String description;
    public int capacity;
    public int registeredStudents;

    public Course(String name, String code, Date date, String id) {
        this.name = name;
        this.code = code;
        this.date = date;
        this.id = id;
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
}