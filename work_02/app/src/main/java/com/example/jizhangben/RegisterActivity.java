package com.example.jizhangben;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jizhangben.model.RegisterResponse;
import com.example.jizhangben.repository.UserRepository;

public class RegisterActivity extends AppCompatActivity {

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. 初始化 Repository
        userRepository = new UserRepository();

        // 2. 找到控件
        EditText etRegUsername = findViewById(R.id.et_reg_username);
        EditText etRegPassword = findViewById(R.id.et_reg_password);
        EditText etRegAge = findViewById(R.id.et_reg_age);
        Button btnRegister = findViewById(R.id.btn_register_confirm);

        // 3. 点击注册按钮
        btnRegister.setOnClickListener(v -> {
            String username = etRegUsername.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();
            String age = etRegAge.getText().toString().trim();

            // 简单校验
            if (username.isEmpty() || password.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. 调用 Repository 进行注册
            userRepository.register(username, password, age, new UserRepository.RegisterCallback() {
                @Override
                public void onSuccess(RegisterResponse response) {
                    if (response.isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        finish(); // 返回登录页
                    } else {
                        Toast.makeText(RegisterActivity.this, "注册失败：" + response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}