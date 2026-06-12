package com.bookkeep.bookkeepapi.service;

import com.bookkeep.bookkeepapi.entity.User;
import com.bookkeep.bookkeepapi.mapper.UserMapper;
import com.bookkeep.bookkeepapi.model.LoginRequest;
import com.bookkeep.bookkeepapi.model.LoginResponse;
import com.bookkeep.bookkeepapi.model.RegisterRequest;
import com.bookkeep.bookkeepapi.model.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletResponse response;

    public RegisterResponse register(RegisterRequest request) {
        String phone = request.getPhone();
        if (phone == null || phone.trim().isEmpty()) {
            return new RegisterResponse(false, "手机号不能为空");
        }
        // 简单校验手机号格式
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return new RegisterResponse(false, "手机号格式不正确");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new RegisterResponse(false, "密码不能为空");
        }
        if (request.getConfirmPassword() == null || !request.getConfirmPassword().equals(request.getPassword())) {
            return new RegisterResponse(false, "两次输入的密码不一致");
        }

        // 检查手机号是否已注册
        User existingUser = userMapper.findByPhone(phone);
        if (existingUser != null) {
            return new RegisterResponse(false, "该手机号已注册");
        }

        // 插入新用户
        Integer age = null;
        try { age = request.getAge() != null ? Integer.parseInt(request.getAge()) : null; } catch (NumberFormatException e) {}

        User user = new User(phone, request.getPassword(),
                request.getName(), age,
                request.getOccupation(), request.getGender());
        userMapper.insert(user);

        return new RegisterResponse(true, "注册成功");
    }

    public LoginResponse login(LoginRequest request) {
        String phone = request.getPhone();
        String password = request.getPassword();
        if (phone == null || phone.trim().isEmpty()) {
            return new LoginResponse(false, "手机号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return new LoginResponse(false, "密码不能为空");
        }

        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new LoginResponse(false, "该手机号未注册");
        }
        if (!user.getPassword().equals(password)) {
            return new LoginResponse(false, "密码错误");
        }
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        return new LoginResponse(true, "登录成功",token);
    }
}
