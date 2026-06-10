package com.example.jizhangben.model;

public class RegisterRequest {
    private String username;
    private String password;
    private String age;

    public RegisterRequest() {
    }//适配json

    public RegisterRequest(String username, String password, String age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }

    public String getUsername() {       //适配json
        return username;
    }

    public String getPassword() {       //适配json
        return password;
    }

    public String getAge() {            //适配json
        return age;
    }
    
}