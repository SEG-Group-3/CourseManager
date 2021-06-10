package com.segg3.coursemanager;

import java.util.Date;

class Course {
    public String name;
    public String code;
    public Date date;
    private final String id;

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

    //Getters

    public String getId() {
        return this.id;
    }

}