package com.segg3.coursemanager.shared.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.segg3.coursemanager.shared.dao.DataObject;

public class User implements DataObject {
    @JsonIgnore
    public String id;

    public String type;
    public String userName;
    public String password;

    @Override
    public String getPrimaryKey() {
        return userName;
    }
}
