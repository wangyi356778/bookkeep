package com.example.jizhangben;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jizhangben.model.LoginResponse;
import com.example.jizhangben.repository.UserRepository;

public class LoginActivity extends AppCompatActivity {

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = new UserRepository();

        // 1. 找到界面组件
        EditText etUsername = findViewById(R.id.et_username);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        // 2. 给登录按钮加点击事件
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }

            userRepository.login(username, password, new UserRepository.LoginCallback() {
                @Override
                public void onSuccess(LoginResponse response) {
                    if (response.isSuccess()) {
                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // 3. 给注册按钮加点击事件
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}