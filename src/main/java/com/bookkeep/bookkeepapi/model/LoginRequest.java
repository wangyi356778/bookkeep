package com.bookkeep.bookkeepapi.model;

public class LoginRequest {
    private String phone;        // 手机号登录
    private String password;

    public LoginRequest() {}

    public LoginRequest(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    // 兼容旧字段
    public String getUsername() { return phone; }
    public void setUsername(String username) { this.phone = username; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
