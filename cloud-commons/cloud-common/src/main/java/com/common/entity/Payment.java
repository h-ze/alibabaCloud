package com.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Payment implements Serializable {
    private String id;
    private String name;
}
