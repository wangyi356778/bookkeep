package com.example.jizhangben.model;

public class RegisterRequest {
    private String username;
    private String password;
    private String age;

    public RegisterRequest(String username, String password, String age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }
}