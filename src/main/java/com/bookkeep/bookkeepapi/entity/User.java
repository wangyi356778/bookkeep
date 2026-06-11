package com.bookkeep.bookkeepapi.entity;

public class User {
    private Long id;
    private String phone;        // 手机号（登录账号）
    private String password;
    private String name;         // 姓名
    private Integer age;         // 年龄
    private String occupation;   // 职业
    private String gender;       // 性别：男/女

    public User() {}

    public User(String phone, String password, String name, Integer age, String occupation, String gender) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.age = age;
        this.occupation = occupation;
        this.gender = gender;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUsername() { return phone; }  // 兼容旧字段名
    public void setUsername(String username) { this.phone = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}
