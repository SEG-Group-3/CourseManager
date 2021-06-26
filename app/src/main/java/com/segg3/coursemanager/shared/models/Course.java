package com.segg3.coursemanager.shared.models;

import com.segg3.coursemanager.shared.dao.DataObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Course extends DataObject {
    public String name = "ERROR";
    public String code = "ERR 0000";
    public String instructor = "";
    public String description = "";
    public int capacity = -1;
    public int registeredStudents = 0;
    public List<String> courseHours = new ArrayList<>();

    public Course(){
        courseHours.add("1|10:00|30");
        courseHours.add("2|16:00|50");
        this.id = "";
    }

    public Course(String name, String code, String id) {
        this.name = name;
        this.code = code;
        this.id = id;
    }



    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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



        tmp+="}";
        tmp="}";

        return tmp;
    }



    @Override
    public String getPrimaryKey() {
        return code;
    }
}