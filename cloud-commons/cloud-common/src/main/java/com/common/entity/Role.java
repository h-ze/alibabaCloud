package com.common.entity;

import java.io.Serializable;

public class Role implements Serializable {
    private static final long serialVersionUID = 123L;

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
