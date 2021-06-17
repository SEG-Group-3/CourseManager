package com.segg3.coursemanager.shared.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.segg3.coursemanager.shared.dao.DataObject;

public class Course implements DataObject {
    @JsonIgnore
    public String id;

    public String code;
    public String name;

    @Override
    public String getPrimaryKey() {
        return code;
    }
}
