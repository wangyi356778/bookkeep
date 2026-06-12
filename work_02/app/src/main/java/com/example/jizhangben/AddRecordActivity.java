package com.example.jizhangben;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jizhangben.model.AddRecordRequest;
import com.example.jizhangben.repository.UserRepository;
import com.example.jizhangben.utils.TokenManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddRecordActivity extends AppCompatActivity {

    private RadioGroup rgType;
    private RadioButton rbExpense, rbIncome;
    private Spinner spCategory;
    private EditText etAmount, etDate, etRemark;
    private UserRepository userRepository;

    // 支出分类
    private static final String[] EXPENSE_CATEGORIES = {
            "餐饮", "交通", "购物", "娱乐", "居住",
            "医疗", "教育", "通讯", "日用", "其他"
    };

    // 收入分类
    private static final String[] INCOME_CATEGORIES = {
            "工资", "奖金", "兼职", "投资", "红包",
            "退款", "其他收入"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        userRepository = new UserRepository();

        rgType = findViewById(R.id.rg_type);
        rbExpense = findViewById(R.id.rb_expense);
        rbIncome = findViewById(R.id.rb_income);
        spCategory = findViewById(R.id.sp_category);
        etAmount = findViewById(R.id.et_amount);
        etDate = findViewById(R.id.et_date);
        etRemark = findViewById(R.id.et_remark);

        // 默认选中支出
        setupCategorySpinner(EXPENSE_CATEGORIES);

        // 切换收支类型时更新类目
        rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_expense) {
                setupCategorySpinner(EXPENSE_CATEGORIES);
            } else {
                setupCategorySpinner(INCOME_CATEGORIES);
            }
        });

        // 日期选择器 - 默认今天
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Calendar.getInstance().getTime());
        etDate.setText(today);

        etDate.setOnClickListener(v -> showDatePicker());

        // 取消按钮
        findViewById(R.id.btn_record_back).setOnClickListener(v -> finish());

        // 保存按钮
        findViewById(R.id.btn_save_record).setOnClickListener(v -> saveRecord());
    }

    private void setupCategorySpinner(String[] categories) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        String currentText = etDate.getText().toString();
        if (!currentText.isEmpty()) {
            try {
                String[] parts = currentText.split("-");
                if (parts.length == 3) {
                    calendar.set(Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]) - 1,
                            Integer.parseInt(parts[2]));
                }
            } catch (Exception ignored) {}
        }

        new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) ->
                        etDate.setText(String.format(Locale.getDefault(),
                                "%04d-%02d-%02d", year, month + 1, dayOfMonth)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveRecord() {
        // 获取类型
        String type = rbIncome.isChecked() ? "income" : "expense";

        // 获取类目
        String category = (String) spCategory.getSelectedItem();
        if ("请选择".equals(category)) {
            Toast.makeText(this, "请选择类目", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取金额
        String amountStr = etAmount.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                Toast.makeText(this, "金额必须大于0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效金额", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取日期
        String date = etDate.getText().toString().trim();
        if (date.isEmpty()) {
            Toast.makeText(this, "请选择日期", Toast.LENGTH_SHORT).show();
            return;
        }

        // 获取备注（可选）
        String remark = etRemark.getText().toString().trim();

        // 构建请求
        AddRecordRequest request = new AddRecordRequest();
        request.setType(type);
        request.setCategory(category);
        request.setAmount(amount);
        request.setDate(date);
        request.setRemark(remark.isEmpty() ? null : remark);

        // 发送请求
        String phone = TokenManager.getPhone(this);
        if (phone == null) {
            Toast.makeText(this, "未登录，请重新登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRepository.addRecord(phone, request, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess()) {
                    Toast.makeText(AddRecordActivity.this, "记账成功！", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddRecordActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(AddRecordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
