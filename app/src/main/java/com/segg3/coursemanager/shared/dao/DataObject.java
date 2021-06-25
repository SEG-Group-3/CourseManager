package com.segg3.coursemanager.shared.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class DataObject {
    @JsonIgnore
    public String id;


    @JsonIgnore
    public abstract String getPrimaryKey();
}
