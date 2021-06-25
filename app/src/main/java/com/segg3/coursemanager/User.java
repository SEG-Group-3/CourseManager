package com.segg3.coursemanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.segg3.coursemanager.shared.dao.DataObject;

public class User implements DataObject {
    @JsonIgnore
    public String id;


    public String type = "ERROR";
    public String password = "ERROR";
    public String userName = "ERROR";

    public User(){

    }

    @Override
    public String getPrimaryKey() {
        return userName;
    }

}