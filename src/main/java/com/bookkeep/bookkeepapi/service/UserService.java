package com.bookkeep.bookkeepapi.service;

import com.bookkeep.bookkeepapi.model.LoginRequest;
import com.bookkeep.bookkeepapi.model.LoginResponse;
import com.bookkeep.bookkeepapi.model.RegisterRequest;
import com.bookkeep.bookkeepapi.model.RegisterResponse;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    // 内存中存储用户，key=username, value=password
    private final Map<String, String> userStore = new ConcurrentHashMap<>();

    public RegisterResponse register(RegisterRequest request) {
        String username = request.getUsername();
        if (username == null || username.trim().isEmpty()) {
            return new RegisterResponse(false, "用户名不能为空");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return new RegisterResponse(false, "密码不能为空");
        }
        if (userStore.containsKey(username)) {
            return new RegisterResponse(false, "用户名已存在");
        }
        userStore.put(username, request.getPassword());
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
        if (!userStore.containsKey(username)) {
            return new LoginResponse(false, "用户不存在");
        }
        if (!userStore.get(username).equals(password)) {
            return new LoginResponse(false, "密码错误");
        }
        return new LoginResponse(true, "登录成功");
    }
}
