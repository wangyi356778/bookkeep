package com.example.jizhangben;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

        userRepository = new UserRepository();

        // 1. 找到控件
        EditText etRegPhone = findViewById(R.id.et_reg_phone);
        EditText etRegPassword = findViewById(R.id.et_reg_password);
        EditText etRegConfirmPassword = findViewById(R.id.et_reg_confirm_password);
        EditText etRegName = findViewById(R.id.et_reg_name);
        EditText etRegAge = findViewById(R.id.et_reg_age);
        EditText etRegOccupation = findViewById(R.id.et_reg_occupation);
        Spinner spinnerGender = findViewById(R.id.spinner_gender);
        Button btnRegister = findViewById(R.id.btn_register_confirm);

        // 性别选择默认值
        final String[] selectedGender = {""};
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {  // 跳过"请选择性别"
                    selectedGender[0] = parent.getItemAtPosition(position).toString();
                } else {
                    selectedGender[0] = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 2. 点击注册按钮
        btnRegister.setOnClickListener(v -> {
            String phone = etRegPhone.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();
            String confirmPassword = etRegConfirmPassword.getText().toString().trim();
            String name = etRegName.getText().toString().trim();
            String age = etRegAge.getText().toString().trim();
            String occupation = etRegOccupation.getText().toString().trim();
            String gender = selectedGender[0];

            // 校验必填项
            if (phone.isEmpty()) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!phone.matches("^1[3-9]\\d{9}$")) {
                Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. 调用 Repository 进行注册
            userRepository.register(phone, password, confirmPassword,
                    name, age, occupation, gender,
                    new UserRepository.RegisterCallback() {
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
