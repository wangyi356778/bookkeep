package com.example.jizhangben;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jizhangben.repository.UserRepository;
import com.example.jizhangben.utils.TokenManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private EditText etName, etAge, etOccupation;
    private Spinner spGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userRepository = new UserRepository();

        etName = findViewById(R.id.et_edit_name);
        etAge = findViewById(R.id.et_edit_age);
        etOccupation = findViewById(R.id.et_edit_occupation);
        spGender = findViewById(R.id.sp_edit_gender);
        Button btnSave = findViewById(R.id.btn_save_profile);
        Button btnBack = findViewById(R.id.btn_back);

        // 性别选择器
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this, R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(genderAdapter);

        // 加载当前用户信息
        loadCurrentInfo();

        // 保存按钮
        btnSave.setOnClickListener(v -> saveProfile());

        // 返回按钮
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadCurrentInfo() {
        String phone = TokenManager.getPhone(this);
        if (phone == null) return;

        userRepository.getUserInfo(phone, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    try {
                        LinkedHashMap<String, Object> user =
                                (LinkedHashMap<String, Object>) response.getData();

                        String name = (String) user.get("name");
                        if (name != null) etName.setText(name);

                        Object ageObj = user.get("age");
                        if (ageObj != null) etAge.setText(String.valueOf(ageObj));

                        String occupation = (String) user.get("occupation");
                        if (occupation != null) etOccupation.setText(occupation);

                        String gender = (String) user.get("gender");
                        if (gender != null) {
                            String[] genders = getResources().getStringArray(R.array.gender_options);
                            for (int i = 0; i < genders.length; i++) {
                                if (gender.equals(genders[i])) {
                                    spGender.setSelection(i);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // 加载失败，使用默认值
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(EditProfileActivity.this, "加载用户信息失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String occupation = etOccupation.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();

        String phone = TokenManager.getPhone(this);
        if (phone == null) return;

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name.isEmpty() ? null : name);
        userMap.put("age", ageStr.isEmpty() ? null : Integer.parseInt(ageStr));
        userMap.put("occupation", occupation.isEmpty() ? null : occupation);
        userMap.put("gender", gender.equals("请选择") ? null : gender);

        userRepository.updateUserProfile(userMap, phone, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess()) {
                    Toast.makeText(EditProfileActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(EditProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
