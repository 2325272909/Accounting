package com.example.accounting.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

    private Long id;
    private String userName;
    private String userPassword;
    private String userPhone;
}
