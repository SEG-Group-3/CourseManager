package com.segg3.coursemanager.shared.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface DataObject {
    @JsonIgnore
    String getPrimaryKey();
}
