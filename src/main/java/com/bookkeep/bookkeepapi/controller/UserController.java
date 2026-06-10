package com.bookkeep.bookkeepapi.controller;

import com.bookkeep.bookkeepapi.model.LoginRequest;
import com.bookkeep.bookkeepapi.model.LoginResponse;
import com.bookkeep.bookkeepapi.model.RegisterRequest;
import com.bookkeep.bookkeepapi.model.RegisterResponse;
import com.bookkeep.bookkeepapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
