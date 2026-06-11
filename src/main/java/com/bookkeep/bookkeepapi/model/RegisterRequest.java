package com.bookkeep.bookkeepapi.model;

public class RegisterRequest {
    private String phone;        // 手机号
    private String password;
    private String confirmPassword;
    private String name;         // 姓名
    private String age;
    private String occupation;   // 职业
    private String gender;       // 性别

    public RegisterRequest() {}

    public RegisterRequest(String phone, String password, String confirmPassword,
                           String name, String age, String occupation, String gender) {
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.age = age;
        this.occupation = occupation;
        this.gender = gender;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // 兼容旧字段
    public String getUsername() { return phone; }
    public void setUsername(String username) { this.phone = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
