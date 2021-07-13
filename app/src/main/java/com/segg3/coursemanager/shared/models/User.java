package com.segg3.coursemanager.shared.models;

import com.segg3.coursemanager.shared.dao.DataObject;

public class User extends DataObject {
    public String type = "type?";
    public String password = "password?";
    public String userName = "userName?";

    @Override
    public String getPrimaryKey() {
        return userName;
    }

}