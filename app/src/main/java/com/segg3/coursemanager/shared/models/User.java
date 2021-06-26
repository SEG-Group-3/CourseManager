package com.segg3.coursemanager.shared.models;

import com.segg3.coursemanager.shared.dao.DataObject;

public class User extends DataObject {
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