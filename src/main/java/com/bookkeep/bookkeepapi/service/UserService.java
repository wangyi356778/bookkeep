package com.bookkeep.bookkeepapi.service;

import com.bookkeep.bookkeepapi.entity.User;
import com.bookkeep.bookkeepapi.mapper.UserMapper;
import com.bookkeep.bookkeepapi.model.LoginRequest;
import com.bookkeep.bookkeepapi.model.LoginResponse;
import com.bookkeep.bookkeepapi.model.RegisterRequest;
import com.bookkeep.bookkeepapi.model.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public RegisterResponse register(RegisterRequest request) {
        String username = request.getUsername();
        if (username == null || username.trim().isEmpty()) {
            return new RegisterResponse(false, "用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new RegisterResponse(false, "密码不能为空");
        }

        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(username);
        if (existingUser != null) {
            return new RegisterResponse(false, "用户名已存在");
        }

        // 插入新用户
        User user = new User(username, request.getPassword(),
                request.getAge() != null ? Integer.parseInt(request.getAge()) : null);
        userMapper.insert(user);

        return new RegisterResponse(true, "注册成功");
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        if (username == null || username.trim().isEmpty()) {
            return new LoginResponse(false, "用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return new LoginResponse(false, "密码不能为空");
        }

        User user = userMapper.findByUsername(username);
        if (user == null) {
            return new LoginResponse(false, "用户不存在");
        }
        if (!user.getPassword().equals(password)) {
            return new LoginResponse(false, "密码错误");
        }
        return new LoginResponse(true, "登录成功");
    }
}